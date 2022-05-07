/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items.curio;

import com.google.common.collect.Multimap;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.CompoundNBTHelper;
import leaf.cosmere.utils.helpers.StackNBTHelper;
import leaf.cosmere.utils.helpers.TextHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;


public interface IHemalurgicInfo
{
	String stolen_identity_tag = "stolen_identity_tag";
	List<Metals.MetalType> whiteList = new ArrayList<Metals.MetalType>(4);

	default boolean matchHemalurgicIdentity(ItemStack stack, UUID uniqueID)
	{
		if (!hemalurgicIdentityExists(stack))
		{
			return true;
		}

		return StackNBTHelper.getUuid(stack, stolen_identity_tag).compareTo(uniqueID) == 0;
	}

	default boolean hemalurgicIdentityExists(ItemStack stack)
	{
		return StackNBTHelper.verifyExistance(stack, stolen_identity_tag);
	}

	default void setHemalurgicIdentity(ItemStack stack, UUID uniqueID)
	{
		StackNBTHelper.setUuid(stack, stolen_identity_tag, uniqueID);

	}

	default UUID getHemalurgicIdentity(ItemStack stack)
	{
		return StackNBTHelper.getUuid(stack, stolen_identity_tag);
	}

	default CompoundTag getHemalurgicInfo(ItemStack stack)
	{
		return stack.getOrCreateTagElement("hemalurgy");
	}

	default void stealFromSpiritweb(ItemStack stack, Metals.MetalType spikeMetalType, Player playerEntity, LivingEntity entityKilled)
	{
		//todo
		//we should probably check a config to see if pvp real stealing of attributes is wanted.
		boolean isPlayerEntity = (entityKilled instanceof Player);


		//Steals non-manifestation based abilities. traits inherent to an entity?
		switch (spikeMetalType)
		{
			case IRON:
			case TIN:
			case COPPER:
			case ZINC:
			case ALUMINUM:
			case DURALUMIN:
			case CHROMIUM:
			case NICROSIL:

				//Non-Manifestation based hemalurgy all comes here
				//How much is already stored? (like koloss spikes could keep storing strength on the same spike)
				final double strengthCurrent = getHemalurgicStrength(stack, spikeMetalType);
				//how much should we add.
				final double entityAbilityStrength = spikeMetalType.getEntityAbilityStrength(entityKilled, playerEntity);
				final double strengthToAdd = strengthCurrent + entityAbilityStrength;
				if (strengthToAdd > 0.01 || strengthToAdd < -0.01)
				{
					Invest(stack, spikeMetalType, strengthToAdd, entityKilled.getUUID());
				}
				return;
		}

		List<AManifestation> manifestationsFound = new ArrayList<>();
		SpiritwebCapability.get(entityKilled).ifPresent(entityKilledSpiritWeb ->
		{
			//only grab innate manifestations, not ones added by hemalurgy
			manifestationsFound.addAll(entityKilledSpiritWeb.getAvailableManifestations(true));


			if (manifestationsFound.size() > 0)
			{
				whiteList.clear();

				//The type of thing you can steal is dependant on the type of metal.
				Collection<Metals.MetalType> hemalurgyStealWhitelist = spikeMetalType.getHemalurgyStealWhitelist();
				if (hemalurgyStealWhitelist != null)
				{
					whiteList.addAll(hemalurgyStealWhitelist);
				}

				switch (spikeMetalType)
				{
					//steals allomantic abilities
					case STEEL:
					case BRONZE:
					case CADMIUM:
					case ELECTRUM:
					{
						AManifestation manifestation = getRandomMetalPowerFromList(manifestationsFound, whiteList, Manifestations.ManifestationTypes.ALLOMANCY);
						if (manifestation != null)
						{
							Invest(stack, manifestation, manifestation.getStrength(entityKilledSpiritWeb) * 0.7f, entityKilled.getUUID());
							return;
						}

					}
					break;
					//steals feruchemical abilities
					case PEWTER:
					case BRASS:
					case BENDALLOY:
					case GOLD:
					{
						AManifestation manifestation = getRandomMetalPowerFromList(manifestationsFound, whiteList, Manifestations.ManifestationTypes.FERUCHEMY);
						if (manifestation != null)
						{
							Invest(stack, manifestation, manifestation.getStrength(entityKilledSpiritWeb) * 0.7f, entityKilled.getUUID());
							return;
						}
					}
					break;
					//The god metals don't follow the 'normal' rules.
					//Todo decide if they can steal powers from other investiture types or just scadrial related
					case ATIUM:
					{
						//Steals any one power
						//todo decide if we just pick a random power
						//then try steal it
						//todo decide if prefer allomancy over feruchemy?
						AManifestation manifestation = getRandomMetalPowerFromList(manifestationsFound, whiteList, Manifestations.ManifestationTypes.ALLOMANCY);
						if (manifestation == null)
						{
							manifestation = getRandomMetalPowerFromList(manifestationsFound, whiteList, Manifestations.ManifestationTypes.FERUCHEMY);
						}

						if (manifestation != null)
						{
							Invest(stack, manifestation, manifestation.getStrength(entityKilledSpiritWeb) * 0.7f, entityKilled.getUUID());
							return;
						}
					}
					break;
					case LERASIUM:
					{
						for (AManifestation manifestation : manifestationsFound)
						{
							Invest(stack, manifestation, manifestation.getStrength(entityKilledSpiritWeb), entityKilled.getUUID());
						}
					}
					break;
				}
			}
		});

	}

