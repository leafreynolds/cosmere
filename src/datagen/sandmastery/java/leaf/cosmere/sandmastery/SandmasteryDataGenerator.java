/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery;

import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.items.SandmasteryItemModelsGen;
import leaf.cosmere.sandmastery.items.SandmasteryTagsProvider;
import leaf.cosmere.sandmastery.loottables.SandmasteryLootTableGen;
import leaf.cosmere.sandmastery.patchouli.SandmasteryPatchouliGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.concurrent.CompletableFuture;


@EventBusSubscriber(modid = Sandmastery.MODID)
public class SandmasteryDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new SandmasteryEngLangGen(packOutput));
		generator.addProvider(true, new SandmasteryItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new SandmasteryTagsProvider(packOutput, lookupProvider, existingFileHelper));
		generator.addProvider(true, new SandmasteryRecipeGen(packOutput, lookupProvider));
		generator.addProvider(true, new SandmasteryLootTableGen(packOutput, lookupProvider));
		generator.addProvider(true, new SandmasteryPatchouliGen(packOutput));
	}

}