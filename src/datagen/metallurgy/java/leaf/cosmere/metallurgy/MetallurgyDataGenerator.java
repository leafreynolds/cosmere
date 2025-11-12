package leaf.cosmere.metallurgy;

import leaf.cosmere.metallurgy.blocks.MetallurgyBlockModelsGen;
import leaf.cosmere.metallurgy.common.Metallurgy;
import leaf.cosmere.metallurgy.items.MetallurgyItemModelsGen;
import leaf.cosmere.metallurgy.items.MetallurgyTagsProvider;
import leaf.cosmere.metallurgy.loottables.MetallurgyLootTableGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Metallurgy.MODID, bus = Bus.MOD)
public class MetallurgyDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(true, new MetallurgyEngLangGen(packOutput));
        generator.addProvider(true, new MetallurgyItemModelsGen(packOutput, existingFileHelper));
        generator.addProvider(true, new MetallurgyBlockModelsGen(packOutput, existingFileHelper));
        generator.addProvider(true, new MetallurgyLootTableGen(packOutput));
        generator.addProvider(true,
                new MetallurgyTagsProvider(packOutput, event.getLookupProvider(), existingFileHelper));
        generator.addProvider(true, new MetallurgyRecipeGen(packOutput, existingFileHelper));
    }
}
