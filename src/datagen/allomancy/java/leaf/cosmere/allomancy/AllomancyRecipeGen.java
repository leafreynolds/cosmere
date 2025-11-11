/*
 * File updated ~ 4 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.allomancy;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.allomancy.common.registries.AllomancyRecipes;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class AllomancyRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public AllomancyRecipeGen(PackOutput output, ExistingFileHelper existingFileHelper)
	{
		super(output, existingFileHelper, Allomancy.MODID);
	}

	@Override
	protected void addRecipes(Consumer<FinishedRecipe> consumer)
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AllomancyItems.METAL_VIAL.get()).define('X', Items.IRON_NUGGET).define('Y', Items.GLASS).pattern("X").pattern("Y").unlockedBy("has_material", has(Items.GLASS)).save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, AllomancyItems.COIN_POUCH.get(), 1)
				.unlockedBy("has_item", has(Tags.Items.LEATHER))
				.requires(Tags.Items.LEATHER)
				.requires(Tags.Items.STRING)
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AllomancyItems.MISTCLOAK.get())
				.unlockedBy("has_item", has(Items.BLACK_WOOL))
				.define('X', Items.BLACK_WOOL)
				.pattern("X X")
				.pattern("XXX")
				.pattern("XXX").save(consumer);

		SpecialRecipeBuilder
				.special(AllomancyRecipes.VIAL_MIX.get())
				.save(consumer, Allomancy.rl("vial_mixing").toString());

	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return Allomancy.rl(path);
	}
}
