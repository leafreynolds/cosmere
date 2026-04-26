/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.aondor;

import leaf.cosmere.aondor.common.AonDor;
import leaf.cosmere.aondor.loottables.AonDorLootTableGen;
import leaf.cosmere.aondor.patchouli.AonDorPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = AonDor.MODID, bus = Bus.MOD)
public class AonDorDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new AonDorEngLangGen(packOutput));
		generator.addProvider(true, new AonDorTagProvider(packOutput, event.getLookupProvider(), existingFileHelper));
		generator.addProvider(true, new AonDorLootTableGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new AonDorItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new AonDorRecipeGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new AonDorPatchouliGen(packOutput));
	}

}
