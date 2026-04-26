/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.aondor;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.aondor.common.AonDor;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class AonDorRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public AonDorRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(output, lookupProvider, AonDor.MODID);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return AonDor.rl(path);
	}

	@Override
	protected void addRecipes(RecipeOutput output)
	{

	}
}
