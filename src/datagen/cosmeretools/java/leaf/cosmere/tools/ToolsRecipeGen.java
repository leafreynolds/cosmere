/*
 * File updated ~ 24 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.tools;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.tools.common.registries.ToolsItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ToolsRecipeGen extends RecipeProvider implements IConditionBuilder
{
	public ToolsRecipeGen(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer)
	{

		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (!metalType.hasMaterialItem())
			{
				return;
			}

			addPickaxeRecipe(consumer, ToolsItems.METAL_PICKAXES.get(metalType), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType));
			addShovelRecipe(consumer, ToolsItems.METAL_SHOVEL.get(metalType), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType));
			addAxeRecipe(consumer, ToolsItems.METAL_AXES.get(metalType), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType));
			addSwordRecipe(consumer, ToolsItems.METAL_SWORDS.get(metalType), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType));
			addHoeRecipe(consumer, ToolsItems.METAL_HOE.get(metalType), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType));

			addArmorRecipes(
					consumer,
					CosmereTags.Items.METAL_INGOT_TAGS.get(metalType),
					ToolsItems.METAL_HELMETS.get(metalType),
					ToolsItems.METAL_CHESTPLATES.get(metalType),
					ToolsItems.METAL_LEGGINGS.get(metalType),
					ToolsItems.METAL_BOOTS.get(metalType)
			);

		}
	}

	private void addPickaxeRecipe(Consumer<FinishedRecipe> consumer, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("XXX")
				.pattern(" Y ")
				.pattern(" Y ")
				.group("pickaxe")
				.unlockedBy("has_material", has(inputMaterial))
				.save(consumer);
	}

	private void addShovelRecipe(Consumer<FinishedRecipe> consumer, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("X")
				.pattern("Y")
				.pattern("Y")
				.group("shovel")
				.unlockedBy("has_material", has(inputMaterial))
				.save(consumer);
	}

	private void addAxeRecipe(Consumer<FinishedRecipe> consumer, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("XX")
				.pattern("XY")
				.pattern(" Y")
				.group("axe")
				.unlockedBy("has_material", has(inputMaterial))
				.save(consumer);
	}

	private void addSwordRecipe(Consumer<FinishedRecipe> consumer, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("X")
				.pattern("X")
				.pattern("Y")
				.group("sword")
				.unlockedBy("has_material", has(inputMaterial))
				.save(consumer);
	}

	private void addHoeRecipe(Consumer<FinishedRecipe> consumer, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("XX")
				.pattern(" Y")
				.pattern(" Y")
				.group("hoe")
				.unlockedBy("has_material", has(inputMaterial))
				.save(consumer);
	}


	protected static void addArmorRecipes(Consumer<FinishedRecipe> consumer, TagKey<Item> inputMaterial, @Nullable ItemLike head, @Nullable ItemLike chest, @Nullable ItemLike legs, @Nullable ItemLike feet)
	{
		if (head != null)
		{
			ShapedRecipeBuilder
					.shaped(head)
					.define('X', inputMaterial)
					.pattern("XXX")
					.pattern("X X")
					.group("helmet")
					.unlockedBy("has_material", has(inputMaterial))
					.save(consumer);
		}
		if (chest != null)
		{
			ShapedRecipeBuilder
					.shaped(chest)
					.define('X', inputMaterial)
					.pattern("X X")
					.pattern("XXX")
					.pattern("XXX")
					.group("chestplate")
					.unlockedBy("has_material", has(inputMaterial))
					.save(consumer);
		}
		if (legs != null)
		{
			ShapedRecipeBuilder
					.shaped(legs)
					.define('X', inputMaterial)
					.pattern("XXX")
					.pattern("X X")
					.pattern("X X")
					.group("leggings")
					.unlockedBy("has_material", has(inputMaterial))
					.save(consumer);
		}
		if (feet != null)
		{
			ShapedRecipeBuilder
					.shaped(feet)
					.define('X', inputMaterial)
					.pattern("X X")
					.pattern("X X")
					.group("boots")
					.unlockedBy("has_material", has(inputMaterial))
					.save(consumer);
		}
	}
}
