
/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 * File updated ~ 12 - 7 - 2025 ~ Soar
 */

package leaf.cosmere.allomancy;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.patchouli.AllomancyPatchouliGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Allomancy.MODID, bus = Bus.MOD)
public class AllomancyDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new AllomancyEngLangGen(packOutput));
		generator.addProvider(true, new AllomancyItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new AllomancyRecipeGen(packOutput, existingFileHelper));
		generator.addProvider(true, new AllomancyPatchouliGen(packOutput));
		generator.addProvider(true, new AllomancyTagProvider(packOutput, event.getLookupProvider(), existingFileHelper));
		generator.addProvider(event.includeServer(), new AllomancyCuriosProvider(packOutput,existingFileHelper,lookupProvider));

		generator.addProvider(true, new AllomancyDatapackRegistryProvider(packOutput, event.getLookupProvider()));
	}

}
