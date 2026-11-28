/*
 * File updated ~ 4 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.tools;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.tools.common.CosmereTools;
import leaf.cosmere.tools.common.registries.ToolsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ToolsRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public ToolsRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(output, lookupProvider, CosmereTools.MODID);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return CosmereTools.rl(path);
	}

	@Override
	protected void addRecipes(RecipeOutput output)
	{

		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			if (!metalType.hasMaterialItem())
			{
				continue;
			}

			addPickaxeRecipe(output, ToolsItems.METAL_PICKAXES.get(metalType), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType));
			addShovelRecipe(output, ToolsItems.METAL_SHOVEL.get(metalType), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType));
			addAxeRecipe(output, ToolsItems.METAL_AXES.get(metalType), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType));
			addSwordRecipe(output, ToolsItems.METAL_SWORDS.get(metalType), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType));
			addHoeRecipe(output, ToolsItems.METAL_HOE.get(metalType), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType));

			addArmorRecipes(
					output,
					CosmereTags.Items.METAL_INGOT_TAGS.get(metalType),
					ToolsItems.METAL_HELMETS.get(metalType),
					ToolsItems.METAL_CHESTPLATES.get(metalType),
					ToolsItems.METAL_LEGGINGS.get(metalType),
					ToolsItems.METAL_BOOTS.get(metalType)
			);

		}
	}
}