	default AManifestation getRandomMetalPowerFromList(
			List<AManifestation> manifestationsFound,
			List<Metals.MetalType> whiteList,
			Manifestations.ManifestationTypes powerType)
	{
		Collections.shuffle(whiteList);

		//then check the entity has those types to steal
		for (Metals.MetalType typeToTrySteal : whiteList)
		{
			int i = manifestationsFound.indexOf(powerType.getManifestation(typeToTrySteal.getID()));
			//if it exists in the list
			if (i >= 0)
			{
				//then we've found something to steal!
				return manifestationsFound.get(i);
			}
		}

		return null;
	}

	default Multimap<Attribute, AttributeModifier> getHemalurgicAttributes(Multimap<Attribute, AttributeModifier> attributeModifiers, ItemStack stack, Metals.MetalType metalType)
	{
		UUID hemalurgicIdentity = getHemalurgicIdentity(stack);

		if (metalType == Metals.MetalType.ALUMINUM)
		{
			for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
			{
				String path = manifestation.getName();

				if (!AttributesRegistry.COSMERE_ATTRIBUTES.containsKey(path))
				{
					continue;
				}

				attributeModifiers.put(
						AttributesRegistry.COSMERE_ATTRIBUTES.get(path).get(),
						new AttributeModifier(
								Constants.NBT.ALUMINUM_UUID,
								manifestation.translation().getKey(),
								-100,
								AttributeModifier.Operation.ADDITION));
			}
			return attributeModifiers;
		}
		else if (hemalurgicIdentity == null)
		{
			return attributeModifiers;
		}

		final double strength = getHemalurgicStrength(stack, metalType);

		Attribute attribute = null;
		AttributeModifier.Operation attributeModifier = AttributeModifier.Operation.ADDITION;

		switch (metalType)
		{
			case IRON:
				attribute = Attributes.ATTACK_DAMAGE;
				break;
			case CHROMIUM:
				attribute = Attributes.LUCK;
				break;
			default:
				//TIN:
				//Steals senses
				//a type of night vision

				//Copper:
				//Steals mental fortitude, memory, and intelligence

				final RegistryObject<Attribute> attributeRegistryObject = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getName());
				if (attributeRegistryObject != null && attributeRegistryObject.isPresent())
				{
					attribute = attributeRegistryObject.get();
				}
				break;
            /*
            case ZINC:
                //Steals emotional fortitude
                //todo figure out what that means
                break;
            case NICROSIL:
                //Steals Investiture
                //todo figure out what that means
                break;*/
		}

		if (attribute != null)
		{
			attributeModifiers.put(
					attribute,
					new AttributeModifier(
							hemalurgicIdentity,
							"Hemalurgic " + metalType.getName(),
							strength,
							attributeModifier));
		}


		for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			String path = manifestation.getName();

