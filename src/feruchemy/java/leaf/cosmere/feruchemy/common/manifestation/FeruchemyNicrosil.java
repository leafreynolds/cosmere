/*
 * File updated ~ 24 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.manifestation;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.charge.MetalmindChargeHelper;
import leaf.cosmere.feruchemy.common.registries.FeruchemyEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

//storing all the available powers on the user individually
public class FeruchemyNicrosil extends FeruchemyManifestation
{
	public FeruchemyNicrosil(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public int modeMin(ISpiritweb data)
	{
		return -1;
	}

	@Override
	public int modeMax(ISpiritweb data)
	{
		return 1;
	}

	@Override
	public void tick(ISpiritweb data)
	{
		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();

		if (livingEntity.tickCount % 20 != 0)
		{
			return;
		}

		int mode = getMode(data);

		int adjustAmount;

		MobEffect effect = getEffect(mode);

		// if we are tapping
		//check if there is charges to tap
		if (mode < 0)
		{
			//wanting to tap
			//get adjustAmount
			adjustAmount = mode <= -3 ? -(mode * mode) : mode;

		}
		//if we are storing
		//check if there is space to store
		else if (mode > 0)
		{
			adjustAmount = mode;
		}
		//can't store or tap any more
		else
		{
			//remove active effects.
			//let the current effect run out.
			return;
		}

		final ItemStack itemStack = MetalmindChargeHelper.adjustMetalmindChargeExact(data, metalType, adjustAmount, true, true);
		if (!itemStack.isEmpty())
		{
			MobEffectInstance currentEffect = EffectsHelper.getNewEffect(effect, Math.abs(mode) - 1);
			livingEntity.addEffect(currentEffect);

			//storing
			if (mode > 0)
			{
				checkStoreNicrosil(data, itemStack);
			}
			//tapping
			else
			{
				checkTapNicrosil(data, itemStack);
			}
		}
		else
		{
			//this will clear call onModeChange and then clear nicrosil powers.
			data.setMode(this, 0);
		}

	}

	@Override
	public void onModeChange(ISpiritweb data, int lastMode)
	{
		super.onModeChange(data, lastMode);

		if (getMode(data) == 0)
		{
			//clear
			clearNicrosilPowers(data);
		}
	}

	public void clearNicrosilPowers(ISpiritweb data)
	{
		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			Attribute attribute = manifestation.getAttribute();
			if (attribute == null)
			{
				continue;
			}

			final AttributeInstance attributeInstance = data.getLiving().getAttribute(attribute);
			if (attributeInstance != null)
			{
				attributeInstance.removeModifier(data.getLiving().getUUID());
				attributeInstance.removeModifier(Constants.NBT.UNKEYED_UUID);
			}
		}
	}

	private static void checkStoreNicrosil(ISpiritweb data, ItemStack metalmind)
	{
		//extra metalmind logic.
		//if we are actually updating the charge inside
		//and if we are adding to it (chargeToGet is negative if adding to metalmind)
		// and if the metal is nicrosil, which is the storing investiture type.
		//player is storing investiture,
		//set the powers they have to the stack.

		CompoundTag nbt = metalmind.getOrCreateTagElement("StoredInvestiture");
		MobEffectInstance storingIdentity = data.getLiving().getEffect(FeruchemyEffects.STORING_EFFECTS.get(Metals.MetalType.ALUMINUM).get());
		boolean isStoringIdentity = (storingIdentity != null && storingIdentity.getDuration() > 0);

		//set unkeyed if no identity set
		if (!nbt.contains("identity") && isStoringIdentity)
		{
			nbt.putUUID("identity", Constants.NBT.UNKEYED_UUID);
		}
		else if (!nbt.contains("identity") || (nbt.getUUID("identity").compareTo(Constants.NBT.UNKEYED_UUID) == 0))
		{
			nbt.putUUID("identity", data.getLiving().getUUID());
		}

		//for each power the user has access to
		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			//even if it's granted from hemalurgy/temporary
			//update the nbt.
			//this will add/remove powers based on what the user currently has.
			final double baseStrength = manifestation.getStrength(data, true);
			if (baseStrength > 0)
			{
				nbt.putDouble(manifestation.getName(), baseStrength);
			}
			//remove if not available
			else if (nbt.contains(manifestation.getName()))
			{
				nbt.remove(manifestation.getName());
			}
		}
	}

	private static void checkTapNicrosil(ISpiritweb data, ItemStack metalmind)
	{
		//todo better nicrosil tracking.
		Multimap<Attribute, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();
		CompoundTag nbt = metalmind.getOrCreateTagElement("StoredInvestiture");
		//for each power the user has access to
		//todo add the stored investiture identity to spiritweb data if not there already?

		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			String manifestationName = manifestation.getName();
			Attribute attribute = manifestation.getAttribute();
			if (!CompoundNBTHelper.verifyExistance(nbt, manifestationName) || attribute == null)
			{
				continue;
			}

			attributeModifiers.put(
					attribute,
					new AttributeModifier(
							nbt.hasUUID("identity") ? nbt.getUUID("identity") : Constants.NBT.UNKEYED_UUID,
							manifestationName,
							CompoundNBTHelper.getDouble(
									nbt,
									manifestationName,
									0),
							AttributeModifier.Operation.ADDITION));

		}

		data.getLiving().getAttributes().addTransientAttributeModifiers(attributeModifiers);
	}

}
