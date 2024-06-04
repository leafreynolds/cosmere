/*
 * File updated ~ 5 - 6 - 2024 ~ Leaf
 */

package leaf.cosmere.patchouli.data;

import com.google.common.collect.Sets;
import leaf.cosmere.api.text.StringHelper;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

//
//  In-Game Documentation generator
//
public abstract class PatchouliProvider implements DataProvider
{
	public static final String GUIDE_NAME = "guide";
	private final DataGenerator generator;
	private final String modid;


	protected final List<BookStuff.Category> categories = new ArrayList<>();
	protected final List<BookStuff.Entry> entries = new ArrayList<>();

	public PatchouliProvider(DataGenerator generatorIn, String modid)
	{
		this.generator = generatorIn;
		this.modid = modid;
	}

	/**
	 * Performs this provider's action.
	 *
	 * @return
	 */
	public CompletableFuture<?> run(@NotNull CachedOutput cache)
	{
		Path path = this.generator.getPackOutput().getOutputFolder();
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

		return null;
	}

	protected abstract void collectInfoForBook();

	private Consumer<BookStuff.Category> getCategoryConsumer(@NotNull CachedOutput cache, Path path, Set<String> categoryIDs)
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
				DataProvider.saveStable(cache, category.serialize(), path1);
			}
		};
	}

	private Consumer<BookStuff.Entry> getEntryConsumer(@NotNull CachedOutput cache, Path path, Set<String> entryIDs)
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

				DataProvider.saveStable(cache, entry.serialize(modid), path1);

			}
		};
	}

	private Path getCategoryPath(Path pathIn, BookStuff.Category category)
	{
		return pathIn.resolve(
				String.format(
						"data/%s/patchouli_books/%s/en_us/categories/%s.json",
						modid,
						GUIDE_NAME,
						StringHelper.fixPath(category.name)));
	}

	private Path getEntryPath(Path pathIn, BookStuff.Entry entry)
	{
		return pathIn.resolve(
				String.format(
						"data/%s/patchouli_books/%s/en_us/entries/%s/%s.json",
						modid,
						GUIDE_NAME,
						StringHelper.fixPath(entry.category.name),
						StringHelper.fixPath(entry.name)));
	}

}



