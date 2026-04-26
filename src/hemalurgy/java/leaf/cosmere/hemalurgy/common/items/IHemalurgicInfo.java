/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.items;

import com.google.common.collect.Multimap;
import leaf.cosmere.api.*;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.config.HemalurgyConfigs;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

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
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound("hemalurgy");
	}

	default void stealFromSpiritweb(ItemStack stack, Metals.MetalType spikeMetalType, Player playerEntity, LivingEntity entityKilled)
	{
		//todo
		//we should probably check a config to see if pvp real stealing of attributes is wanted.
		boolean isPlayerEntity = (entityKilled instanceof Player);


		//Steals non-manifestation based abilities. traits inherent to an entity?
		if (getAttributes(stack, spikeMetalType, playerEntity, entityKilled))
		{
			return;
		}

		List<Manifestation> manifestationsFound = new ArrayList<>();
		SpiritwebCapability.get(entityKilled).ifPresent(entityKilledSpiritWeb ->
		{
			//only grab innate manifestations, not ones added by hemalurgy
			manifestationsFound.addAll(entityKilledSpiritWeb.getAvailableManifestations(true));


			if (!manifestationsFound.isEmpty())
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
						Manifestation manifestation = getRandomMetalPowerFromList(manifestationsFound, whiteList, Manifestations.ManifestationTypes.ALLOMANCY);
						if (manifestation != null)
						{
							Invest(stack, manifestation, manifestation.getStrength(entityKilledSpiritWeb, true) * 0.7f, entityKilled.getUUID());
							entityKilledSpiritWeb.removeManifestation(manifestation);
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
						Manifestation manifestation = getRandomMetalPowerFromList(manifestationsFound, whiteList, Manifestations.ManifestationTypes.FERUCHEMY);
						if (manifestation != null)
						{
							Invest(stack, manifestation, manifestation.getStrength(entityKilledSpiritWeb, true) * 0.7f, entityKilled.getUUID());
							entityKilledSpiritWeb.removeManifestation(manifestation);
							return;
						}
					}
					break;
					//The god metals don't follow the 'normal' rules.
					//Todo decide if they can steal powers from other investiture types or just scadrial related
					case ATIUM:
					{
						//Steals any one power
						Manifestation manifestation;

						Manifestation atiumAllomancy = CosmereAPI.manifestationRegistry().get(ResourceLocation.fromNamespaceAndPath("allomancy", Metals.MetalType.ATIUM.getName()));
						Manifestation atiumFeruchemy = CosmereAPI.manifestationRegistry().get(ResourceLocation.fromNamespaceAndPath("feruchemy", Metals.MetalType.ATIUM.getName()));

						if (manifestationsFound.contains(atiumFeruchemy))
						{
							manifestation = atiumFeruchemy;
						}
						else if (manifestationsFound.contains(atiumAllomancy))
						{
							manifestation = atiumAllomancy;
						}
						else
						{
							//todo decide if we just pick a random power
							Collections.shuffle(manifestationsFound);
							manifestation = manifestationsFound.get(0);
						}

						//then try steal it
						if (manifestation != null)
						{
							Invest(stack, manifestation, manifestation.getStrength(entityKilledSpiritWeb, true) * 0.7f, entityKilled.getUUID());
							entityKilledSpiritWeb.removeManifestation(manifestation);
							return;
						}
					}
					break;
					case LERASATIUM:
					{
						for (Manifestation manifestation : manifestationsFound)
						{
							Invest(stack, manifestation, manifestation.getStrength(entityKilledSpiritWeb, true) * 0.5f, entityKilled.getUUID());
							entityKilledSpiritWeb.removeManifestation(manifestation);
						}
					}
					break;
				}
			}
		});

	}

	private boolean getAttributes(ItemStack stack, Metals.MetalType spikeMetalType, Player playerEntity, LivingEntity entityKilled)
	{
		switch (spikeMetalType)
		{
			case LERASIUM:
				for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
				{
					//all **attribute** metals EXCEPT lerasium
					switch (metalType)
					{
						case IRON:
						case TIN:
						case COPPER:
						case ZINC:
						case ALUMINUM:
						case DURALUMIN:
						case CHROMIUM:
						case NICROSIL:
							getAttributes(stack, metalType, playerEntity, entityKilled);
							break;
					}
				}
				return true;
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
				return true;
		}
		return false;
	}

	default Manifestation getRandomMetalPowerFromList(
			List<Manifestation> manifestationsFound,
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

	default Multimap<Holder<Attribute>, AttributeModifier> getHemalurgicAttributes(Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers, ItemStack stack, Metals.MetalType metalType)
	{
		UUID hemalurgicIdentity = getHemalurgicIdentity(stack);

		if (metalType == Metals.MetalType.ALUMINUM)
		{
			for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
			{
				final Attribute attribute = manifestation.getAttribute();
				if (attribute == null)
				{
					continue;
				}

				//prevent all other powers being used.
				attributeModifiers.put(
						BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute),
						new AttributeModifier(
								ResourceLocation.fromNamespaceAndPath(Hemalurgy.MODID, Constants.NBT.ALUMINUM_UUID.toString()),
								-100,
								AttributeModifier.Operation.ADD_VALUE));
			}
			return attributeModifiers;
		}
		else if (hemalurgicIdentity == null)
		{
			return attributeModifiers;
		}

		final double strength = getHemalurgicStrength(stack, metalType);

		{
			Holder<Attribute> attribute = null;
			AttributeModifier.Operation attributeModifier = AttributeModifier.Operation.ADD_VALUE;

			switch (metalType)
			{
				case IRON:
					attribute = Attributes.ATTACK_DAMAGE;

					final Attribute xpGainRate = AttributesRegistry.XP_RATE_ATTRIBUTE.getAttribute();
					attributeModifiers.put(
							BuiltInRegistries.ATTRIBUTE.wrapAsHolder(xpGainRate),
							new AttributeModifier(
									ResourceLocation.fromNamespaceAndPath(Hemalurgy.MODID, hemalurgicIdentity + "_koloss"),
									-0.15,
									AttributeModifier.Operation.ADD_VALUE));
					break;
				case TIN:
					//TIN:
					//Steals senses
					//a type of night vision
					attribute = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributesRegistry.NIGHT_VISION_ATTRIBUTE.getAttribute());
					break;
				case COPPER:
					//Copper:
					//Steals mental fortitude, memory, and intelligence
					attribute = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributesRegistry.XP_RATE_ATTRIBUTE.getAttribute());
					break;
				case CHROMIUM:
					attribute = Attributes.LUCK;
					break;
            /*
            case ZINC:
                //Steals emotional fortitude
                //todo figure out what that means
                break;
            case NICROSIL:
                //Steals Investiture
                //todo figure out what that means
                //shardblades?
                break;*/
				default:
					//??
					break;
			}

			if (attribute != null)
			{
				attributeModifiers.put(
						attribute,
						new AttributeModifier(
								ResourceLocation.fromNamespaceAndPath(Hemalurgy.MODID, hemalurgicIdentity.toString()),
								strength,
								attributeModifier));
			}
		}


		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			final double hemalurgicStrength = getHemalurgicStrength(stack, manifestation);
			if (hemalurgicStrength > 0)
			{
				final Attribute regAttribute = manifestation.getAttribute();
				if (regAttribute == null)
				{
					continue;
				}

				attributeModifiers.put(
						BuiltInRegistries.ATTRIBUTE.wrapAsHolder(regAttribute),
						new AttributeModifier(
								ResourceLocation.fromNamespaceAndPath(Hemalurgy.MODID, hemalurgicIdentity + "_" + manifestation.getName()),
								hemalurgicStrength,
								AttributeModifier.Operation.ADD_VALUE));
			}
		}

		return attributeModifiers;
	}

	default double getHemalurgicStrength(ItemStack stack, Metals.MetalType metalType)
	{
		return getHemalurgicStrength(stack, metalType.getName());
	}

	default double getHemalurgicStrength(ItemStack stack, Manifestation manifestation)
	{
		return getHemalurgicStrength(stack, manifestation.getRegistryName().toString());
	}

	default double getHemalurgicStrength(ItemStack stack, String name)
	{
		double strength = CompoundNBTHelper.getDouble(
				getHemalurgicInfo(stack),
				name,
				0);

		HemalurgicSpikeItem spikeItem = (HemalurgicSpikeItem) stack.getItem();
		switch (spikeItem.getMetalType())
		{
			case IRON, TIN, COPPER, ZINC, NICROSIL ->
			{
				//nil
			}
			case LERASATIUM ->
			{
				//lerasatium spikes can't give more than 5
				final int max = HemalurgyConfigs.SERVER.LERASATIUM_MAX_SPIKE_STRENGTH.get();
				strength = Math.min(max, strength);
			}
			case CHROMIUM ->
			{
				final int max = HemalurgyConfigs.SERVER.CHROMIUM_MAX_SPIKE_STRENGTH.get();
				strength = Math.min(max, strength);
			}
			default ->
			{
				//other spikes can't give more than 7
				final int max = HemalurgyConfigs.SERVER.DEFAULT_POWER_MAX_SPIKE_STRENGTH.get();
				strength = Math.min(max, strength);
			}
		}

		return strength;
	}

	default void setHemalurgicStrength(ItemStack stack, String name, double val)
	{
		CustomData.update(DataComponents.CUSTOM_DATA, stack, rootTag ->
		{
			CompoundTag hemalurgy = rootTag.getCompound("hemalurgy");
			CompoundNBTHelper.setDouble(hemalurgy, name, val);
			rootTag.put("hemalurgy", hemalurgy);
		});
	}


	default void addInvestitureInformation(ItemStack stack, List<Component> tooltip)
	{
		if (!hemalurgicIdentityExists(stack))
		{
			return;
		}

		tooltip.add(TextHelper.createTranslatedText(Constants.Strings.CONTAINED_POWERS_FOUND));

		//iterate through all metals, because lerasium spikes steal all attributes
		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			//all **attribute** metals EXCEPT lerasium
			switch (metalType)
			{
				case IRON:
				case TIN:
				case COPPER:
				case ZINC:
				case ALUMINUM:
				case DURALUMIN:
				case CHROMIUM:
				case NICROSIL:
					if (hasHemalurgicPower(stack, metalType))
					{
						double hemalurgicStrength = getHemalurgicStrength(stack, metalType);

						switch (metalType)
						{
							case IRON, CHROMIUM ->
							{
								// don't display as percentage
							}
							default ->
							{
								//but everything else does
								hemalurgicStrength = hemalurgicStrength * 100;
							}
						}
						double roundOff = (double) Math.round(hemalurgicStrength * 100) / 100;

						String sign = roundOff > 0 ? "+" : "";

						tooltip.add(TextHelper.createTranslatedText(
								"tooltip.cosmere.attribute." + metalType.getName(),
								sign,
								roundOff
						));
					}
					break;
			}
		}

		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			// if this spike has that power
			if (hasHemalurgicPower(stack, manifestation))
			{
				//then grant it
				tooltip.add(manifestation.getTextComponent());
			}
		}
	}

	default boolean hasHemalurgicPower(ItemStack stack, Manifestation manifestation)
	{
		return getHemalurgicStrength(stack, manifestation) > 0;
	}

	default boolean hasHemalurgicPower(ItemStack stack, Metals.MetalType metalType)
	{
		final double hemalurgicStrength = getHemalurgicStrength(stack, metalType);
		final double marginOfError = 0.01;
		return hemalurgicStrength > marginOfError || hemalurgicStrength < -marginOfError;
	}

	default void Invest(ItemStack stack, Manifestation manifestation, double level, UUID identity)
	{
		Invest(stack, manifestation.getRegistryName().toString(), level, identity);
	}

	default void Invest(ItemStack stack, Metals.MetalType metalType, double level, UUID identity)
	{
		Invest(stack, metalType.getName(), level, identity);
	}

	default void Invest(ItemStack stack, String manifestation, double level, UUID identity)
	{
		setHemalurgicStrength(stack, manifestation, level);
		setHemalurgicIdentity(stack, identity);
	}

	default void Divest(ItemStack stack)
	{
		StackNBTHelper.removeEntry(stack, "hemalurgy");
		StackNBTHelper.removeEntry(stack, stolen_identity_tag);
	}
}
