/*
 * File updated ~ 24 - 6 - 2026 ~ Leaf
 */

package leaf.cosmere;

import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.crafting.DifferenceIngredient;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public abstract class BaseRecipeProvider extends RecipeProvider
{

	private final String modid;

	protected BaseRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modid)
	{
		super(output, registries);
		this.modid = modid;
	}

	@Override
	protected final void buildRecipes(RecipeOutput output)
	{
		addRecipes(output);
	}

	protected abstract ResourceLocation makeRL(String path);

	protected abstract void addRecipes(RecipeOutput output);

	public static Ingredient difference(TagKey<Item> base, ItemLike subtracted)
	{
		return DifferenceIngredient.of(Ingredient.of(base), Ingredient.of(subtracted));
	}

	protected void addOreSmeltingRecipes(RecipeOutput output, ItemLike ore, Item result, float experience, int time)
	{
		String name = RegistryHelper.get(result).getPath();
		String path = RegistryHelper.get(ore.asItem()).getPath();
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ore), RecipeCategory.MISC, result, experience, time).unlockedBy("has_ore", has(ore)).save(output, makeRL(name + "_from_smelting_" + path));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ore), RecipeCategory.MISC, result, experience, time / 2).unlockedBy("has_ore", has(ore)).save(output, makeRL(name + "_from_blasting_" + path));
	}

	protected void addCookingRecipes(RecipeOutput output, ItemLike inputItem, Item result, float experience, int time)
	{
		String name = RegistryHelper.get(result).getPath();

		SimpleCookingRecipeBuilder.smelting(
						Ingredient.of(inputItem),
						RecipeCategory.FOOD,
						result,
						experience,
						time)
				.unlockedBy("has_item", has(inputItem))
				.save(output, ResourceLocation.fromNamespaceAndPath(modid, name + "_from_smelting"));

		SimpleCookingRecipeBuilder.smoking(
						Ingredient.of(inputItem),
						RecipeCategory.FOOD,
						result,
						experience,
						time / 2)
				.unlockedBy("has_item", has(inputItem))
				.save(output, ResourceLocation.fromNamespaceAndPath(modid, name + "_from_smoking"));

		SimpleCookingRecipeBuilder.campfireCooking(
						Ingredient.of(inputItem),
						RecipeCategory.FOOD,
						result,
						experience,
						time)
				.unlockedBy("has_item", has(inputItem))
				.save(output, ResourceLocation.fromNamespaceAndPath(modid, name + "_from_campfire"));
	}

	protected void addPickaxeRecipe(RecipeOutput output, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(RecipeCategory.TOOLS, outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("XXX")
				.pattern(" Y ")
				.pattern(" Y ")
				.group("pickaxe")
				.unlockedBy("has_material", has(inputMaterial))
				.save(output);
	}

	protected void addShovelRecipe(RecipeOutput output, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(RecipeCategory.TOOLS, outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("X")
				.pattern("Y")
				.pattern("Y")
				.group("shovel")
				.unlockedBy("has_material", has(inputMaterial))
				.save(output);
	}

	protected void addAxeRecipe(RecipeOutput output, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(RecipeCategory.TOOLS, outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("XX")
				.pattern("XY")
				.pattern(" Y")
				.group("axe")
				.unlockedBy("has_material", has(inputMaterial))
				.save(output);
	}

	protected void addSwordRecipe(RecipeOutput output, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(RecipeCategory.COMBAT, outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("X")
				.pattern("X")
				.pattern("Y")
				.group("sword")
				.unlockedBy("has_material", has(inputMaterial))
				.save(output);
	}

	protected void addHoeRecipe(RecipeOutput output, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(RecipeCategory.TOOLS, outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("XX")
				.pattern(" Y")
				.pattern(" Y")
				.group("hoe")
				.unlockedBy("has_material", has(inputMaterial))
				.save(output);
	}


	protected void addArmorRecipes(RecipeOutput output, TagKey<Item> inputMaterial, @Nullable ItemLike head, @Nullable ItemLike chest, @Nullable ItemLike legs, @Nullable ItemLike feet)
	{
		if (head != null)
		{
			ShapedRecipeBuilder
					.shaped(RecipeCategory.COMBAT, head)
					.define('X', inputMaterial)
					.pattern("XXX")
					.pattern("X X")
					.group("helmet")
					.unlockedBy("has_material", has(inputMaterial))
					.save(output);
		}
		if (chest != null)
		{
			ShapedRecipeBuilder
					.shaped(RecipeCategory.COMBAT, chest)
					.define('X', inputMaterial)
					.pattern("X X")
					.pattern("XXX")
					.pattern("XXX")
					.group("chestplate")
					.unlockedBy("has_material", has(inputMaterial))
					.save(output);
		}
		if (legs != null)
		{
			ShapedRecipeBuilder
					.shaped(RecipeCategory.COMBAT, legs)
					.define('X', inputMaterial)
					.pattern("XXX")
					.pattern("X X")
					.pattern("X X")
					.group("leggings")
					.unlockedBy("has_material", has(inputMaterial))
					.save(output);
		}
		if (feet != null)
		{
			ShapedRecipeBuilder
					.shaped(RecipeCategory.COMBAT, feet)
					.define('X', inputMaterial)
					.pattern("X X")
					.pattern("X X")
					.group("boots")
					.unlockedBy("has_material", has(inputMaterial))
					.save(output);
		}
	}

	protected void decompressRecipe(RecipeOutput output, ItemLike outputItem, ItemLike input, String name)
	{
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, outputItem, 9)
				.unlockedBy("has_item", has(outputItem))
				.requires(input)
				.save(output, ResourceLocation.fromNamespaceAndPath(modid, "conversions/" + name));
	}

	protected void decompressRecipe(RecipeOutput output, ItemLike outputItem, TagKey<Item> input, String name)
	{
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, outputItem, 9)
				.unlockedBy("has_item", has(outputItem))
				.requires(input)
				.save(output, ResourceLocation.fromNamespaceAndPath(modid, "conversions/" + name));
	}

	protected ShapedRecipeBuilder compressRecipe(ItemLike output, TagKey<Item> input)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, output)
				.define('I', input)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_item", has(input));
	}

	protected ShapedRecipeBuilder compressRecipe(ItemLike output, TagKey<Item> input, ItemLike center)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, output)
				.define('I', input)
				.define('J', center)
				.pattern("III")
				.pattern("IJI")
				.pattern("III")
				.unlockedBy("has_item", has(input));
	}

	protected ShapedRecipeBuilder godMetalCompressRecipe(ItemLike output, ItemLike input)
	{
		CompoundTag tag = new CompoundTag();
		tag.putInt("nuggetSize", 16);
		Ingredient ingredient = DataComponentIngredient.of(false, DataComponents.CUSTOM_DATA, CustomData.of(tag), input.asItem());

		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, output)
				.define('I', ingredient)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_item", has(input));
	}
}
