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
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new AviarEngLangGen(packOutput));
		generator.addProvider(true, new AviarTagProvider(packOutput, event.getLookupProvider(), existingFileHelper));
		generator.addProvider(true, new AviarLootTableGen(packOutput));
		generator.addProvider(true, new AviarItemModelsGen(packOutput, existingFileHelper));
		generator.addProvider(true, new AviarRecipeGen(packOutput, existingFileHelper));
		generator.addProvider(true, new AviarPatchouliGen(packOutput));
	}

}