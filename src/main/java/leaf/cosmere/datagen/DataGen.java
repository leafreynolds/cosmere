/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special Thank you to ChampionAsh5357 from the forge project discord!
 * They provided a series of tutorials with examples of how to add new sections of data generation
 * Generating 20+ different metal related blocks, items, curios etc would have been a nightmare without it.
 */

package leaf.cosmere.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import leaf.cosmere.Cosmere;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Bus.MOD)
public class DataGen
{

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @SubscribeEvent
    public static void onDataGen(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(new EngLangGen(generator));

        BlockTagsGen blockTags = new BlockTagsGen(generator, existingFileHelper);
        generator.addProvider(blockTags);
        generator.addProvider(new ItemTagsGen(generator, blockTags, existingFileHelper));

        if (!event.includeClient())
        {
            return;
        }

        generator.addProvider(new ItemModelsGen(generator, existingFileHelper));
        generator.addProvider(new BlockModelsGen(generator, existingFileHelper));
        generator.addProvider(new LootTableGen(generator));
        generator.addProvider(new RecipeGen(generator));

		/* todo: more automated json generation
		generator.addProvider(new AdvancementGen(generator));
		*/
    }

}
