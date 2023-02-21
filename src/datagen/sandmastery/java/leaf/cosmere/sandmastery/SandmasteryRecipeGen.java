/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery;

import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class SandmasteryRecipeGen extends RecipeProvider implements IConditionBuilder
{
	public SandmasteryRecipeGen(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
	{
		ShapedRecipeBuilder.shaped(SandmasteryItems.QIDO_ITEM.get())
				.define('H', Items.GOAT_HORN)
				.define('S', Items.STRING)
				.pattern("SSS")
				.pattern("S S")
				.pattern(" H ")
				.unlockedBy("has_material", has(Items.GOAT_HORN))
				.save(consumer);

		ShapedRecipeBuilder.shaped(SandmasteryItems.JAR_ITEM.get())
				.define('G', Items.GLASS)
				.pattern("G G")
				.pattern("G G")
				.pattern(" G ")
				.unlockedBy("has_material", has(Items.GLASS))
				.save(consumer);

		ShapedRecipeBuilder.shaped(SandmasteryItems.SAND_POUCH_ITEM.get())
				.define('L', Items.LEATHER)
				.define('B', Items.LEAD)
				.define('J', SandmasteryItems.SAND_JAR_ITEM)
				.pattern("BLL")
				.pattern("LJL")
				.pattern("LLL")
				.unlockedBy("has_material", has(SandmasteryItems.SAND_JAR_ITEM))
				.save(consumer);

		ShapedRecipeBuilder.shaped(SandmasteryBlocksRegistry.SAND_SPREADING_TUB_BLOCK.getBlock())
				.define('W', Items.BIRCH_PLANKS)
				.pattern("W W")
				.pattern("WWW")
				.unlockedBy("has_material", has(Items.BIRCH_PLANKS))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(SandmasteryItems.SAND_JAR_ITEM.get())
				.requires(SandmasteryBlocksRegistry.SAND_JAR_BLOCK.asItem())
				.unlockedBy("has_material", has(SandmasteryItems.SAND_JAR_ITEM))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(SandmasteryBlocksRegistry.SAND_JAR_BLOCK.asItem())
				.requires(SandmasteryItems.SAND_JAR_ITEM)
				.unlockedBy("has_material", has(SandmasteryBlocksRegistry.SAND_JAR_BLOCK.asItem()))
				.save(consumer);
	}
}
