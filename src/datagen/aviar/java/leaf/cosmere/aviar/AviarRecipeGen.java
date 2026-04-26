/*
 * File updated ~ 4 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.aviar;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.aviar.common.Aviar;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class AviarRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public AviarRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(output, lookupProvider, Aviar.MODID);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return Aviar.rl(path);
	}

	@Override
	protected void addRecipes(RecipeOutput output)
	{
	}

}
