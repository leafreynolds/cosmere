/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.hemalurgy;

import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.patchouli.HemalurgyPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Hemalurgy.MODID, bus = Bus.MOD)
public class HemalurgyDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new HemalurgyEngLangGen(output));
		generator.addProvider(true, new HemalurgyTagsProvider(output, event.getLookupProvider(), existingFileHelper));
		generator.addProvider(true, new HemalurgyItemModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new HemalurgyRecipeGen(output, existingFileHelper));
		generator.addProvider(true, new HemalurgyPatchouliGen(generator));
	}

}