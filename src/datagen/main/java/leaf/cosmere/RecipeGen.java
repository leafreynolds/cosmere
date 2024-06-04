/*
 * File updated ~ 5 - 6 - 2024 ~ Leaf
 */

package leaf.cosmere;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

public class RecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public RecipeGen(PackOutput output, ExistingFileHelper existingFileHelper)
	{
		super(output, existingFileHelper);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return Cosmere.rl(path);
	}

	@Override
	protected void addRecipes(Consumer<FinishedRecipe> consumer)
	{

		ShapedRecipeBuilder
				.shaped(RecipeCategory.DECORATIONS, BlocksRegistry.METALWORKING_TABLE.getBlock())
				.define('X', ItemsRegistry.METAL_INGOTS.get(Metals.MetalType.STEEL).asItem())
				.define('Y', ItemTags.PLANKS)
				.pattern("XX")
				.pattern("YY")
				.pattern("YY")
				.unlockedBy("has_material", has(Tags.Items.INGOTS))
				.save(consumer);


		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			//theres no reason for uss to add ways to recipe blocks/ingots that minecraft already has
			final Metals.MetalType[] blacklistedTypes = {Metals.MetalType.IRON, Metals.MetalType.GOLD,};
			if (Arrays.stream(blacklistedTypes).anyMatch(metalType::equals))
			{
				continue;
			}

			compressRecipe(BlocksRegistry.METAL_BLOCKS.get(metalType).getBlock(), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType), ItemsRegistry.METAL_INGOTS.get(metalType)).save(consumer);
			decompressRecipe(consumer, ItemsRegistry.METAL_INGOTS.get(metalType).get(), BlocksRegistry.METAL_BLOCKS.get(metalType), metalType.getName() + "_block_deconstruct");

			compressRecipe(ItemsRegistry.METAL_INGOTS.get(metalType).get(), CosmereTags.Items.METAL_NUGGET_TAGS.get(metalType), ItemsRegistry.METAL_NUGGETS.get(metalType)).save(consumer);
			decompressRecipe(consumer, ItemsRegistry.METAL_NUGGETS.get(metalType).get(), CosmereTags.Items.METAL_INGOT_TAGS.get(metalType), metalType.getName() + "_item_deconstruct");

			if (metalType.hasOre())
			{
				addOreSmeltingRecipes(consumer, BlocksRegistry.METAL_ORE.get(metalType).getBlock(), ItemsRegistry.METAL_INGOTS.get(metalType).asItem(), 1.0f, 200);
				addOreSmeltingRecipes(consumer, BlocksRegistry.METAL_ORE_DEEPSLATE.get(metalType).getBlock(), ItemsRegistry.METAL_INGOTS.get(metalType).asItem(), 1.0f, 200);
				addOreSmeltingRecipes(consumer, ItemsRegistry.METAL_RAW_ORE.get(metalType).get(), ItemsRegistry.METAL_INGOTS.get(metalType).asItem(), 1.0f, 200);
			}

			if (metalType.isAlloy())
			{
				Item outputBlend = ItemsRegistry.METAL_RAW_BLEND.get(metalType).asItem();
				addAlloyRecipes(consumer, metalType, outputBlend, CosmereTags.Items.METAL_RAW_TAGS, "raw_blend");
				addAlloyRecipes(consumer, metalType, outputBlend, CosmereTags.Items.METAL_DUST_TAGS, "dust_blend");
				addAlloyRecipes(consumer, metalType, outputBlend, CosmereTags.Items.METAL_INGOT_TAGS, "ingot_blend");

				addOreSmeltingRecipes(consumer, outputBlend, ItemsRegistry.METAL_INGOTS.get(metalType).asItem(), 1.0f, 200);
			}

		}
	}

	protected static void addAlloyRecipes(Consumer<FinishedRecipe> consumer, Metals.MetalType metalType, Item output, Map<Metals.MetalType, TagKey<Item>> materialTag, String recipe)
	{
		String s = String.format("alloying/%s/", recipe);

		switch (metalType)
		{
			case STEEL:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 4)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.IRON))
						.requires(materialTag.get(Metals.MetalType.IRON))
						.requires(materialTag.get(Metals.MetalType.IRON))
						.requires(Ingredient.of(Items.COAL, Items.CHARCOAL))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
			case PEWTER:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 5)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
			case BRASS:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 2)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.ZINC))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
			case BRONZE:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 4)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
			case DURALUMIN:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 5)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
			case NICROSIL:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 4)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.CHROMIUM))
						.requires(materialTag.get(Metals.MetalType.NICKEL))
						.requires(materialTag.get(Metals.MetalType.NICKEL))
						.requires(materialTag.get(Metals.MetalType.NICKEL))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
			case BENDALLOY:
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 9)
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
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 2)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.GOLD))
						.requires(materialTag.get(Metals.MetalType.SILVER))
						.save(consumer, Cosmere.rl(s + metalType.getName()));
				break;
		}
	}
}
