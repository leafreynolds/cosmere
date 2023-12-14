/*
 * File updated ~ 17 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy;

import leaf.cosmere.hemalurgy.advancements.HemalurgyAdvancementGen;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.patchouli.HemalurgyPatchouliGen;
import net.minecraft.data.DataGenerator;
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
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new HemalurgyEngLangGen(generator));
		generator.addProvider(true, new HemalurgyItemTagsProvider(generator, existingFileHelper));
		generator.addProvider(true, new HemalurgyItemModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new HemalurgyRecipeGen(generator));
		generator.addProvider(true, new HemalurgyPatchouliGen(generator));
		generator.addProvider(true, new HemalurgyAdvancementGen(generator));
	}

}