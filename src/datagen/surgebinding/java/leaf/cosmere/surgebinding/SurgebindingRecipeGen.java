/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.helpers.ResourceLocationHelper;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;

public class SurgebindingRecipeGen extends RecipeProvider implements IConditionBuilder
{
	public SurgebindingRecipeGen(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
	{
		for (Roshar.Gemstone gemstone : Roshar.Gemstone.values())
		{
			compressRecipe(SurgebindingBlocks.GEM_BLOCKS.get(gemstone).getBlock(), CosmereTags.Items.GEM_TAGS.get(gemstone)).save(consumer);
			decompressRecipe(consumer, SurgebindingItems.GEMSTONE_BROAMS.get(gemstone).get(), CosmereTags.Items.GEM_BLOCK_ITEM_TAGS.get(gemstone), gemstone.getName() + "_block_deconstruct");

			addOreSmeltingRecipes(consumer, SurgebindingBlocks.GEM_ORE.get(gemstone).getBlock(), SurgebindingItems.GEMSTONE_MARKS.get(gemstone).get(), 1.0f, 1000);
			addOreSmeltingRecipes(consumer, SurgebindingBlocks.GEM_ORE_DEEPSLATE.get(gemstone).getBlock(), SurgebindingItems.GEMSTONE_BROAMS.get(gemstone).get(), 1.0f, 1000);
		}
	}


	//todo better alloying recipes than these debug shenanigans
	private void addAlloyRecipes(Consumer<FinishedRecipe> consumer, Metals.MetalType metalType, Item output, Map<Metals.MetalType, TagKey<Item>> materialTag, String recipe)
	{
		String s = String.format("alloying/%s/", recipe);

		switch (metalType)
		{
			case STEEL:
				ShapelessRecipeBuilder.shapeless(output, 4)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.IRON))
						.requires(materialTag.get(Metals.MetalType.IRON))
						.requires(materialTag.get(Metals.MetalType.IRON))
						.requires(Ingredient.of(Items.COAL, Items.CHARCOAL))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
			case PEWTER:
				ShapelessRecipeBuilder.shapeless(output, 5)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
			case BRASS:
				ShapelessRecipeBuilder.shapeless(output, 2)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.ZINC))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
			case BRONZE:
				ShapelessRecipeBuilder.shapeless(output, 4)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
			case DURALUMIN:
				ShapelessRecipeBuilder.shapeless(output, 5)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
			case NICROSIL:
				ShapelessRecipeBuilder.shapeless(output, 4)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.CHROMIUM))
						.requires(materialTag.get(Metals.MetalType.NICKEL))
						.requires(materialTag.get(Metals.MetalType.NICKEL))
						.requires(materialTag.get(Metals.MetalType.NICKEL))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
			case BENDALLOY:
				ShapelessRecipeBuilder.shapeless(output, 9)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.CADMIUM))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
			case ELECTRUM:
				ShapelessRecipeBuilder.shapeless(output, 2)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.GOLD))
						.requires(materialTag.get(Metals.MetalType.SILVER))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
		}
	}


	protected static void addBasicArmorRecipes(Consumer<FinishedRecipe> consumer, TagKey<Item> inputMaterial, @Nullable Item head, @Nullable Item chest, @Nullable Item legs, @Nullable Item feet)
	{
		if (head != null)
		{
			ShapedRecipeBuilder.shaped(head).define('X', inputMaterial).pattern("XXX").pattern("X X").group("helmets").unlockedBy("has_material", has(inputMaterial)).save(consumer);
		}
		if (chest != null)
		{
			ShapedRecipeBuilder.shaped(chest).define('X', inputMaterial).pattern("X X").pattern("XXX").pattern("XXX").group("chestplates").unlockedBy("has_material", has(inputMaterial)).save(consumer);
		}
		if (legs != null)
		{
			ShapedRecipeBuilder.shaped(legs).define('X', inputMaterial).pattern("XXX").pattern("X X").pattern("X X").group("leggings").unlockedBy("has_material", has(inputMaterial)).save(consumer);
		}
		if (feet != null)
		{
			ShapedRecipeBuilder.shaped(feet).define('X', inputMaterial).pattern("X X").pattern("X X").group("boots").unlockedBy("has_material", has(inputMaterial)).save(consumer);
		}
	}

	private void decompressRecipe(Consumer<FinishedRecipe> consumer, ItemLike output, TagKey<Item> input, String name)
	{
		ShapelessRecipeBuilder.shapeless(output, 9)
				.unlockedBy("has_item", has(output))
				.requires(input)
				.save(consumer, Cosmere.rl("conversions/" + name));
	}

	private ShapedRecipeBuilder compressRecipe(ItemLike output, TagKey<Item> input)
	{
		return ShapedRecipeBuilder.shaped(output)
				.define('I', input)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_item", has(input));
	}

	protected void addOreSmeltingRecipes(Consumer<FinishedRecipe> consumer, ItemLike ore, Item result, float experience, int time)
	{

		String name = ResourceLocationHelper.get(result).getPath();
		String path = ResourceLocationHelper.get(ore.asItem()).getPath();
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ore), result, experience, time).unlockedBy("has_ore", has(ore)).save(consumer, Surgebinding.rl(name + "_from_smelting_" + path));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ore), result, experience, time / 2).unlockedBy("has_ore", has(ore)).save(consumer, new ResourceLocation(Surgebinding.MODID, name + "_from_blasting_" + path));
	}

	protected void addCookingRecipes(Consumer<FinishedRecipe> consumer, ItemLike inputItem, Item result, float experience, int time)
	{
		String name = ResourceLocationHelper.get(result).getPath();
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(inputItem), result, experience, time).unlockedBy("has_item", has(inputItem)).save(consumer, Surgebinding.rl(name + "_from_smelting"));
		SimpleCookingRecipeBuilder.cooking(Ingredient.of(inputItem), result, experience, time / 2, RecipeSerializer.SMOKING_RECIPE).unlockedBy("has_item", has(inputItem)).save(consumer, Surgebinding.rl(name + "_from_smoking"));
		SimpleCookingRecipeBuilder.cooking(Ingredient.of(inputItem), result, experience, time, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_item", has(inputItem)).save(consumer, Surgebinding.rl(name + "_from_campfire"));
	}
}
