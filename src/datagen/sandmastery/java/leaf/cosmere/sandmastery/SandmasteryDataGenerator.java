/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery;

import leaf.cosmere.sandmastery.blocks.SandmasteryBlockTagsGen;
import leaf.cosmere.sandmastery.items.SandmasteryItemModelsGen;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.loottables.SandmasteryBlockLootTableGen;
import leaf.cosmere.sandmastery.loottables.SandmasteryLootTableGen;
import leaf.cosmere.sandmastery.patchouli.SandmasteryPatchouliGen;
import net.minecraft.data.DataGenerator;
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
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new SandmasteryEngLangGen(generator));
		generator.addProvider(true, new SandmasteryItemModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new SandmasteryBlockTagsGen(generator, existingFileHelper));
		generator.addProvider(true, new SandmasteryRecipeGen(generator));
		generator.addProvider(true, new SandmasteryLootTableGen(generator));
		generator.addProvider(true, new SandmasteryPatchouliGen(generator));
	}

}