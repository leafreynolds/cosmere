/*
 * File updated ~ 5 - 6 - 2024 ~ Leaf
 */

package leaf.cosmere.tools;

import leaf.cosmere.tools.advancements.ToolsAdvancementGen;
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
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new ToolsEngLangGen(output));
		generator.addProvider(true, new ToolsTagProvider(generator, existingFileHelper));
		generator.addProvider(true, new ToolsLootTableGen(generator));
		generator.addProvider(true, new ToolsItemModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new ToolsRecipeGen(output, existingFileHelper));
		generator.addProvider(true, new ToolsPatchouliGen(generator));
		generator.addProvider(true, new ToolsAdvancementGen(generator));
	}

}