/*
 * File updated ~ 5 - 6 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery;

import leaf.cosmere.sandmastery.blocks.SandmasteryBlockTagsGen;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.items.SandmasteryItemModelsGen;
import leaf.cosmere.sandmastery.items.SandmasteryItemTagsGen;
import leaf.cosmere.sandmastery.loottables.SandmasteryLootTableGen;
import leaf.cosmere.sandmastery.patchouli.SandmasteryPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Sandmastery.MODID, bus = Bus.MOD)
public class SandmasteryDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new SandmasteryEngLangGen(output));
		generator.addProvider(true, new SandmasteryItemModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new SandmasteryBlockTagsGen(generator, existingFileHelper));
		generator.addProvider(true, new SandmasteryItemTagsGen(generator, new BlockTagsProvider(generator, Sandmastery.MODID, existingFileHelper), existingFileHelper));
		generator.addProvider(true, new SandmasteryRecipeGen(output, existingFileHelper));
		generator.addProvider(true, new SandmasteryLootTableGen(generator));
		generator.addProvider(true, new SandmasteryPatchouliGen(generator));
	}

}