/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.items;

import com.google.common.collect.Multimap;
import leaf.cosmere.api.*;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.cap.item.CosmereItemCapabilities;
import leaf.cosmere.common.charge.IChargeable;
import leaf.cosmere.common.datamaps.SpikeProperties;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.capabilities.HemalurgyItemCapabilities;
import leaf.cosmere.hemalurgy.common.config.HemalurgyConfigs;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.*;


public interface IHemalurgicInfo
{
	List<Metals.MetalType> whiteList = new ArrayList<Metals.MetalType>(4);

	default boolean matchHemalurgicIdentity(ItemStack stack, UUID uniqueID)
	{
		final UUID identity = getHemalurgicIdentity(stack);
		if (identity == null)
		{
			return true;
		}

		return identity.compareTo(uniqueID) == 0;
	}

	default boolean hemalurgicIdentityExists(ItemStack stack)
	{
		return stack.has(HemalurgyDataComponents.STOLEN_IDENTITY.get());
	}

	default void setHemalurgicIdentity(ItemStack stack, UUID uniqueID)
	{
		stack.set(HemalurgyDataComponents.STOLEN_IDENTITY.get(), uniqueID);
	}

	default UUID getHemalurgicIdentity(ItemStack stack)
	{
		return stack.get(HemalurgyDataComponents.STOLEN_IDENTITY.get());
	}

	//detached copy - write back via setHemalurgicStrength, never mutate in place
	default CompoundTag getHemalurgicInfo(ItemStack stack)
	{
		final CompoundTag tag = stack.get(HemalurgyDataComponents.SPIKE_POWERS.get());
		return tag != null ? tag.copy() : new CompoundTag();
	}

	//null means not a spike
	default Metals.MetalType getSpikeMetalType(ItemStack stack)
	{
		if (stack.getItem() instanceof IHasMetalType hasMetalType)
		{
			return hasMetalType.getMetalType();
		}
		SpikeProperties properties = HemalurgyItemCapabilities.getSpikeProperties(stack.getItem());
		return properties != null ? properties.metal() : null;
	}

