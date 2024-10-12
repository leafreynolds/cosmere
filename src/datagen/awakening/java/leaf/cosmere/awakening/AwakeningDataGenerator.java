/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.awakening;

import leaf.cosmere.awakening.common.Awakening;
import leaf.cosmere.awakening.loottables.AwakeningLootTableGen;
import leaf.cosmere.awakening.patchouli.AwakeningPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Awakening.MODID, bus = Bus.MOD)
public class AwakeningDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new AwakeningEngLangGen(packOutput));
		generator.addProvider(true, new AwakeningTagProvider(packOutput, event.getLookupProvider(), existingFileHelper));
		generator.addProvider(true, new AwakeningLootTableGen(packOutput));
		generator.addProvider(true, new AwakeningItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new AwakeningRecipeGen(packOutput, existingFileHelper));
		generator.addProvider(true, new AwakeningPatchouliGen(packOutput));
	}

}