			final double hemalurgicStrength = getHemalurgicStrength(stack, manifestation);
			if (hemalurgicStrength > 0)
			{
				if (!AttributesRegistry.COSMERE_ATTRIBUTES.containsKey(path))
				{
					continue;
				}

				attributeModifiers.put(
						AttributesRegistry.COSMERE_ATTRIBUTES.get(path).get(),
						new AttributeModifier(
								hemalurgicIdentity,
								String.format("Hemalurgic-%s: %s", path, hemalurgicIdentity.toString()),
								hemalurgicStrength,
								AttributeModifier.Operation.ADDITION));
			}
		}


		return attributeModifiers;
	}

	default double getHemalurgicStrength(ItemStack stack, Metals.MetalType metalType)
	{
		return getHemalurgicStrength(stack, metalType.getName());
	}

	default double getHemalurgicStrength(ItemStack stack, AManifestation manifestation)
	{
		return getHemalurgicStrength(stack, "power_" + manifestation.getName());
	}

	default double getHemalurgicStrength(ItemStack stack, String name)
	{
		return CompoundNBTHelper.getDouble(
				getHemalurgicInfo(stack),
				name,
				0);
	}

	default void setHemalurgicStrength(ItemStack stack, Metals.MetalType metalType, double val)
	{
		setHemalurgicStrength(stack, metalType.getName(), val);
	}

	default void setHemalurgicStrength(ItemStack stack, AManifestation manifestation, double val)
	{
		setHemalurgicStrength(stack, "power_" + manifestation.getName(), val);
	}

	default void setHemalurgicStrength(ItemStack stack, String name, double val)
	{
		CompoundNBTHelper.setDouble(getHemalurgicInfo(stack), name, val);
	}


	default void addInvestitureInformation(ItemStack stack, HemalurgicSpikeItem hemalurgicSpikeItem, List<Component> tooltip)
	{
		if (!hemalurgicIdentityExists(stack))
		{
			return;
		}

		tooltip.add(TextHelper.createTranslatedText(Constants.Strings.CONTAINED_POWERS_FOUND));

		if (hasHemalurgicPower(stack, hemalurgicSpikeItem.getMetalType()))
		{
			tooltip.add(TextHelper.createTranslatedText("tooltip.hemalurgy." + hemalurgicSpikeItem.getMetalType()));

			if (hemalurgicSpikeItem.getMetalType() == Metals.MetalType.IRON)
			{
				double hemalurgicStrength = getHemalurgicStrength(stack, hemalurgicSpikeItem.getMetalType());
				String s = "+" + hemalurgicStrength + " Attack Damage";
				tooltip.add(TextHelper.createText(s));
			}
		}

		IForgeRegistry<AManifestation> manifestations = ManifestationRegistry.MANIFESTATION_REGISTRY.get();
		for (AManifestation manifestation : manifestations)
		{
			// if this spike has that power
			if (hasHemalurgicPower(stack, manifestation))
			{
				//then grant it
				tooltip.add(manifestation.translation());
			}
		}
	}

	default boolean hasHemalurgicPower(ItemStack stack, AManifestation manifestation)
	{
		return getHemalurgicStrength(stack, manifestation) > 0;
	}

	default boolean hasHemalurgicPower(ItemStack stack, Metals.MetalType metalType)
	{
		final double hemalurgicStrength = getHemalurgicStrength(stack, metalType);
		final double marginOfError = 0.01;
		return hemalurgicStrength > marginOfError || hemalurgicStrength < -marginOfError;
	}

	default void Invest(ItemStack stack, AManifestation manifestation, double level, UUID identity)
	{
		setHemalurgicStrength(stack, manifestation, level);
		setHemalurgicIdentity(stack, identity);
	}

	default void Invest(ItemStack stack, Metals.MetalType metalType, double level, UUID identity)
	{
		setHemalurgicStrength(stack, metalType, level);
		setHemalurgicIdentity(stack, identity);
	}

	default void Divest(ItemStack stack)
	{
		StackNBTHelper.removeEntry(stack, "hemalurgy");
		StackNBTHelper.removeEntry(stack, stolen_identity_tag);
	}
}

