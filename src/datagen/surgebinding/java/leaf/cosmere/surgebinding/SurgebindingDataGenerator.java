/*
 * File updated ~ 5 - 6 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding;

import leaf.cosmere.surgebinding.biome.SurgebindingBiomeModifierGen;
import leaf.cosmere.surgebinding.biome.SurgebindingBiomeTagsProvider;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.loottables.SurgebindingLootTableGen;
import leaf.cosmere.surgebinding.patchouli.SurgebindingPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Surgebinding.MODID, bus = Bus.MOD)
public class SurgebindingDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();


		generator.addProvider(true, new SurgebindingEngLangGen(output));

		generator.addProvider(true, new SurgebindingTagsProvider(generator, existingFileHelper));

		generator.addProvider(true, new SurgebindingItemModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new SurgebindingBlockModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new SurgebindingLootTableGen(generator));
		generator.addProvider(true, new SurgebindingRecipeGen(output, existingFileHelper));

		generator.addProvider(true, new SurgebindingPatchouliGen(generator));

		generator.addProvider(true, new SurgebindingBiomeModifierGen(generator));
		generator.addProvider(true, new SurgebindingBiomeTagsProvider(generator, existingFileHelper));
	}

}