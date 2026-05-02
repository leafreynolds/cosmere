/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.surgebinding;

import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.loottables.SurgebindingLootTableGen;
import leaf.cosmere.surgebinding.patchouli.SurgebindingPatchouliGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Surgebinding.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SurgebindingDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new SurgebindingEngLangGen(packOutput));

		generator.addProvider(true, new SurgebindingTagsProvider(packOutput, lookupProvider, existingFileHelper));

		generator.addProvider(true, new SurgebindingItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new SurgebindingBlockModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new SurgebindingLootTableGen(packOutput, lookupProvider));
		generator.addProvider(true, new SurgebindingRecipeGen(packOutput, lookupProvider));

		generator.addProvider(true, new SurgebindingPatchouliGen(packOutput));
		generator.addProvider(true, new SurgebindingWorldGenProvider(packOutput, lookupProvider));
		generator.addProvider(true, new SurgebindingCuriosProvider(packOutput, existingFileHelper, lookupProvider));
	}

}
