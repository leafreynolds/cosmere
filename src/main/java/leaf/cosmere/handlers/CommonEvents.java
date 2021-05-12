/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.handlers;


import leaf.cosmere.Cosmere;
import leaf.cosmere.commands.CosmereCommand;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.LogHelper;
import leaf.cosmere.registry.FeatureRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents
{
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event)
    {
        CosmereCommand.register(event.getDispatcher());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoad(BiomeLoadingEvent event)
    {
        Biome.Category biomeCategory = event.getCategory();

        //whitelist only vanilla biomes.
        //todo may have compatibility issues with mining dimension type mods
        //don't really want to deprive people of easier access to ores if they want to play that way. something to look into.
        List<Biome.Category> whitelist =
                Arrays.stream(Biome.Category.values())
                        .filter(biomeType -> biomeType != Biome.Category.NONE && biomeType != Biome.Category.NETHER && biomeType != Biome.Category.THEEND)
                        .collect(Collectors.toList());


        if (whitelist.contains(biomeCategory))
        {
            for (Metals.MetalType metalType : Metals.MetalType.values())
            {
                if (metalType.hasOre())
                {
                    event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, FeatureRegistry.ConfiguredFeatures.ORE_FEATURES.get(metalType));
                    LogHelper.debug(String.format("Added %s to: %s", metalType.name().toLowerCase(Locale.ROOT), event.getName()));
                }
            }

        }
    }
}