/*
 * File updated ~ 18 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.charge.ItemChargeHelper;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import leaf.cosmere.sandmastery.common.diem.DiemProgression;
import leaf.cosmere.sandmastery.common.items.SandPouchItem;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import leaf.cosmere.sandmastery.common.utils.SandmasteryConstants;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public class SandmasteryManifestation extends Manifestation
{
	protected final Taldain.Mastery mastery;

	public SandmasteryManifestation(Taldain.Mastery mastery)
	{
		super(Manifestations.ManifestationTypes.SANDMASTERY);
		this.mastery = mastery;
	}

	@Override
	public int getPowerID()
	{
		return mastery.getID();
	}

	@Override
	public int modeMin(ISpiritweb data)
	{
		return 0;
	}

	@Override
	public int modeMax(ISpiritweb data)
	{
		return (int) data.getSelectedManifestation().getStrength(data, false);
	}

	@Override
	public int getModeModifier(ISpiritweb data, Manifestation manifestation, int requestedModifier)
	{
		SpiritwebCapability playerSpiritweb = (SpiritwebCapability) data;
		SandmasterySpiritwebSubmodule submodule = (SandmasterySpiritwebSubmodule) playerSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);
		sanityCheckRibbons(data); //todo: find a better way of doing this, I don't want to loop over the manifestations every time
		requestedModifier *= ((SandmasteryManifestation) manifestation).getRibbonsPerLevel(data);
		if (requestedModifier > 0)
		{
			if (manifestation.getMode(data) >= modeMax(data))
			{
				return 0;
			}
			return submodule.requstRibbons(data, this, requestedModifier);
		}
		else if (requestedModifier < 0)
		{
			if (manifestation.getMode(data) <= modeMin(data))
			{
				return 0;
			}
			return -submodule.returnRibbons(data, this, -requestedModifier); // function expects the number of returned ribbons to be positive, and returns the number of ribbons returned as a positive integer.
		}
		return requestedModifier;
	}

	public void sanityCheckRibbons(ISpiritweb data)
	{
		int ribbons = 0;
		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			if (manifestation.getManifestationType() == Manifestations.ManifestationTypes.SANDMASTERY)
			{
				ribbons += data.getMode(manifestation);
			}
		}
		SpiritwebCapability playerSpiritweb = (SpiritwebCapability) data;
		SandmasterySpiritwebSubmodule submodule = (SandmasterySpiritwebSubmodule) playerSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);
		if (ribbons != submodule.getUsedRibbons())
		{
			submodule.setUsedRibbons(ribbons);
		}
	}

	public int getRibbonsPerLevel(ISpiritweb data)
	{
		return 1;
	}

	protected boolean notEnoughChargedSand(ISpiritweb data)
	{
		if (data.getLiving() instanceof Player player)
		{
			List<ItemStack> allPouches = getSandPouches(player);
			if (allPouches.isEmpty())
			{
				return true;
			}

			// only whole layers are paid for, the remainder carries over as debt
			long required = chargeOwedNow(data);

			long count = 0;
			for (ItemStack stack : allPouches)
			{
				count += ((SandPouchItem) stack.getItem()).getCharge(stack);
				if (count >= required)
				{
					return false;
				}
			}
		}
		return true;
	}

	public void useChargedSand(ISpiritweb data)
	{
		if (data.getLiving() instanceof Player player)
		{
			SandmasterySpiritwebSubmodule submodule = SandmasterySpiritwebSubmodule.get(data);
			long debt = submodule.getChargeDebt() + (long) getSandCost(data);
			long changeLeft = debt - debt % SandmasteryConstants.CHARGE_PER_SAND_LAYER;
			submodule.setChargeDebt((int) (debt - changeLeft));

			int spent = 0;
			for (ItemStack stack : getSandPouches(player))
			{
				if (changeLeft <= 0)
				{
					break;
				}
				SandPouchItem pouch = (SandPouchItem) stack.getItem();
				int taken = (int) Math.min(pouch.getCharge(stack), changeLeft);
				pouch.setCharge(stack, pouch.getCharge(stack) - taken);
				changeLeft -= taken;
				spent += taken;
			}
			DiemProgression.addPracticeXp(data, spent);
		}
	}

	// charge to take from the pouches this tick: the cost plus any debt, rounded down to whole layers
	private long chargeOwedNow(ISpiritweb data)
	{
		long debt = SandmasterySpiritwebSubmodule.get(data).getChargeDebt() + (long) getSandCost(data);
		return debt - debt % SandmasteryConstants.CHARGE_PER_SAND_LAYER;
	}

	protected List<ItemStack> getSandPouches(Player player)
	{
		List<ItemStack> curios = ItemChargeHelper.getChargeCurios(player);
		List<ItemStack> items = ItemChargeHelper.getChargeItems(player);

		curios.removeIf(getIsItemInvalid());
		items.removeIf(getIsItemInvalid());

		curios.addAll(items);

		return curios;
	}

	protected int getBaseCost()
	{
		return 10;
	}

	public int getSandCost(ISpiritweb data)
	{
		int preModifiedCost = MiscHelper.distanceFromGround(data.getLiving()) * getBaseCost();
		if (preModifiedCost < 0)
		{
			preModifiedCost = 1000000000; // If the cost is less than 0, I am over the void. Cost should be high enough cost that you can't reasonably use the power
		}
		// the void cost times the multiplier would overflow an int, so cap it
		return (int) Math.min((long) preModifiedCost * SandmasteryConfigs.SERVER.CHARGE_COST_MULTIPLIER.get(), Integer.MAX_VALUE);
	}

	public int getHydrationCost(ISpiritweb data)
	{
		return (int) Math.round(getSandCost(data) * SandmasteryConfigs.SERVER.HYDRATION_COST_MULTIPLIER.get());
	}

	private static Predicate<ItemStack> getIsItemInvalid()
	{
		return obj ->
		{
			if (obj.getItem() instanceof SandPouchItem)
			{
				return false;
			}
			return true;
		};
	}

	@Override
	public Holder<Attribute> getAttribute()
	{
		return SandmasteryAttributes.RIBBONS.getHolder();
	}

}
