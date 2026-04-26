/*
 * File updated ~ 4 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.example;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.example.common.Example;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ExampleRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public ExampleRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(output, lookupProvider, Example.MODID);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return Example.rl(path);
	}

	@Override
	protected void addRecipes(RecipeOutput output)
	{
	}

}
