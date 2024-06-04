/*
 * File updated ~ 5 - 6 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class SandmasteryRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public SandmasteryRecipeGen(PackOutput output, ExistingFileHelper existingFileHelper)
	{
		super(output, existingFileHelper);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return Sandmastery.rl(path);
	}

	@Override
	protected void addRecipes(Consumer<FinishedRecipe> consumer)
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SandmasteryItems.QIDO_ITEM.get())
				.define('H', Items.GOAT_HORN)
				.define('S', Tags.Items.STRING)
				.pattern("SSS")
				.pattern("S S")
				.pattern(" H ")
				.unlockedBy("has_material", has(Items.GOAT_HORN))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SandmasteryItems.JAR_ITEM.get())
				.define('G', Tags.Items.GLASS)
				.pattern("G G")
				.pattern("G G")
				.pattern(" G ")
				.unlockedBy("has_material", has(Tags.Items.GLASS))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SandmasteryItems.SAND_POUCH_ITEM.get())
				.define('L', Tags.Items.LEATHER)
				.define('B', Items.LEAD)
				.define('J', SandmasteryItems.SAND_JAR_ITEM)
				.pattern("BLL")
				.pattern("LJL")
				.pattern("LLL")
				.unlockedBy("has_material", has(SandmasteryItems.SAND_JAR_ITEM))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, SandmasteryBlocksRegistry.SAND_SPREADING_TUB_BLOCK.getBlock())
				.define('W', ItemTags.WOODEN_SLABS)
				.pattern("W W")
				.pattern("WWW")
				.unlockedBy("has_material", has(ItemTags.WOODEN_SLABS))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, SandmasteryItems.SAND_JAR_ITEM.get())
				.requires(SandmasteryBlocksRegistry.SAND_JAR_BLOCK.asItem())
				.unlockedBy("has_material", has(SandmasteryItems.SAND_JAR_ITEM))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, SandmasteryBlocksRegistry.SAND_JAR_BLOCK.asItem())
				.requires(SandmasteryItems.SAND_JAR_ITEM)
				.unlockedBy("has_material", has(SandmasteryBlocksRegistry.SAND_JAR_BLOCK.asItem()))
				.save(consumer);
	}
}
