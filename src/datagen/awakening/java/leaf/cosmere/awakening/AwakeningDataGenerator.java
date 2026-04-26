/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.awakening;

import leaf.cosmere.awakening.common.Awakening;
import leaf.cosmere.awakening.loottables.AwakeningLootTableGen;
import leaf.cosmere.awakening.patchouli.AwakeningPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;

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
		generator.addProvider(true, new AwakeningLootTableGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new AwakeningItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new AwakeningRecipeGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new AwakeningPatchouliGen(packOutput));
	}

}
