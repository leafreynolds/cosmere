/*
 * File updated ~ 4 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.common.registry.CosmereRecipesRegistry;
import leaf.cosmere.common.registry.ItemsRegistry;
import leaf.cosmere.common.resource.ore.OreType;
import leaf.cosmere.common.util.CosmereEnumUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public RecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries, Cosmere.MODID);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return Cosmere.rl(path);
	}

	@Override
	protected void addRecipes(RecipeOutput output)
	{

		ShapedRecipeBuilder
				.shaped(RecipeCategory.DECORATIONS, BlocksRegistry.METALWORKING_TABLE.getBlock())
				.define('X', ItemsRegistry.METAL_INGOTS.get(Metals.MetalType.STEEL).asItem())
				.define('Y', ItemTags.PLANKS)
				.pattern("XX")
				.pattern("YY")
				.pattern("YY")
				.unlockedBy("has_material", has(Tags.Items.INGOTS))
				.save(output);


		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			//theres no reason for uss to add ways to recipe blocks/ingots that minecraft already has
			final Metals.MetalType[] blacklistedTypes = {Metals.MetalType.IRON, Metals.MetalType.GOLD,};
			if (Arrays.stream(blacklistedTypes).anyMatch(metalType::equals))
			{
				continue;
			}

			// specifically copper has no nuggets, so create nugget and move on
			if (metalType == Metals.MetalType.COPPER)
			{
				compressRecipe(Items.COPPER_INGOT, CosmereTags.Items.METAL_NUGGET_TAGS.get(metalType), ItemsRegistry.METAL_NUGGETS.get(metalType)).save(output, ResourceLocation.fromNamespaceAndPath(Cosmere.MODID, "copper_ingot"));
				decompressRecipe(output, ItemsRegistry.METAL_NUGGETS.get(metalType).get(), Tags.Items.INGOTS_COPPER, metalType.getName() + "_item_deconstruct");
				decompressRecipe(output, Items.COPPER_INGOT, Tags.Items.STORAGE_BLOCKS_COPPER, metalType.getName() + "_block_deconstruct");
				continue;
			}

			compressRecipe(BlocksRegistry.METAL_BLOCKS.get(metalType).getBlock(), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType), ItemsRegistry.METAL_INGOTS.get(metalType)).save(output);
			decompressRecipe(output, ItemsRegistry.METAL_INGOTS.get(metalType).get(), BlocksRegistry.METAL_BLOCKS.get(metalType), metalType.getName() + "_block_deconstruct");

			if(metalType.isGodMetal())
			{
				godMetalCompressRecipe(ItemsRegistry.METAL_INGOTS.get(metalType).get(), ItemsRegistry.GOD_METAL_NUGGETS.get(metalType).get()).save(output);
				decompressRecipe(output, ItemsRegistry.GOD_METAL_NUGGETS.get(metalType).get(), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType), metalType.getName() + "_item_deconstruct");
			}
			else
			{
				compressRecipe(ItemsRegistry.METAL_INGOTS.get(metalType).get(), CosmereTags.Items.METAL_NUGGET_TAGS.get(metalType), ItemsRegistry.METAL_NUGGETS.get(metalType)).save(output);
				decompressRecipe(output, ItemsRegistry.METAL_NUGGETS.get(metalType).get(), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType), metalType.getName() + "_item_deconstruct");
			}

			if (metalType.isAlloy())
			{
				Item outputBlend = ItemsRegistry.METAL_RAW_BLEND.get(metalType).asItem();
				addAlloyRecipes(output, metalType, outputBlend, CosmereTags.Items.METAL_RAW_TAGS, "raw_blend");
				addAlloyRecipes(output, metalType, outputBlend, CosmereTags.Items.METAL_DUST_TAGS, "dust_blend");
				addAlloyRecipes(output, metalType, outputBlend, CosmereTags.Items.METAL_INGOT_TAGS, "ingot_blend");

				addOreSmeltingRecipes(output, outputBlend, ItemsRegistry.METAL_INGOTS.get(metalType).asItem(), 1.0f, 200);
			}

		}

		for (OreType oreType : CosmereEnumUtils.ORE_TYPES)
		{
			final Metals.MetalType metalType = oreType.getMetalType();
			addOreSmeltingRecipes(output, BlocksRegistry.METAL_ORE.get(oreType).stone().getBlock(), ItemsRegistry.METAL_INGOTS.get(metalType).asItem(), 1.0f, 200);
			addOreSmeltingRecipes(output, BlocksRegistry.METAL_ORE.get(oreType).deepslate().getBlock(), ItemsRegistry.METAL_INGOTS.get(metalType).asItem(), 1.0f, 200);
			addOreSmeltingRecipes(output, ItemsRegistry.METAL_RAW_ORE.get(metalType).get(), ItemsRegistry.METAL_INGOTS.get(metalType).asItem(), 1.0f, 200);
		}

		SpecialRecipeBuilder.special(leaf.cosmere.common.recipes.GodMetalAlloyNuggetRecipe::new)
				.save(output, ResourceLocation.fromNamespaceAndPath(Cosmere.MODID,
						CosmereRecipesRegistry.GOD_METAL_ALLOY_NUGGET_RECIPE.getInternalRegistryName()).toString());

		SpecialRecipeBuilder.special(leaf.cosmere.common.recipes.GodMetalNuggetsCompress::new)
				.save(output, ResourceLocation.fromNamespaceAndPath(Cosmere.MODID,
						CosmereRecipesRegistry.GOD_METAL_NUGGETS_COMPRESS.getInternalRegistryName()).toString());

		SpecialRecipeBuilder.special(leaf.cosmere.common.recipes.GodMetalNuggetsDecompress::new)
				.save(output, ResourceLocation.fromNamespaceAndPath(Cosmere.MODID,
						CosmereRecipesRegistry.GOD_METAL_NUGGETS_DECOMPRESS.getInternalRegistryName()).toString());

	}

	protected static void addAlloyRecipes(RecipeOutput output, Metals.MetalType metalType, Item result, Map<Metals.MetalType, TagKey<Item>> materialTag, String recipe)
	{
		String s = String.format("alloying/%s/", recipe);

		switch (metalType)
		{
			case STEEL:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 4)
						.unlockedBy("has_item", has(result))
						.requires(materialTag.get(Metals.MetalType.IRON))
						.requires(materialTag.get(Metals.MetalType.IRON))
						.requires(materialTag.get(Metals.MetalType.IRON))
						.requires(Ingredient.of(Items.COAL, Items.CHARCOAL))
						.save(output, Cosmere.rl(s + metalType.getName()));
				break;
			case PEWTER:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 5)
						.unlockedBy("has_item", has(result))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.save(output, Cosmere.rl(s + metalType.getName()));
				break;
			case BRASS:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 2)
						.unlockedBy("has_item", has(result))
						.requires(materialTag.get(Metals.MetalType.ZINC))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.save(output, Cosmere.rl(s + metalType.getName()));
				break;
			case BRONZE:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 4)
						.unlockedBy("has_item", has(result))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.save(output, Cosmere.rl(s + metalType.getName()));
				break;
			case DURALUMIN:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 5)
						.unlockedBy("has_item", has(result))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.save(output, Cosmere.rl(s + metalType.getName()));
				break;
			case NICROSIL:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 4)
						.unlockedBy("has_item", has(result))
						.requires(materialTag.get(Metals.MetalType.CHROMIUM))
						.requires(materialTag.get(Metals.MetalType.NICKEL))
						.requires(materialTag.get(Metals.MetalType.NICKEL))
						.requires(materialTag.get(Metals.MetalType.NICKEL))
						.save(output, Cosmere.rl(s + metalType.getName()));
				break;
			case BENDALLOY:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 9)
						.unlockedBy("has_item", has(result))
						.requires(materialTag.get(Metals.MetalType.CADMIUM))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.save(output, Cosmere.rl(s + metalType.getName()));
				break;
			case ELECTRUM:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 2)
						.unlockedBy("has_item", has(result))
						.requires(materialTag.get(Metals.MetalType.GOLD))
						.requires(materialTag.get(Metals.MetalType.SILVER))
						.save(output, Cosmere.rl(s + metalType.getName()));
				break;
		}
	}
}
