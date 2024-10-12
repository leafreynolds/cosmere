/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.aondor;

import leaf.cosmere.aondor.common.AonDor;
import leaf.cosmere.aondor.loottables.AonDorLootTableGen;
import leaf.cosmere.aondor.patchouli.AonDorPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
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
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new AonDorEngLangGen(packOutput));
		generator.addProvider(true, new AonDorTagProvider(packOutput, event.getLookupProvider(), existingFileHelper));
		generator.addProvider(true, new AonDorLootTableGen(packOutput));
		generator.addProvider(true, new AonDorItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new AonDorRecipeGen(packOutput, existingFileHelper));
		generator.addProvider(true, new AonDorPatchouliGen(packOutput));
	}

}