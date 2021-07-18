/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.patchouli;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.*;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.datagen.patchouli.categories.PatchouliAllomancy;
import leaf.cosmere.datagen.patchouli.categories.PatchouliBasics;
import leaf.cosmere.datagen.patchouli.categories.PatchouliFeruchemy;
import leaf.cosmere.datagen.patchouli.categories.PatchouliHemalurgy;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.allomancy.AllomancyBase;
import leaf.cosmere.manifestation.feruchemy.FeruchemyBase;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

import net.minecraftforge.fml.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static leaf.cosmere.registry.ManifestationRegistry.*;

//
//  In-Game Documentation generator
//
public class PatchouliGen implements IDataProvider
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String GUIDE_NAME = "guide";
    private final DataGenerator generator;


    private List<BookStuff.Category> categories = new ArrayList<>();
    private List<BookStuff.Entry> entries = new ArrayList<>();

    public PatchouliGen(DataGenerator generatorIn)
    {
        this.generator = generatorIn;
    }

    /**
     * Performs this provider's action.
     */
    public void act(DirectoryCache cache) throws IOException
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

    private Consumer<BookStuff.Category> getCategoryConsumer(DirectoryCache cache, Path path, Set<String> categoryIDs)
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
                    IDataProvider.save(GSON, cache, category.serialize(), path1);
                } catch (IOException ioexception)
                {
                    LOGGER.error("Couldn't save page {}", path1, ioexception);
                }

            }
        };
    }

    private Consumer<BookStuff.Entry> getEntryConsumer(DirectoryCache cache, Path path, Set<String> entryIDs)
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
                    IDataProvider.save(GSON, cache, entry.serialize(), path1);
                } catch (IOException ioexception)
                {
                    LOGGER.error("Couldn't save page {}", path1, ioexception);
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
                        category.name));
    }

    private static Path getEntryPath(Path pathIn, BookStuff.Entry entry)
    {
        return pathIn.resolve(
                String.format(
                        "data/cosmere/patchouli_books/%s/en_us/entries/%s/%s.json",
                        GUIDE_NAME,
                        entry.category.name,
                        entry.name));
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    public String getName()
    {
        return "PatchouliGeneration";
    }


}



