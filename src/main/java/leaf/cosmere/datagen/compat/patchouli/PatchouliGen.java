/*
 * File created ~ 13 - 6 - 2022 ~ Leaf
 */

package leaf.cosmere.datagen.compat.patchouli;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import leaf.cosmere.datagen.compat.patchouli.categories.PatchouliAllomancy;
import leaf.cosmere.datagen.compat.patchouli.categories.PatchouliFeruchemy;
import leaf.cosmere.datagen.compat.patchouli.categories.PatchouliHemalurgy;
import leaf.cosmere.datagen.compat.patchouli.categories.PatchouliBasics;
import leaf.cosmere.utils.helpers.LogHelper;
import leaf.cosmere.utils.helpers.StringHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

//
//  In-Game Documentation generator
//
public class PatchouliGen implements DataProvider
{
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final String GUIDE_NAME = "guide";
	private final DataGenerator generator;


	private final List<BookStuff.Category> categories = new ArrayList<>();
	private final List<BookStuff.Entry> entries = new ArrayList<>();

	public PatchouliGen(DataGenerator generatorIn)
	{
		this.generator = generatorIn;
	}

	/**
	 * Performs this provider's action.
	 */
	public void run(HashCache cache) throws IOException
	{
		Path path = this.generator.getOutputFolder();
		Set<String> entryIDs = Sets.newHashSet();

		Consumer<BookStuff.Entry> entryConsumer = getEntryConsumer(cache, path, entryIDs);
		Consumer<BookStuff.Category> categoryConsumer = getCategoryConsumer(cache, path, entryIDs);

		//adds to our categories and entries fields.
		collectInfoForBook();

		for (BookStuff.Category categoryToConsume : this.categories)
		{
			categoryConsumer.accept(categoryToConsume);
		}

		for (BookStuff.Entry entryToConsume : this.entries)
		{
			entryConsumer.accept(entryToConsume);
		}

	}

	private void collectInfoForBook()
	{
		//dynamically figure out all the things we wanna generate categories/entries for?
		//------------------------------------------//
		//              Categories                  //
		//------------------------------------------//
		//  - Basics                                //
		//  - Manifestations (parent category?)     //
		//  - Feruchemy                             //
		//  - Allomancy                             //
		//  - Hemalurgy                             //
		//  - Curios                                //
		//  - Machines ?                            //
		//  - Challenges ?                          //
		//  -                                       //
		//  -                                       //
		//  -                                       //
		//  -                                       //
		//  -                                       //
		//------------------------------------------//

		PatchouliBasics.collect(this.categories, this.entries);
		PatchouliAllomancy.collect(this.categories, this.entries);
		PatchouliFeruchemy.collect(this.categories, this.entries);
		PatchouliHemalurgy.collect(this.categories, this.entries);


	}

	private Consumer<BookStuff.Category> getCategoryConsumer(HashCache cache, Path path, Set<String> categoryIDs)
	{
		return category ->
		{
			if (!categoryIDs.add(category.name))
			{
				throw new IllegalStateException("Duplicate page " + category.name);
			}
			else
			{
				Path path1 = getCategoryPath(path, category);

				try
				{
					DataProvider.save(GSON, cache, category.serialize(), path1);
				}
				catch (IOException ioexception)
				{
					LogHelper.LOGGER.error("Couldn't save page {}", path1, ioexception);
				}

			}
		};
	}

	private Consumer<BookStuff.Entry> getEntryConsumer(HashCache cache, Path path, Set<String> entryIDs)
	{
		return entry ->
		{
			if (!entryIDs.add(entry.name))
			{
				throw new IllegalStateException("Duplicate page " + entry.name);
			}
			else
			{
				Path path1 = getEntryPath(path, entry);

				try
				{
					DataProvider.save(GSON, cache, entry.serialize(), path1);
				}
				catch (IOException ioexception)
				{
					LogHelper.LOGGER.error("Couldn't save page {}", path1, ioexception);
				}

			}
		};
	}

	private static Path getCategoryPath(Path pathIn, BookStuff.Category category)
	{
		return pathIn.resolve(
				String.format(
						"data/cosmere/patchouli_books/%s/en_us/categories/%s.json",
						GUIDE_NAME,
						StringHelper.fixPath(category.name)));
	}

	private static Path getEntryPath(Path pathIn, BookStuff.Entry entry)
	{
		return pathIn.resolve(
				String.format(
						"data/cosmere/patchouli_books/%s/en_us/entries/%s/%s.json",
						GUIDE_NAME,
						StringHelper.fixPath(entry.category.name),
						StringHelper.fixPath(entry.name)));
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	public String getName()
	{
		return "PatchouliGeneration";
	}


}



