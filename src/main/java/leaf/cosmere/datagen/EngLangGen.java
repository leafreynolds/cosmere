/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

import static leaf.cosmere.constants.Constants.Strings.*;

public class EngLangGen extends LanguageProvider
{
    private final DataGenerator generator;

    public EngLangGen(DataGenerator gen)
    {
        super(gen, Cosmere.MODID, "en_us");
        this.generator = gen;
    }

    @Override
    protected void addTranslations()
    {

        //Items and Blocks
        for (Item item : ForgeRegistries.ITEMS.getValues())
        {
            if (item.getRegistryName().getNamespace().contentEquals(Cosmere.MODID))
            {
                String localisedString = fixCapitalisation(item.getRegistryName().getPath());

                switch (localisedString)
                {
                    case "Guide":
                        localisedString = "Ars Arcanum";
                        break;
                }

                add(item.getTranslationKey(), localisedString);
            }
        }

        //Entities
        for (EntityType<?> type : ForgeRegistries.ENTITIES)
        {
            if (type.getRegistryName().getNamespace().equals(Cosmere.MODID))
            {
                add(type.getTranslationKey(), fixCapitalisation(type.getRegistryName().getPath()));
            }
        }

        //innate

        for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
        {
            //power type
            String key = manifestation.translation().getKey();
            String path = manifestation.getRegistryName().getPath();

            //Name
            add(key, fixCapitalisation(path));

            //description
            //can't auto generate the descriptions ya dingleberry
            String description;

            switch (manifestation.getManifestationType())
            {
                case ALLOMANCY:
                    description = "Users can burn " + Metals.MetalType.valueOf(manifestation.getPowerID()).get().toString();
                    break;
                case FERUCHEMY:
                    description = "Users can tap " + Metals.MetalType.valueOf(manifestation.getPowerID()).get().toString();
                    break;
                case RADIANT:
                case ELANTRIAN:
                case AWAKENER:
                default:
                case NONE:
                    description = "No Special Powers";
                    break;
            }


            add(manifestation.description().getKey(), description);
        }

        //ItemGroups/Tabs
        add("itemGroup." + CosmereItemGroups.ITEMS.getPath(), "Cosmere Items");
        add("itemGroup." + CosmereItemGroups.METALMINDS.getPath(), "Cosmere Metalminds");
        add("itemGroup." + CosmereItemGroups.BLOCKS.getPath(), "Cosmere Blocks");

        //Damage Sources

        //Containers

        //effects
        for (RegistryObject<Effect> effect : EffectsRegistry.EFFECTS.getEntries())
        {
            add(effect.get().getName(), fixCapitalisation(effect.get().getRegistryName().getPath()));

        }

        //Sound Schemes

        //Configs

        //Commands


        //Tooltips
        add("tooltip.item.info.shift", "\u00A77Hold \u00A78[\u00A7eShift\u00A78]");
        add("tooltip.item.info.shift_control", "\u00A77Hold \u00A78[\u00A7eShift\u00A78] \u00A77and \u00A78[\u00A7eControl\u00A78]");
        add("tooltip.item.info.control", "\u00A77Hold \u00A78[\u00A7eControl\u00A78]");

        //GUI elements
        add("gui.cosmere.previous", "> Previous");
        add("gui.cosmere.next", "> Next");
        add("gui.cosmere.select", "> Select");
        add("gui.cosmere.confirm", "> Confirm");
        add("gui.cosmere.save", "> Save");
        add("gui.cosmere.cancel", "> Cancel");
        add("gui.cosmere.button.back", "Back");


        add("gui.cosmere.other.inactive", "Inactive");
        add("gui.cosmere.other.active", "Active");
        add("gui.cosmere.mode.increase", "Increase");
        add("gui.cosmere.mode.decrease", "Decrease");


        //ARS Arcanum
        add("cosmere.landing", "The Cosmere is filled with many fantastical things. I have left my findings written within this book.");

        //KeyBindings
        add(KEYS_CATEGORY, "Cosmere");
        add(KEY_MANIFESTATION_TOGGLE, "Toggle Current Powers");
        add(KEY_MANIFESTATION_MODE_NEXT, "Mode Next");
        add(KEY_MANIFESTATION_MODE_PREVIOUS, "Mode Previous");

        add(KEY_ALLOMANCY_PUSH, "Push");
        add(KEY_ALLOMANCY_PULL, "Pull");


        //powers
        add(POWER_INVALID, "Invalid power");
        add(POWER_SET_SUCCESS, "Successfully set power to: %s");
        add(POWER_SET_FAIL, "Failed to update power");
        add(POWER_MODE_SET, "Mode set to: %s");
        add(POWER_ACTIVE, "Power now active: %s");
        add(POWER_INACTIVE, "Power now inactive: %s");

        add(POWERS_FOUND, "Powers found for: %s \n");
        add(CONTAINED_POWERS_FOUND, "Contained Powers:");


    }

    public Path getSoundPath(Path path, String modid)
    {
        return path.resolve("data/" + modid + "/sounds/" + "sounds.json");
    }

    public String fixCapitalisation(String text)
    {
        String original = text.trim().replace("    ", "").replace("_", " ").replace("/", ".");
        String output = Arrays.stream(original.split("\\s+")).map(t -> t.substring(0, 1).toUpperCase() + t.substring(1)).collect(Collectors.joining(" "));
        return output;
    }

    public String getTranslationKey(SoundEvent sound)
    {
        String subtitleTranslationKey = "";
        if (subtitleTranslationKey.isEmpty() || subtitleTranslationKey == null)
        {
            subtitleTranslationKey = Util.makeTranslationKey("subtitle", sound.getRegistryName());
        }
        return subtitleTranslationKey;
    }


}
