/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.charge.MetalmindChargeHelper;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.IHasMetalType;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.ManifestationBase;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.KeybindingRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.registries.RegistryObject;

public class AllomancyBase extends ManifestationBase implements IHasMetalType
{
	private final Metals.MetalType metalType;

	public AllomancyBase(Metals.MetalType metalType)
	{
		super(Manifestations.ManifestationTypes.ALLOMANCY, metalType.getColorValue());
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
	public int modeMin(ISpiritweb data)
	{
		//Allmancy doesn't have a negative, so 0 as 'off'
		return 0;
	}

	@Override
	public boolean isActive(ISpiritweb data)
	{
		return super.isActive(data) && isMetalBurning(data);
	}

	//A metal is considered burning if the user has the power and can afford the next tick of burning.
	public boolean isMetalBurning(ISpiritweb data)
	{
		int mode = getMode(data);

		//make sure the user can afford the cost of burning this metal
		while (mode > 0)
		{
			//if not then try reduce the amount that they are burning

			if (data.adjustIngestedMetal(metalType, mode, false))
			{
				return true;
			}
			else
			{
				mode--;
				//set that mode back to the capability.
				data.setMode(this, mode);
				//if it hits zero then return out
				//try again at a lower burn rate.
			}
		}
		return false;
	}

	@Override
	public void tick(ISpiritweb data)
	{
		if (!isActive(data))
		{
			return;
		}

		int mode = getMode(data);

		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();
		boolean isActiveTick = livingEntity.tickCount % 20 == 0;
		data.adjustIngestedMetal(metalType, mode, isActiveTick);

		//if we get to this point, we are in an active burn state.
		//check for compound.
		final AManifestation feruchemyManifestation = ManifestationRegistry.FERUCHEMY_POWERS.get(metalType).get();
		int feruchemyMode = data.hasManifestation(feruchemyManifestation)
		                    ? feruchemyManifestation.getMode(data)
		                    : 0;

		//feruchemy power exists and is active
		if (feruchemyMode != 0 && isActiveTick)
		{
			//todo config variable
			//eg 10 base, * 2 for flaring mode = 20
			//or from spike 7 * 2 = 14
			//then add the config value
			//max should be around 30. 50 was way too much

			int secondsOfFeruchemyToAdd = (int) Math.floor(getRange(data)) - 5;
			if (null != MetalmindChargeHelper.adjustMetalmindChargeExact(data, metalType, (-secondsOfFeruchemyToAdd) * (mode), true, true))
			{
				//compound successful
			}
		}

		applyEffectTick(data);
	}

	protected KeyMapping getKeyBinding()
	{
		if (getMetalType().isPullMetal())
		{
			return KeybindingRegistry.ALLOMANCY_PULL;
		}
		else if (getMetalType().isPushMetal())
		{
			return KeybindingRegistry.ALLOMANCY_PUSH;
		}

		return null;
	}

	public double getStrength(ISpiritweb data, boolean getBaseStrength)
	{
		AttributeInstance attribute = data.getLiving().getAttribute(getAttribute().get());
		if (attribute != null)
		{
			return getBaseStrength ? attribute.getBaseValue() : attribute.getValue();
		}
		return 0;
	}


	public int getRange(ISpiritweb data)
	{
		if (!isActive(data))
		{
			return 0;
		}

		//get allomantic strength
		double allomanticStrength = getStrength(data, false);
		return Mth.floor(allomanticStrength * getMode(data));

	}

	@Override
	public RegistryObject<Attribute> getAttribute()
	{
		return AttributesRegistry.ALLOMANCY_ATTRIBUTES.get(metalType);
	}
}
