/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.advancements;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class AdvancementGen implements IDataProvider
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final DataGenerator generator;
    private final List<Consumer<Consumer<Advancement>>> advancements = ImmutableList.of(
            new CoreAdvancements(),
            new AllomancyAdvancements(),
            new FeruchemyAdvancements(),
            new HemalurgyAdvancements()
    );

    public AdvancementGen(DataGenerator generatorIn)
    {
        this.generator = generatorIn;
    }

    /**
     * Performs this provider's action.
     */
    public void run(DirectoryCache cache) throws IOException
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
                    IDataProvider.save(GSON, cache, advancement.deconstruct().serializeToJson(), path1);
                } catch (IOException ioexception)
                {
                    LOGGER.error("Couldn't save advancement {}", path1, ioexception);
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
        return pathIn.resolve("data/cosmere/advancements/" + advancementIn.getId().getPath() + ".json");
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    public String getName()
    {
        return "Cosmere Advancements";
    }
}
