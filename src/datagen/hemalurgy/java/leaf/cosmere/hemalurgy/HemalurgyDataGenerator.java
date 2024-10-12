/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.hemalurgy;

import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.patchouli.HemalurgyPatchouliGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Hemalurgy.MODID, bus = Bus.MOD)
public class HemalurgyDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new HemalurgyEngLangGen(packOutput));
		generator.addProvider(true, new HemalurgyTagsProvider(packOutput, lookupProvider, existingFileHelper));
		generator.addProvider(true, new HemalurgyItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new HemalurgyRecipeGen(packOutput, existingFileHelper));
		generator.addProvider(true, new HemalurgyPatchouliGen(packOutput));
		generator.addProvider(event.includeServer(), new HemalurgyCuriosProvider(packOutput, existingFileHelper, lookupProvider));
	}

}