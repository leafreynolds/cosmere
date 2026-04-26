/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.sandmastery;

import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.items.SandmasteryItemModelsGen;
import leaf.cosmere.sandmastery.items.SandmasteryTagsProvider;
import leaf.cosmere.sandmastery.loottables.SandmasteryLootTableGen;
import leaf.cosmere.sandmastery.patchouli.SandmasteryPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Sandmastery.MODID, bus = EventBusSubscriber.Bus.MOD)
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
		generator.addProvider(true, new SandmasteryRecipeGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new SandmasteryLootTableGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new SandmasteryPatchouliGen(packOutput));
	}

}
