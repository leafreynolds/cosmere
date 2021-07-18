/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items.curio;

import com.google.common.collect.Multimap;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.CompoundNBTHelper;
import leaf.cosmere.utils.helpers.StackNBTHelper;
import leaf.cosmere.utils.helpers.TextHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.*;

import static leaf.cosmere.constants.Constants.Strings.CONTAINED_POWERS_FOUND;

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

    default CompoundNBT getHemalurgicInfo(ItemStack stack)
    {
        return stack.getOrCreateChildTag("hemalurgy");
    }

    default void stealFromSpiritweb(ItemStack stack, Metals.MetalType spikeMetalType, LivingEntity entityKilled)
    {
        //todo
        boolean isPlayerEntity = (entityKilled instanceof PlayerEntity);
        boolean saveIdentity = false;
        //we should probably check a config to see if pvp real stealing of attributes is wanted.

        CompoundNBT hemalurgyInfo = getHemalurgicInfo(stack);


        //Steals non-manifestation based abilities. traits inherent to an entity?
        switch (spikeMetalType)
        {
            case IRON:
                //steals physical strength
                double strengthCurrent = CompoundNBTHelper.getDouble(hemalurgyInfo, spikeMetalType.name(), 0);
                //don't steal modified values, only base value
                //todo decide how much strength is reasonable to steal and how much goes to waste
                //currently will try 70%
                double strengthToAdd = entityKilled.getAttributeManager().getAttributeBaseValue(Attributes.ATTACK_DAMAGE) * 0.7D;
                CompoundNBTHelper.setDouble(hemalurgyInfo, spikeMetalType.name(), strengthCurrent + strengthToAdd);
                saveIdentity = true;
                break;
            case TIN:
                //Steals senses
                //todo figure out what that means in minecraft
                break;
            case COPPER:
                //Steals mental fortitude, memory, and intelligence
                //todo increase base xp gain?
                break;
            case ZINC:
                //Steals emotional fortitude
                //todo figure out what that means
                break;
            case ALUMINUM:
                //Removes all powers
                //... ooops?
                //maybe its an item you can equip on others that they then have to remove?
                break;
            case DURALUMIN:
                //Steals Connection/Identity
                break;
            case CHROMIUM:
                //Might steal destiny
                //so we could add some permanent luck?
                break;
            case NICROSIL:
                //Steals Investiture
                //todo figure out what that means
                //probably in the breaths/stormlight
                break;
        }

        List<AManifestation> manifestationsFound = new ArrayList<>();
        SpiritwebCapability.get(entityKilled).ifPresent(cap ->
        {
            //only grab innate manifestations, not ones added by hemalurgy
            manifestationsFound.addAll(cap.getAvailableManifestations(true));

        });

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
                    saveIdentity = tryStealScadrialManifestation(hemalurgyInfo, manifestationsFound, whiteList, Manifestations.ManifestationTypes.ALLOMANCY);
                    break;
                //steals feruchemical abilities
                case PEWTER:
                case BRASS:
                case BENDALLOY:
                case GOLD:
                    saveIdentity = tryStealScadrialManifestation(hemalurgyInfo, manifestationsFound, whiteList, Manifestations.ManifestationTypes.FERUCHEMY);
                    break;
                //The god metals don't follow the 'normal' rules.
                //Todo decide if they can steal powers from other investiture types or just scadrial related
                case ATIUM:
                    //Steals any one power
                    //todo decide if we just pick a random power
                    whiteList.addAll(Arrays.asList(Metals.MetalType.values()));
                    //then try steal it
                    //todo decide if prefer allomancy over feruchemy?
                    if (!tryStealScadrialManifestation(hemalurgyInfo, manifestationsFound, whiteList, Manifestations.ManifestationTypes.ALLOMANCY))
                    {
                        saveIdentity = tryStealScadrialManifestation(hemalurgyInfo, manifestationsFound, whiteList, Manifestations.ManifestationTypes.FERUCHEMY);
                    }
                    else
                    {
                        saveIdentity = true;
                    }
                    break;
                case LERASIUM:
                    for (AManifestation manifestation : manifestationsFound)
                    {
                        CompoundNBTHelper.setBoolean(hemalurgyInfo, manifestation.getRegistryName().getPath(), true);
                    }
                    saveIdentity = true;
                    break;
            }
        }

        if (saveIdentity)
        {
            setHemalurgicIdentity(stack, entityKilled.getUniqueID());
            CompoundNBTHelper.setBoolean(hemalurgyInfo, "hasHemalurgicPower", true);
        }
    }

    default boolean tryStealScadrialManifestation(
            CompoundNBT hemalurgyInfo,
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
                CompoundNBTHelper.setBoolean(hemalurgyInfo, manifestationsFound.get(i).getRegistryName().getPath(), true);
                CompoundNBTHelper.setBoolean(hemalurgyInfo, "hasHemalurgicPower", true);
                return true;
            }
        }

        return false;
    }

    default Multimap<Attribute, AttributeModifier> getHemalurgicAttributes(Multimap<Attribute, AttributeModifier> attributeModifiers, ItemStack stack, Metals.MetalType metalType)
    {
        CompoundNBT hemalurgyInfo = getHemalurgicInfo(stack);
        UUID hemalurgicIdentity = getHemalurgicIdentity(stack);

        if (hemalurgicIdentity == null)
        {
            return attributeModifiers;
        }

        switch (metalType)
        {
            case IRON:

                attributeModifiers.put(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                hemalurgicIdentity,
                                "Hemalurgic " + metalType.name(),
                                (double) CompoundNBTHelper.getDouble(
                                        hemalurgyInfo,
                                        metalType.name(),
                                        0),
                                AttributeModifier.Operation.ADDITION));

                break;
            case TIN:
                //Steals senses
                //todo figure out what that means in minecraft
                break;
            case ZINC:
                //Steals emotional fortitude
                //todo figure out what that means
                break;
            case COPPER:
                //Steals mental fortitude, memory, and intelligence
                //todo increase base xp gain?
                break;
            case CHROMIUM:
                //Might steal destiny
                //so we could add some permanent luck?
                break;
            case NICROSIL:
                //Steals Investiture
                //todo figure out what that means
                break;
        }


        for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
        {
            String path = manifestation.getRegistryName().getPath();

            if (CompoundNBTHelper.getBoolean(hemalurgyInfo, path, false))
            {
                if (!AttributesRegistry.MANIFESTATION_STRENGTH_ATTRIBUTES.containsKey(path))
                {
                    continue;
                }

                attributeModifiers.put(
                        AttributesRegistry.MANIFESTATION_STRENGTH_ATTRIBUTES.get(path).get(),
                        new AttributeModifier(
                                hemalurgicIdentity,
                                String.format("Hemalurgic-%s: %s", path, hemalurgicIdentity.toString()),
                                6,
                                AttributeModifier.Operation.ADDITION));
            }
        }


        return attributeModifiers;
    }


    default void addInvestitureInformation(ItemStack stack, List<ITextComponent> tooltip)
    {
        if (!hemalurgicIdentityExists(stack))
        {
            return;
        }

        tooltip.add(TextHelper.createTranslatedText(CONTAINED_POWERS_FOUND));
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
        return CompoundNBTHelper.getBoolean(getHemalurgicInfo(stack), manifestation.getRegistryName().getPath(), false);
    }

}

