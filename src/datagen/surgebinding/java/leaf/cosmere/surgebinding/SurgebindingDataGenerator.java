
/*
 * File updated ~ 10 - 1 - 2025 ~ Leaf
 * File updated ~ 12 - 7 - 2025 ~ Soar
 */

package leaf.cosmere.surgebinding;

import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBiomes;
import leaf.cosmere.surgebinding.loottables.SurgebindingLootTableGen;
import leaf.cosmere.surgebinding.patchouli.SurgebindingPatchouliGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Surgebinding.MODID, bus = Bus.MOD)
public class SurgebindingDataGenerator
{
	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.BIOME, SurgebindingBiomes::bootstrapBiomes);

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();


		generator.addProvider(true, new SurgebindingEngLangGen(packOutput));

		generator.addProvider(true, new SurgebindingTagsProvider(packOutput, event.getLookupProvider(), existingFileHelper));

		generator.addProvider(true, new SurgebindingItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new SurgebindingBlockModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new SurgebindingLootTableGen(packOutput));
		generator.addProvider(true, new SurgebindingRecipeGen(packOutput, existingFileHelper));

		generator.addProvider(true, new SurgebindingPatchouliGen(packOutput));
		generator.addProvider(true, new SurgebindingCuriosProvider(packOutput, existingFileHelper, lookupProvider));
	}

}
