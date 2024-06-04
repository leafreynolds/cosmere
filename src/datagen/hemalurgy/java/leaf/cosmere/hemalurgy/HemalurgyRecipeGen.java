/*
 * File updated ~ 5 - 6 - 2024 ~ Leaf
 */

package leaf.cosmere.hemalurgy;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.ItemsRegistry;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class HemalurgyRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public HemalurgyRecipeGen(PackOutput output, ExistingFileHelper existingFileHelper)
	{
		super(output, existingFileHelper);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return Hemalurgy.rl(path);
	}

	@Override
	protected void addRecipes(Consumer<FinishedRecipe> consumer)
	{
		addOreSmeltingRecipes(consumer, HemalurgyItems.METAL_SPIKE.get(Metals.MetalType.IRON), ItemsRegistry.GUIDE.get(), 1.0f, 200);

		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			addSpikes(consumer, metalType);
		}
	}

	protected static void addSpikes(Consumer<FinishedRecipe> consumer, Metals.MetalType metalType)
	{
		TagKey<Item> inputMaterial = metalType.getMetalIngotTag();

		if (metalType.hasHemalurgicEffect())
		{
			ShapedRecipeBuilder
					.shaped(RecipeCategory.TOOLS, HemalurgyItems.METAL_SPIKE.get(metalType))
					.define('X', inputMaterial)
					.pattern("X")
					.pattern("X")
					.group("spike")
					.unlockedBy("has_material", has(inputMaterial)).save(consumer);
		}
	}

}
