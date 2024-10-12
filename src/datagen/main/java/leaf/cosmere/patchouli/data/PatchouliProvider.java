/*
 * File updated ~ 9 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.patchouli.data;

import com.google.common.collect.Sets;
import leaf.cosmere.api.text.StringHelper;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
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
	private final PackOutput packOutput;
	private final String modid;


	protected final List<BookStuff.Category> categories = new ArrayList<>();
	protected final List<BookStuff.Entry> entries = new ArrayList<>();

	public PatchouliProvider(PackOutput packOutput, String modid)
	{
		this.packOutput = packOutput;
		this.modid = modid;
	}

	/**
	 * Performs this provider's action.
	 *
	 * @return
	 */
	public CompletableFuture<?> run(@NotNull CachedOutput cache)
	{
		List<CompletableFuture<?>> futures = new ArrayList<>();

		Path path = this.packOutput.getOutputFolder();
		Set<String> entryIDs = Sets.newHashSet();

		Consumer<BookStuff.Entry> entryConsumer = getEntryConsumer(cache, path, entryIDs, futures);
		Consumer<BookStuff.Category> categoryConsumer = getCategoryConsumer(cache, path, entryIDs, futures);

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

		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	protected abstract void collectInfoForBook();

	private Consumer<BookStuff.Category> getCategoryConsumer(@NotNull CachedOutput cache, Path path, Set<String> categoryIDs, List<CompletableFuture<?>> futures)
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
				futures.add(DataProvider.saveStable(cache, category.serialize(), path1));
			}
		};
	}

	private Consumer<BookStuff.Entry> getEntryConsumer(@NotNull CachedOutput cache, Path path, Set<String> entryIDs, List<CompletableFuture<?>> futures)
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
				futures.add(DataProvider.saveStable(cache, entry.serialize(modid), path1));
			}
		};
	}

	private Path getCategoryPath(Path pathIn, BookStuff.Category category)
	{
		return pathIn.resolve(
				String.format(
						"assets/%s/patchouli_books/%s/en_us/categories/%s.json",
						modid,
						GUIDE_NAME,
						StringHelper.fixPath(category.name)));
	}

	private Path getEntryPath(Path pathIn, BookStuff.Entry entry)
	{
		return pathIn.resolve(
				String.format(
						"assets/%s/patchouli_books/%s/en_us/entries/%s/%s.json",
						modid,
						GUIDE_NAME,
						StringHelper.fixPath(entry.category.name),
						StringHelper.fixPath(entry.name)));
	}

}
