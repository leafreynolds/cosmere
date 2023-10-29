/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.client.AllomancyKeybindings;
import leaf.cosmere.allomancy.common.capabilities.AllomancySpiritwebSubmodule;
import leaf.cosmere.allomancy.common.registries.AllomancyStats;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.charge.MetalmindChargeHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class AllomancyManifestation extends Manifestation implements IHasMetalType
{
	private final Metals.MetalType metalType;

	public AllomancyManifestation(Metals.MetalType metalType)
	{
		super(Manifestations.ManifestationTypes.ALLOMANCY);
		this.metalType = metalType;
	}


	@Override
	public int getPowerID()
	{
		return metalType.getID();
	}

	@Override
	public boolean modeWraps(ISpiritweb data)
	{
		return true;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}

	//active or not active
	@Override
	public int modeMax(ISpiritweb data)
	{
		//1 for burning
		//2 for flaring
		return 2;
	}

	@Override
	public void onModeChange(ISpiritweb data, int lastMode)
	{
		super.onModeChange(data, lastMode);

		if (Mth.abs(getMode(data)) != 0)
		{
			//don't reset stats while burning
			return;
		}

		if (data.getLiving() instanceof ServerPlayer serverPlayer)
		{
			serverPlayer.resetStat(Stats.CUSTOM.get(getBurnTimeStat()));
		}
	}

	@Override
	public int modeMin(ISpiritweb data)
	{
		final ResourceLocation feruchemyRL = new ResourceLocation("feruchemy", getRegistryName().getPath());
		final Manifestation feruchemy = CosmereAPI.manifestationRegistry().getValue(feruchemyRL);
		if (data.hasManifestation(feruchemy))
		{
			//compounding
			return -2;
		}

		return 0;
	}

	@Override
	public boolean isActive(ISpiritweb data)
	{
		return super.isActive(data) && isMetalBurning(data);
	}

	public boolean isCompounding(ISpiritweb data)
	{
		return isMetalBurning(data) && getMode(data) < 0;
	}

	public boolean isAllomanticBurn(ISpiritweb data)
	{
		return isMetalBurning(data) && getMode(data) > 0;
	}

	//A metal is considered burning if the user has the power and can afford the next tick of burning.
	public boolean isMetalBurning(ISpiritweb data)
	{
		//absolute value, because compounding uses negative modes.
		int modeAbs = Mth.abs(getMode(data));
		AllomancySpiritwebSubmodule allo = (AllomancySpiritwebSubmodule) ((SpiritwebCapability) data).getSubmodule(Manifestations.ManifestationTypes.ALLOMANCY);

		//make sure the user can afford the cost of burning this metal
		while (modeAbs > 0)
		{
			//if not then try reduce the amount that they are burning

			if (allo.adjustIngestedMetal(metalType, -modeAbs, false))
			{
				return true;
			}
			else
			{
				//todo fix this to work for compounding,
				modeAbs--;
				//set that mode back to the capability.
				data.setMode(this, modeAbs);
				//if it hits zero then return out
				//try again at a lower burn rate.
			}
		}
		return false;
	}

	@Override
	public boolean tick(ISpiritweb data)
	{
		if (!isActive(data))
		{
			return false;
		}

		int mode = getMode(data);
		final int cost = Mth.abs(mode);

		AllomancySpiritwebSubmodule allo = (AllomancySpiritwebSubmodule) ((SpiritwebCapability) data).getSubmodule(Manifestations.ManifestationTypes.ALLOMANCY);

		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();
		boolean isActiveTick = livingEntity.tickCount % 20 == 0;
		allo.adjustIngestedMetal(metalType, -cost, isActiveTick);

		if (isActiveTick && livingEntity instanceof ServerPlayer serverPlayer)
		{
			serverPlayer.awardStat(getBurnTimeStat());
		}

		//if burning normally, do allomancy
		if (mode > 0)
		{
			applyEffectTick(data);
			return true;
		}
		//else funnel all that power to feruchemy attribute
		//but only if active tick
		else if (isActiveTick)
		{
			tickCompounding(data, cost);
			return true;
		}

		return false;
	}

	private void tickCompounding(ISpiritweb data, int allomanticSecondsUsed)
	{
		//if we get to this point, we are in an active burn state.
		//check for compound.
		final Manifestation feruchemyManifestation = CosmereAPI.manifestationRegistry().getValue(new ResourceLocation("feruchemy", getRegistryName().getPath()));

		//player has feruchemy in same metal
		if (data.hasManifestation(feruchemyManifestation))
		{
			//TODO config variable
			//eg 10 base, * 2 for flaring mode = 20
			//or from spike 7 * 2 = 14
			//then add the config value
			//max should be around 30. 50 was way too much

			final double compoundStrength = getStrength(data, false) * Mth.abs(allomanticSecondsUsed);
			int secondsOfFeruchemyToAdd = Mth.absFloor(compoundStrength) - 5;

			if (secondsOfFeruchemyToAdd > 0)
			{
				final ItemStack metalmindStack =
						MetalmindChargeHelper.adjustMetalmindChargeExact(
								data,
								metalType,
								secondsOfFeruchemyToAdd,
								true,
								true);

				if (!metalmindStack.isEmpty())
				{
					//compound successful
				}
			}
		}
	}

	private ResourceLocation getBurnTimeStat()
	{
		final ResourceLocation resourceLocation = AllomancyStats.ALLOMANCY_BURN_TIME.get(this.metalType).get();
		//force set this stat to be time related, which happens in the get function
		Stat<ResourceLocation> doot = Stats.CUSTOM.get(resourceLocation, StatFormatter.TIME);
		return resourceLocation;
	}

	protected KeyMapping getKeyBinding()
	{
		if (getMetalType().isPullMetal())
		{
			return AllomancyKeybindings.ALLOMANCY_PULL;
		}
		else if (getMetalType().isPushMetal())
		{
			return AllomancyKeybindings.ALLOMANCY_PUSH;
		}

		return null;
	}


	public int getRange(ISpiritweb data)
	{
		if (!isActive(data))
		{
			return 0;
		}

		//get allomantic strength
		double allomanticStrength = getStrength(data, false);

		//no range if compounding.
		final int mode = Math.max(getMode(data), 0);
		return Mth.floor(allomanticStrength * mode);
	}

}
