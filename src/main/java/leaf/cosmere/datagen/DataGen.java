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
import leaf.cosmere.datagen.advancements.AdvancementGen;
import leaf.cosmere.datagen.biome.BiomeModifierGen;
import leaf.cosmere.datagen.blocks.BlockModelsGen;
import leaf.cosmere.datagen.blocks.BlockTagsGen;
import leaf.cosmere.datagen.items.ItemModelsGen;
import leaf.cosmere.datagen.items.ItemTagsGen;
import leaf.cosmere.datagen.language.EngLangGen;
import leaf.cosmere.datagen.loottables.LootTableGen;
import leaf.cosmere.datagen.patchouli.PatchouliGen;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Bus.MOD)
public class DataGen
{

	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	@SubscribeEvent
	public static void onDataGen(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(true, new EngLangGen(generator));

		BlockTagsGen blockTags = new BlockTagsGen(generator, existingFileHelper);
		generator.addProvider(true, blockTags);
		generator.addProvider(true, new ItemTagsGen(generator, blockTags, existingFileHelper));

		generator.addProvider(true, new ItemModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new BlockModelsGen(generator, existingFileHelper));
		generator.addProvider(true, new LootTableGen(generator));
		generator.addProvider(true, new RecipeGen(generator));

		generator.addProvider(true, new AdvancementGen(generator));

		generator.addProvider(true, new PatchouliGen(generator));

		generator.addProvider(true, new BiomeModifierGen(generator));

	}

}
