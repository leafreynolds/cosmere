/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar;

import leaf.cosmere.aviar.advancements.AviarAdvancementGen;
import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.aviar.items.AviarItemModelsGen;
import leaf.cosmere.aviar.loottables.AviarLootTableGen;
import leaf.cosmere.aviar.patchouli.AviarPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Aviar.MODID, bus = Bus.MOD)
public class AviarDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new AviarEngLangGen(generator));
		generator.addProvider(true, new AviarTagProvider(generator, existingFileHelper));
		generator.addProvider(true, new AviarLootTableGen(generator));
		generator.addProvider(true, new AviarItemModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new AviarRecipeGen(generator));
		generator.addProvider(true, new AviarPatchouliGen(generator));
		generator.addProvider(true, new AviarAdvancementGen(generator));
	}

}