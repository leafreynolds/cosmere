/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.tools;

import leaf.cosmere.tools.common.CosmereTools;
import leaf.cosmere.tools.loottables.ToolsLootTableGen;
import leaf.cosmere.tools.patchouli.ToolsPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = CosmereTools.MODID, bus = Bus.MOD)
public class ToolsDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new ToolsEngLangGen(packOutput));
		generator.addProvider(true, new ToolsTagProvider(packOutput, event.getLookupProvider(), existingFileHelper));
		generator.addProvider(true, new ToolsLootTableGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new ToolsItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new ToolsRecipeGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new ToolsPatchouliGen(packOutput));
	}

}