/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.tools;

import leaf.cosmere.tools.common.CosmereTools;
import leaf.cosmere.tools.loottables.ToolsLootTableGen;
import leaf.cosmere.tools.patchouli.ToolsPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

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
		generator.addProvider(true, new ToolsLootTableGen(packOutput));
		generator.addProvider(true, new ToolsItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new ToolsRecipeGen(packOutput, existingFileHelper));
		generator.addProvider(true, new ToolsPatchouliGen(packOutput));
	}

}