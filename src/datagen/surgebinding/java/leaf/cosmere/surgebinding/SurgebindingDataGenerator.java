/*
 * File updated ~ 10 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding;

import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.loottables.SurgebindingLootTableGen;
import leaf.cosmere.surgebinding.patchouli.SurgebindingPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Surgebinding.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SurgebindingDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new SurgebindingEngLangGen(packOutput));

		generator.addProvider(true, new SurgebindingTagsProvider(packOutput, event.getLookupProvider(), existingFileHelper));

		generator.addProvider(true, new SurgebindingItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new SurgebindingBlockModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new SurgebindingLootTableGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new SurgebindingRecipeGen(packOutput, event.getLookupProvider()));

		generator.addProvider(true, new SurgebindingPatchouliGen(packOutput));
	}

}