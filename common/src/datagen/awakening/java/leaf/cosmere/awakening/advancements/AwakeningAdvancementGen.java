/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.awakening.advancements;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import leaf.cosmere.api.CosmereAPI;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class AwakeningAdvancementGen implements DataProvider
{
	private final DataGenerator generator;
	private final List<Consumer<Consumer<Advancement>>> advancements = ImmutableList.of(
			new AwakeningAdvancements()
	);

	public AwakeningAdvancementGen(DataGenerator generatorIn)
	{
		this.generator = generatorIn;
	}

	/**
	 * Performs this provider's action.
	 */
	@Override
	public void run(@NotNull CachedOutput cache) throws IOException
	{
		Path path = this.generator.getOutputFolder();
		Set<ResourceLocation> set = Sets.newHashSet();
		Consumer<Advancement> consumer = (advancement) ->
		{
			if (!set.add(advancement.getId()))
			{
				throw new IllegalStateException("Duplicate advancement " + advancement.getId());
			}
			else
			{
				Path path1 = getPath(path, advancement);

				try
				{
					DataProvider.saveStable(cache, advancement.deconstruct().serializeToJson(), path1);
				}
				catch (IOException ioexception)
				{
					CosmereAPI.logger.error("Couldn't save advancement {}", path1, ioexception);
				}

			}
		};

		for (Consumer<Consumer<Advancement>> consumer1 : this.advancements)
		{
			consumer1.accept(consumer);
		}

	}

	private static Path getPath(Path pathIn, Advancement advancementIn)
	{
		return pathIn.resolve("data/awakening/advancements/" + advancementIn.getId().getPath() + ".json");
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	public @NotNull String getName()
	{
		return "Awakening Advancements";
	}
}