	//kill-steal entry point. https://wob.coppermind.net/events/332/#e9569
	default void killedEntity(ItemStack stack, Player playerEntity, LivingEntity entityKilled)
	{
		// do nothing if an identity exists and doesn't match
		if (!matchHemalurgicIdentity(stack, entityKilled.getUUID()))
		{
			return;
		}

		Metals.MetalType spikeMetalType = getSpikeMetalType(stack);
		if (spikeMetalType == null)
		{
			return;
		}

		// ensure we set the stolen identity
		stealFromSpiritweb(stack, spikeMetalType, playerEntity, entityKilled);
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
						if (manifestation != null
								&& Invest(stack, manifestation, manifestation.getStrength(entityKilledSpiritWeb, true) * 0.7f, entityKilled.getUUID()) > 0)
						{
							//only take the power if the spike had room to hold any of it
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
						if (manifestation != null
								&& Invest(stack, manifestation, manifestation.getStrength(entityKilledSpiritWeb, true) * 0.7f, entityKilled.getUUID()) > 0)
						{
							//only take the power if the spike had room to hold any of it
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
						if (manifestation != null
								&& Invest(stack, manifestation, manifestation.getStrength(entityKilledSpiritWeb, true) * 0.7f, entityKilled.getUUID()) > 0)
						{
							//only take the power if the spike had room to hold any of it
							entityKilledSpiritWeb.removeManifestation(manifestation);
							return;
						}
					}
					break;
					case LERASATIUM:
					{
						for (Manifestation manifestation : manifestationsFound)
						{
							//vessel fills up. powers that don't fit stay with the victim
							if (Invest(stack, manifestation, manifestation.getStrength(entityKilledSpiritWeb, true) * 0.5f, entityKilled.getUUID()) > 0)
							{
								entityKilledSpiritWeb.removeManifestation(manifestation);
							}
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
				//raw read: the capped/display value must never feed back into storage
				final double strengthCurrent = getRawHemalurgicStrength(stack, spikeMetalType.getName());
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

	// 1.21.1: AttributeModifier ids are ResourceLocations, not UUIDs. Modifier ids derived
	// from the hemalurgic identity stay stable per stolen identity, like the old UUID ids did.
	private static ResourceLocation hemalurgicModifierId(String prefix, Object suffix)
	{
		return ResourceLocation.fromNamespaceAndPath(Hemalurgy.MODID, prefix + "_" + suffix);
	}

	default Multimap<Holder<Attribute>, AttributeModifier> getHemalurgicAttributes(Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers, ItemStack stack, Metals.MetalType metalType)
	{
		UUID hemalurgicIdentity = getHemalurgicIdentity(stack);

		if (metalType == Metals.MetalType.ALUMINUM)
		{
			for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
			{
				final Holder<Attribute> attribute = manifestation.getAttribute();
				if (attribute == null)
				{
					continue;
				}

				//prevent all other powers being used.
				attributeModifiers.put(
						attribute,
						new AttributeModifier(
								hemalurgicModifierId("aluminum", Constants.NBT.ALUMINUM_UUID),
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

					final Holder<Attribute> xpGainRate = AttributesRegistry.XP_RATE_ATTRIBUTE.getHolder();
					attributeModifiers.put(
							xpGainRate,
							new AttributeModifier(
									hemalurgicModifierId("kolossification", hemalurgicIdentity),
									-0.15,
									AttributeModifier.Operation.ADD_VALUE));
					break;
				case TIN:
					//TIN:
					//Steals senses
					//a type of night vision
					attribute = AttributesRegistry.NIGHT_VISION_ATTRIBUTE.getHolder();
					break;
				case COPPER:
					//Copper:
					//Steals mental fortitude, memory, and intelligence
					attribute = AttributesRegistry.XP_RATE_ATTRIBUTE.getHolder();
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
								hemalurgicModifierId("hemalurgic_" + metalType.getName(), hemalurgicIdentity),
								strength,
								attributeModifier));
			}
		}


		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			String path = manifestation.getName();

			final double hemalurgicStrength = getHemalurgicStrength(stack, manifestation);
			if (hemalurgicStrength > 0)
			{
				final Holder<Attribute> regAttribute = manifestation.getAttribute();
				if (regAttribute == null)
				{
					continue;
				}

				attributeModifiers.put(
						regAttribute,
						new AttributeModifier(
								hemalurgicModifierId("hemalurgic_" + manifestation.getRegistryName().getNamespace() + "_" + path, hemalurgicIdentity),
								hemalurgicStrength,
								AttributeModifier.Operation.ADD_VALUE));
			}
		}

		return attributeModifiers;
	}

	//one vessel: spikeMaxTotalStrength units shared between stolen powers and feruchemical charge
	default int getSpikeInvestitureCapacity()
	{
		return HemalurgyConfigs.SERVER.SPIKE_TOTAL_STRENGTH_CAPACITY.get();
	}

	//no caps or scaling - internal accounting only
	default double getRawHemalurgicStrength(ItemStack stack, String name)
	{
		return CompoundNBTHelper.getDouble(getHemalurgicInfo(stack), name, 0);
	}

	//vessel units used by hemalurgy. negative investments still occupy the metal
	default double getTotalStolenStrength(ItemStack stack)
	{
		double total = 0;
		final CompoundTag powers = getHemalurgicInfo(stack);
		for (String key : powers.getAllKeys())
		{
			total += Math.abs(powers.getDouble(key));
		}
		return total;
	}

	//fill measured against BASE max to avoid feedback with getMaxCharge
	default double getFeruchemicalChargeUnits(ItemStack stack)
	{
		final IChargeable chargeable = CosmereItemCapabilities.getChargeable(stack);
		if (chargeable == null)
		{
			return 0;
		}
		final int baseMax = chargeable.getBaseMaxCharge(stack);
		if (baseMax <= 0)
		{
			return 0;
		}
		final double fill = Mth.clamp((double) chargeable.getCharge(stack) / baseMax, 0, 1);
		return fill * getSpikeInvestitureCapacity();
	}

	//stolen powers shrink feruchemical capacity
	default int scaleMaxChargeByInvestiture(ItemStack stack, int baseMaxCharge)
	{
		final int capacity = getSpikeInvestitureCapacity();
		final double hemalurgicFill = Mth.clamp(getTotalStolenStrength(stack) / capacity, 0, 1);
		return Mth.floor(baseMaxCharge * (1 - hemalurgicFill));
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

		final Metals.MetalType spikeMetal = getSpikeMetalType(stack);
		if (spikeMetal == null)
		{
			//not a spike, so use the default cap
			return Math.min(HemalurgyConfigs.SERVER.DEFAULT_POWER_MAX_SPIKE_STRENGTH.get(), strength);
		}

		switch (spikeMetal)
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
		//read-modify-write: the getter hands out a detached copy
		CompoundTag hemalurgyInfo = getHemalurgicInfo(stack);
		CompoundNBTHelper.setDouble(hemalurgyInfo, name, val);
		stack.set(HemalurgyDataComponents.SPIKE_POWERS.get(), hemalurgyInfo);
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

		Registry<Manifestation> manifestations = CosmereAPI.manifestationRegistry();
		for (Manifestation manifestation : manifestations)
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

	default double Invest(ItemStack stack, Manifestation manifestation, double level, UUID identity)
	{
		return Invest(stack, manifestation.getRegistryName().toString(), level, identity);
	}

	default double Invest(ItemStack stack, Metals.MetalType metalType, double level, UUID identity)
	{
		return Invest(stack, metalType.getName(), level, identity);
	}

	//absolute set, clamped to free room. returns what fit, 0 means full.
	//displaces stored charge that no longer fits
	default double Invest(ItemStack stack, String manifestation, double level, UUID identity)
	{
		final int capacity = getSpikeInvestitureCapacity();
		final double usedByOtherPowers = getTotalStolenStrength(stack) - Math.abs(getRawHemalurgicStrength(stack, manifestation));
		final double roomForThisPower = Math.max(0, capacity - usedByOtherPowers - getFeruchemicalChargeUnits(stack));

		final double storedMagnitude = Math.min(Math.abs(level), roomForThisPower);
		if (storedMagnitude < 0.01)
		{
			return 0;
		}
		final double stored = Math.signum(level) * storedMagnitude;

		setHemalurgicStrength(stack, manifestation, stored);
		setHemalurgicIdentity(stack, identity);

		//push out stored charge that no longer fits
		final IChargeable chargeable = CosmereItemCapabilities.getChargeable(stack);
		if (chargeable != null)
		{
			final int newMax = chargeable.getMaxCharge(stack);
			if (chargeable.getCharge(stack) > newMax)
			{
				chargeable.setCharge(stack, newMax / Math.max(1, stack.getCount()));
			}
		}

		//built-in spikes glint via isFoil, foreign items need the component
		if (!(stack.getItem() instanceof HemalurgicSpikeItem))
		{
			stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
		}

		return stored;
	}

	default void Divest(ItemStack stack)
	{
		stack.remove(HemalurgyDataComponents.SPIKE_POWERS.get());
		stack.remove(HemalurgyDataComponents.STOLEN_IDENTITY.get());

		if (!(stack.getItem() instanceof HemalurgicSpikeItem))
		{
			stack.remove(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
		}
	}
}
