/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aondor;

import leaf.cosmere.aondor.advancements.AonDorAdvancementGen;
import leaf.cosmere.aondor.common.AonDor;
import leaf.cosmere.aondor.loottables.AonDorLootTableGen;
import leaf.cosmere.aondor.patchouli.AonDorPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = AonDor.MODID, bus = Bus.MOD)
public class AonDorDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new AonDorEngLangGen(generator));
		generator.addProvider(true, new AonDorTagProvider(generator, existingFileHelper));
		generator.addProvider(true, new AonDorLootTableGen(generator));
		generator.addProvider(true, new AonDorItemModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new AonDorRecipeGen(generator));
		generator.addProvider(true, new AonDorPatchouliGen(generator));
		generator.addProvider(true, new AonDorAdvancementGen(generator));
	}

}