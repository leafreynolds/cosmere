/*
 * File updated ~ 5 - 6 - 2024 ~ Leaf
 */

package leaf.cosmere.aondor;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.aondor.common.AonDor;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class AonDorRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public AonDorRecipeGen(PackOutput output, ExistingFileHelper existingFileHelper)
	{
		super(output, existingFileHelper);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return AonDor.rl(path);
	}

	@Override
	protected void addRecipes(Consumer<FinishedRecipe> consumer)
	{

	}
}
