/*
 * File updated ~ 7 - 8 - 2024 ~ Leaf
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
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new FeruchemyEngLangGen(output));
		generator.addProvider(true, new FeruchemyItemModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new FeruchemyTagProvider(generator, existingFileHelper));
		generator.addProvider(true, new FeruchemyRecipeGen(output, existingFileHelper));
		generator.addProvider(true, new FeruchemyPatchouliGen(generator));
	}

}