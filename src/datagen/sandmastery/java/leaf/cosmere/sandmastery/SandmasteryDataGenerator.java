/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery;

import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.items.SandmasteryItemModelsGen;
import leaf.cosmere.sandmastery.items.SandmasteryTagsProvider;
import leaf.cosmere.sandmastery.loottables.SandmasteryLootTableGen;
import leaf.cosmere.sandmastery.patchouli.SandmasteryPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
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
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new SandmasteryEngLangGen(packOutput));
		generator.addProvider(true, new SandmasteryItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new SandmasteryTagsProvider(packOutput, event.getLookupProvider(), existingFileHelper));
		generator.addProvider(true, new SandmasteryRecipeGen(packOutput, existingFileHelper));
		generator.addProvider(true, new SandmasteryLootTableGen(packOutput));
		generator.addProvider(true, new SandmasteryPatchouliGen(packOutput));
	}

}