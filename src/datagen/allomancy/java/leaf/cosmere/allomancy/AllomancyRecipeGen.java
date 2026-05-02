/*
 * File updated ~ 4 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.allomancy;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.recipes.VialMixingRecipe;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class AllomancyRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public AllomancyRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries, Allomancy.MODID);
	}

	@Override
	protected void addRecipes(RecipeOutput output)
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AllomancyItems.METAL_VIAL.get()).define('X', Items.IRON_NUGGET).define('Y', Items.GLASS).pattern("X").pattern("Y").unlockedBy("has_material", has(Items.GLASS)).save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, AllomancyItems.COIN_POUCH.get(), 1)
				.unlockedBy("has_item", has(Items.LEATHER))
				.requires(Items.LEATHER)
				.requires(Items.STRING)
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AllomancyItems.MISTCLOAK.get())
				.unlockedBy("has_item", has(Items.BLACK_WOOL))
				.define('X', Items.BLACK_WOOL)
				.pattern("X X")
				.pattern("XXX")
				.pattern("XXX").save(output);

		SpecialRecipeBuilder
				.special(VialMixingRecipe::new)
				.save(output, Allomancy.rl("vial_mixing"));

	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return Allomancy.rl(path);
	}
}
