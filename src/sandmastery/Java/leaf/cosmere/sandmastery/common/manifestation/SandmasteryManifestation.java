/*
 * File updated ~ 18 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.charge.ItemChargeHelper;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import leaf.cosmere.sandmastery.common.items.SandPouchItem;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
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
		requestedModifier *= ((SandmasteryManifestation) manifestation).getRibbonsPerLevel();
		if (requestedModifier > 0)
		{
			if (manifestation.getMode(data) >= modeMax(data)) return 0;
			return submodule.requstRibbons(data, this, requestedModifier);
		}
		else if (requestedModifier < 0)
		{
			if (manifestation.getMode(data) <= modeMin(data)) return 0;
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

	public int getRibbonsPerLevel()
	{
		return 1;
	}

	protected boolean notEnoughChargedSand(ISpiritweb data)
	{
		if (data.getLiving() instanceof Player player)
		{
			List<ItemStack> allPouches = getSandPouches(player);
			int required = getSandCost(data);

			if (allPouches.isEmpty())
			{
				return true;
			}

			int count = 0;
			for (ItemStack stack : allPouches)
			{
				count += StackNBTHelper.getInt(stack, Constants.NBT.CHARGE_LEVEL, 0);
				if (count > required)
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
			List<ItemStack> allPouches = getSandPouches(player);

			int changeLeft = getSandCost(data);
			for (ItemStack stack : allPouches)
			{
				int startingCharge = StackNBTHelper.getInt(stack, Constants.NBT.CHARGE_LEVEL, 0);
				int amountLeft = (startingCharge - changeLeft);
				if (amountLeft >= 0)
				{
					StackNBTHelper.setInt(stack, Constants.NBT.CHARGE_LEVEL, amountLeft);
				}
				else
				{
					StackNBTHelper.setInt(stack, Constants.NBT.CHARGE_LEVEL, 0);
					changeLeft += amountLeft;
				}
				if (changeLeft <= 0)
				{
					break;
				}
			}
		}
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
			preModifiedCost = 1000000000; // If the cost is less than 0, I am over the void. Cost should be high enough cost that you can't reasonably use the power
		return preModifiedCost * SandmasteryConfigs.SERVER.CHARGE_COST_MULTIPLIER.get();
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
	public Attribute getAttribute()
	{
		return SandmasteryAttributes.RIBBONS.getAttribute();
	}

}
