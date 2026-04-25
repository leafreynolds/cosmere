/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.allomancy;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.patchouli.AllomancyPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Allomancy.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AllomancyDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new AllomancyEngLangGen(packOutput));
		generator.addProvider(true, new AllomancyItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new AllomancyRecipeGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new AllomancyPatchouliGen(packOutput));
		generator.addProvider(true, new AllomancyTagProvider(packOutput, event.getLookupProvider(), existingFileHelper));

		generator.addProvider(true, new AllomancyDatapackRegistryProvider(packOutput, event.getLookupProvider()));
	}

}
