/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.example;

import leaf.cosmere.example.common.Example;
import leaf.cosmere.example.loottables.ExampleLootTableGen;
import leaf.cosmere.example.patchouli.ExamplePatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Example.MODID, bus = Bus.MOD)
public class ExampleDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new ExampleEngLangGen(packOutput));
		generator.addProvider(true, new ExampleTagProvider(packOutput, event.getLookupProvider(), existingFileHelper));
		generator.addProvider(true, new ExampleLootTableGen(packOutput));
		generator.addProvider(true, new ExampleItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new ExampleRecipeGen(packOutput, existingFileHelper));
		generator.addProvider(true, new ExamplePatchouliGen(packOutput));
	}

}