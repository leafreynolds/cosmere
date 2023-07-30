/*
 * File updated ~ 21 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere;

import leaf.cosmere.biome.BiomeModifierGen;
import leaf.cosmere.biome.BiomeTagsProvider;
import leaf.cosmere.blocks.BlockModelsGen;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.items.ItemModelsGen;
import leaf.cosmere.loottables.LootTableGen;
import leaf.cosmere.patchouli.PatchouliGen;
import leaf.cosmere.tag.CosmereTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Cosmere.MODID, bus = Bus.MOD)
public class CosmereDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();


		generator.addProvider(true, new EngLangGen(generator));

		generator.addProvider(true, new ItemModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new BlockModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new LootTableGen(generator));
		generator.addProvider(true, new RecipeGen(generator));

		generator.addProvider(true, new PatchouliGen(generator));

		generator.addProvider(true, new BiomeModifierGen(generator));
		generator.addProvider(true, new BiomeTagsProvider(generator, existingFileHelper));

		generator.addProvider(true, new CosmereTagProvider(generator, existingFileHelper));

	}

}