/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere;

import leaf.cosmere.biome.BiomeTagsProvider;
import leaf.cosmere.biome.CosmereDatapackRegistryProvider;
import leaf.cosmere.blocks.BlockModelsGen;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.BiomeRegistry;
import leaf.cosmere.items.ItemModelsGen;
import leaf.cosmere.loottables.LootTableGen;
import leaf.cosmere.patchouli.PatchouliGen;
import leaf.cosmere.tag.CosmereTagProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Cosmere.MODID, bus = Bus.MOD)
public class CosmereDataGenerator
{
	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.BIOME, BiomeRegistry::bootstrapBiomes);

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();


		generator.addProvider(true, new EngLangGen(packOutput));

		generator.addProvider(true, new ItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new BlockModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new LootTableGen(packOutput));
		generator.addProvider(true, new RecipeGen(packOutput, existingFileHelper));

		generator.addProvider(true, new PatchouliGen(packOutput));

		generator.addProvider(true, new CosmereDatapackRegistryProvider(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new BiomeTagsProvider(packOutput, event.getLookupProvider(), existingFileHelper));

		generator.addProvider(true, new CosmereTagProvider(packOutput, event.getLookupProvider(), existingFileHelper));

	}


}