/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.manifestation;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.cosmereEffect.CosmereEffectInstance;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.charge.MetalmindChargeHelper;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.registries.FeruchemyManifestations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
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
	public boolean tick(ISpiritweb data)
	{
		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();

		if (livingEntity.tickCount % 20 != 0)
		{
			return false;
		}

		int mode = getMode(data);

		int adjustAmount;

		CosmereEffect effect = getEffect(mode);

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
			return false;
		}

		final ItemStack itemStack = MetalmindChargeHelper.adjustMetalmindChargeExact(data, metalType, adjustAmount, true, true);
		if (!itemStack.isEmpty())
		{
			CosmereEffectInstance currentEffect = EffectsHelper.getNewEffect(effect, livingEntity, Math.abs(mode) - 1);
			data.addEffect(currentEffect);

			//storing
			if (mode > 0)
			{
				checkStoreNicrosil(data, itemStack);
				return false;
			}
			//tapping
			else
			{
				checkTapNicrosil(data, itemStack);
				return true;
			}
		}
		else
		{
			//this will clear call onModeChange and then clear nicrosil powers.
			data.setMode(this, 0);
		}

		return false;
	}

	private void checkStoreNicrosil(ISpiritweb data, ItemStack metalmind)
	{
		//extra metalmind logic.
		//if we are actually updating the charge inside
		//and if we are adding to it (chargeToGet is negative if adding to metalmind)
		// and if the metal is nicrosil, which is the storing investiture type.
		//player is storing investiture,
		//set the powers they have to the stack.

		CompoundTag nbt = metalmind.getOrCreateTagElement("StoredInvestiture");

		int identity = (int) EntityHelper.getAttributeValue(data.getLiving(), AttributesRegistry.IDENTITY.getAttribute());
		boolean isStoringIdentity = identity < 1;

		//set unkeyed if no identity set
		if (!nbt.contains("identity") && isStoringIdentity)
		{
			nbt.putUUID("identity", Constants.NBT.UNKEYED_UUID);
		}
		else if (!nbt.contains("identity") || (nbt.getUUID("identity").compareTo(Constants.NBT.UNKEYED_UUID) == 0))
		{
			nbt.putUUID("identity", data.getLiving().getUUID());
		}

		final CosmereEffectInstance effectInstance = getOrCreateEffect(getStoringEffect(), data, 0);

		//for each power the user has access to
		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			//even if it's granted from hemalurgy/temporary
			//update the nbt.
			//this will add/remove powers based on what the user currently has.
			final double totalStrength = manifestation.getStrength(data, false);
			final String registryName = manifestation.getRegistryName().toString();
			if (totalStrength > 0)
			{
				nbt.putDouble(registryName, totalStrength);
			}
			//remove if not available
			else if (nbt.contains(registryName))
			{
				nbt.remove(registryName);
			}

			final Attribute attributeRegistryObject = manifestation.getAttribute();

			//don't disable nicrosil, because we want to keep storing
			//don't disable aluminum, because we may be wanting to store without identity
			final boolean invalidMetalToDisable =
					manifestation == FeruchemyManifestations.FERUCHEMY_POWERS.get(Metals.MetalType.NICROSIL).get()
							|| manifestation == FeruchemyManifestations.FERUCHEMY_POWERS.get(Metals.MetalType.ALUMINUM).get();

			if (attributeRegistryObject == null || invalidMetalToDisable)
			{
				continue;
			}

			//also disable all powers
			effectInstance.setDynamicAttribute(manifestation.getAttribute(), -1000, AttributeModifier.Operation.ADDITION);
		}

		data.addEffect(effectInstance);
	}

	private void checkTapNicrosil(ISpiritweb data, ItemStack metalmind)
	{
		CompoundTag nbt = metalmind.getOrCreateTagElement("StoredInvestiture");
		//for each power the user has access to
		//todo add the stored investiture identity to spiritweb data if not there already?

		final CosmereEffectInstance effectInstance = getOrCreateEffect(getTappingEffect(), data, 0);

		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			String manifestationName = manifestation.getRegistryName().toString();
			Attribute attribute = manifestation.getAttribute();
			if (CompoundNBTHelper.verifyExistance(nbt, manifestationName))
			{
				final double strength =
						CompoundNBTHelper.getDouble(
								nbt,
								manifestationName,
								0);

				effectInstance.setDynamicAttribute(attribute, strength, AttributeModifier.Operation.ADDITION);
			}
			else
			{
				//remove any old attributes that don't exist on the item
				//this could happen if the user was switching nicrosil metalminds
				if (attribute != null)
				{
					effectInstance.removeDynamicAttribute(attribute);
				}
			}
		}

		data.addEffect(effectInstance);
	}
}
