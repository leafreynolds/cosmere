/*
 * File updated ~ 5 - 6 - 2024 ~ Leaf
 */

package leaf.cosmere.soulforgery;

import leaf.cosmere.soulforgery.advancements.SoulforgeryAdvancementGen;
import leaf.cosmere.soulforgery.common.Soulforgery;
import leaf.cosmere.soulforgery.loottables.SoulforgeryLootTableGen;
import leaf.cosmere.soulforgery.patchouli.SoulforgeryPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Soulforgery.MODID, bus = Bus.MOD)
public class SoulforgeryDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new SoulforgeryEngLangGen(output));
		generator.addProvider(true, new SoulforgeryTagProvider(generator, existingFileHelper));
		generator.addProvider(true, new SoulforgeryLootTableGen(generator));
		generator.addProvider(true, new SoulforgeryItemModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new SoulforgeryRecipeGen(output, existingFileHelper));
		generator.addProvider(true, new SoulforgeryPatchouliGen(generator));
		generator.addProvider(true, new SoulforgeryAdvancementGen(generator));
	}

}