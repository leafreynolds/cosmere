/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.Constants;
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
	public boolean tick(ISpiritweb data)
	{
		if (MiscHelper.isClient(data))
		{
			performEffectClient(data);
		}
		return false;
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
	public void onModeChange(ISpiritweb data, int lastMode)
	{
		SpiritwebCapability playerSpiritweb = (SpiritwebCapability) data;
		SandmasterySpiritwebSubmodule submodule = (SandmasterySpiritwebSubmodule) playerSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);
		if (getMode(data) > lastMode)
		{
			submodule.useRibbon(data, this);
		}
		else if (getMode(data) < lastMode)
		{
			submodule.releaseRibbon(data, this);
		}
	}

	protected boolean notEnoughChargedSand(ISpiritweb data)
	{
		if (data.getLiving() instanceof Player player)
		{
			List<ItemStack> allPouches = getSandPouches(player);
			int required = getCost(data);

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

			int changeLeft = getCost(data);
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

	public int getCost(ISpiritweb data)
	{
		int mode = data.getMode(this);
		return mode * SandmasteryConfigs.SERVER.CHARGE_COST_MULTIPLIER.get();
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

	protected void performEffectClient(ISpiritweb data)
	{
	}
}
