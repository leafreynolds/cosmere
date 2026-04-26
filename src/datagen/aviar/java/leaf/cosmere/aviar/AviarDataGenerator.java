/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.aviar;

import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.aviar.items.AviarItemModelsGen;
import leaf.cosmere.aviar.loottables.AviarLootTableGen;
import leaf.cosmere.aviar.patchouli.AviarPatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Aviar.MODID, bus = Bus.MOD)
public class AviarDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new AviarEngLangGen(packOutput));
		generator.addProvider(true, new AviarTagProvider(packOutput, event.getLookupProvider(), existingFileHelper));
		generator.addProvider(true, new AviarLootTableGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new AviarItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new AviarRecipeGen(packOutput, event.getLookupProvider()));
		generator.addProvider(true, new AviarPatchouliGen(packOutput));
	}

}