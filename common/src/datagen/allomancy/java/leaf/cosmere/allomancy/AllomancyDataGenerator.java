/*
 * File updated ~ 7 - 8 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy;

import leaf.cosmere.allomancy.advancements.AllomancyAdvancementGen;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.patchouli.AllomancyPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Allomancy.MODID, bus = Bus.MOD)
public class AllomancyDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();


		generator.addProvider(true, new AllomancyEngLangGen(generator));


		generator.addProvider(true, new AllomancyItemModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new AllomancyRecipeGen(generator));

		generator.addProvider(true, new AllomancyPatchouliGen(generator));
		generator.addProvider(true, new AllomancyAdvancementGen(generator));
		generator.addProvider(true, new AllomancyTagProvider(generator, existingFileHelper));
	}

}