/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy;

import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.patchouli.FeruchemyPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Feruchemy.MODID, bus = Bus.MOD)
public class FeruchemyDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new FeruchemyEngLangGen(packOutput));
		generator.addProvider(true, new FeruchemyItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new FeruchemyTagProvider(packOutput, event.getLookupProvider(), existingFileHelper));
		generator.addProvider(true, new FeruchemyRecipeGen(packOutput, existingFileHelper));
		generator.addProvider(true, new FeruchemyPatchouliGen(packOutput));
	}

}