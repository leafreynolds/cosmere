/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.charge.MetalmindChargeHelper;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.CompoundNBTHelper;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;


//storing all the available powers on the user individually
public class FeruchemyNicrosil extends FeruchemyBase
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

		int cost;

		MobEffect effect = getEffect(mode);

		// if we are tapping
		//check if there is charges to tap
		if (mode < 0)
		{
			//wanting to tap
			//get cost
			cost = mode <= -3 ? -(mode * mode) : mode;

		}
		//if we are storing
		//check if there is space to store
		else if (mode > 0)
		{
			cost = mode;
		}
		//can't store or tap any more
		else
		{
			//remove active effects.
			//let the current effect run out.
			return;
		}

		final ItemStack itemStack = MetalmindChargeHelper.adjustMetalmindChargeExact(data, metalType, -cost, true, true);
		if (itemStack != null)
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
	public void onModeChange(ISpiritweb data)
	{
		super.onModeChange(data);

		if (getMode(data) == 0)
		{
			//clear
			clearNicrosilPowers(data);
		}
	}

	public void clearNicrosilPowers(ISpiritweb data)
	{
		for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			RegistryObject<Attribute> attributeRegistryObject = manifestation.getAttribute();
			if (attributeRegistryObject == null || !attributeRegistryObject.isPresent())
			{
				continue;
			}

			final Attribute pAttribute = manifestation.getAttribute().get();
			final AttributeInstance attribute = data.getLiving().getAttribute(pAttribute);
			if (attribute != null)
			{
				attribute.removeModifier(data.getLiving().getUUID());
				attribute.removeModifier(Constants.NBT.UNKEYED_UUID);
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
		MobEffectInstance storingIdentity = data.getLiving().getEffect(EffectsRegistry.STORING_EFFECTS.get(Metals.MetalType.ALUMINUM).get());
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
		for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			//even if it's granted from hemalurgy/temporary
			//update the nbt.
			//this will add/remove powers based on what the user currently has.
			if (data.hasManifestation(manifestation))
			{
				nbt.putDouble(manifestation.getName(), manifestation.getStrength(data, false));
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

		for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			String manifestationName = manifestation.getName();
			RegistryObject<Attribute> attributeRegistryObject = manifestation.getAttribute();
			if (!CompoundNBTHelper.verifyExistance(nbt, manifestationName) || attributeRegistryObject == null || !attributeRegistryObject.isPresent())
			{
				continue;
			}

			attributeModifiers.put(
					attributeRegistryObject.get(),
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
