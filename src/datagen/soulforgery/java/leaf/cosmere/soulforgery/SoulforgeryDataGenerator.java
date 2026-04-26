/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.soulforgery;

import leaf.cosmere.soulforgery.common.Soulforgery;
import leaf.cosmere.soulforgery.loottables.SoulforgeryLootTableGen;
import leaf.cosmere.soulforgery.patchouli.SoulforgeryPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Soulforgery.MODID, bus = Bus.MOD)
public class SoulforgeryDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new SoulforgeryEngLangGen(packOutput));
		generator.addProvider(true, new SoulforgeryTagProvider(packOutput, event.getLookupProvider(), existingFileHelper));
		generator.addProvider(true, new SoulforgeryLootTableGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new SoulforgeryItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new SoulforgeryRecipeGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new SoulforgeryPatchouliGen(packOutput));
	}

}
