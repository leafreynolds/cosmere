/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.constants.Roshar;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.RecipeRegistry;
import leaf.cosmere.registry.TagsRegistry;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

public class RecipeGen extends RecipeProvider implements IConditionBuilder
{
	public RecipeGen(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
	{

		//example recipes obtained from following website on 3rd April '21. Thank you ChampionAsh5357!
		//https://github.com/ChampionAsh5357/1.16.x-Minecraft-Tutorial/blob/1.16.1-32.0.61-web/src/main/java/io/github/championash5357/tutorial/data/TutorialRecipeProvider.java

		//addBasicArmorRecipes(consumer, ItemsRegistry.RUBY.get(), ItemsRegistry.RUBY_HELMET.get(), ItemsRegistry.RUBY_CHESTPLATE.get(), ItemsRegistry.RUBY_LEGGINGS.get(), ItemsRegistry.RUBY_BOOTS.get());
		//ShapedRecipeBuilder.shapedRecipe(BlocksRegistry.WASHER.get()).key('I', Items.IRON_INGOT).key('W', Items.WATER_BUCKET).patternLine("III").patternLine("IWI").patternLine("III").addCriterion("in_water", enteredBlock(Blocks.WATER)).build(consumer);


		//addOreSmeltingRecipes(consumer, BlocksRegistry.GEM_BLOCK.get(), ItemsRegistry.GUIDE.get(), 1.0f, 200);
		addOreSmeltingRecipes(consumer, ItemsRegistry.METAL_SPIKE.get(Metals.MetalType.IRON).get(), ItemsRegistry.GUIDE.get(), 1.0f, 200);

		ShapedRecipeBuilder.shaped(ItemsRegistry.METAL_VIAL.get()).define('X', Items.IRON_NUGGET).define('Y', Items.GLASS).pattern("X").pattern("Y").unlockedBy("has_material", has(Items.GLASS)).save(consumer);

		ShapedRecipeBuilder
				.shaped(BlocksRegistry.METALWORKING_TABLE.get())
				.define('X', Tags.Items.INGOTS)
				.define('Y', ItemTags.PLANKS)
				.pattern("XX")
				.pattern("YY")
				.pattern("YY")
				.unlockedBy("has_material", has(Tags.Items.INGOTS))
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(ItemsRegistry.COIN_POUCH.get(), 1)
				.unlockedBy("has_item", has(Tags.Items.LEATHER))
				.requires(Tags.Items.LEATHER)
				.requires(Tags.Items.STRING)
				.save(consumer);

		SpecialRecipeBuilder.special(RecipeRegistry.VIAL_RECIPE_SERIALIZER.get()).save(consumer, ResourceLocationHelper.prefix("vial_mixing").toString());

		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			addBasicMetalmindRecipes(consumer, metalType);

			//theres no reason for uss to add ways to recipe blocks/ingots that minecraft already has
			final Metals.MetalType[] blacklistedTypes = {Metals.MetalType.IRON, Metals.MetalType.GOLD,};
			if (Arrays.stream(blacklistedTypes).anyMatch(metalType::equals))
			{
				continue;
			}

			compressRecipe(BlocksRegistry.METAL_BLOCKS.get(metalType).get(), TagsRegistry.Items.METAL_INGOT_TAGS.get(metalType)).save(consumer);
			decompressRecipe(consumer, ItemsRegistry.METAL_INGOTS.get(metalType).get(), TagsRegistry.Items.METAL_BLOCK_ITEM_TAGS.get(metalType), metalType.getName() + "_block_deconstruct");

			compressRecipe(ItemsRegistry.METAL_INGOTS.get(metalType).get(), TagsRegistry.Items.METAL_NUGGET_TAGS.get(metalType)).save(consumer);
			decompressRecipe(consumer, ItemsRegistry.METAL_NUGGETS.get(metalType).get(), TagsRegistry.Items.METAL_INGOT_TAGS.get(metalType), metalType.getName() + "_item_deconstruct");

			if (metalType.hasOre())
			{
				addOreSmeltingRecipes(consumer, metalType.getOreBlock(), metalType.getIngotItem(), 1.0f, 200);
				addOreSmeltingRecipes(consumer, metalType.getDeepslateOreBlock(), metalType.getIngotItem(), 1.0f, 200);
				addOreSmeltingRecipes(consumer, metalType.getRawMetalItem(), metalType.getIngotItem(), 1.0f, 200);
			}

			if (metalType.isAlloy())
			{
/*                Item outputNugget = ItemsRegistry.METAL_NUGGETS.get(metalType).get();
                Item outputIngot = ItemsRegistry.METAL_INGOTS.get(metalType).get();
                Item outputBlock = BlocksRegistry.METAL_BLOCKS.get(metalType).get();
                addAlloyRecipes(consumer, metalType, outputNugget, TagsRegistry.Items.METAL_NUGGET_TAGS,"nugget");
                addAlloyRecipes(consumer, metalType, outputIngot, TagsRegistry.Items.METAL_INGOT_TAGS,"ingot");
                addAlloyRecipes(consumer, metalType, outputBlock, TagsRegistry.Items.METAL_BLOCK_ITEM_TAGS,"block");*/

				Item outputBlend = metalType.getRawMetalItem();
				addAlloyRecipes(consumer, metalType, outputBlend, TagsRegistry.Items.METAL_RAW_TAGS, "raw_blend");
				addAlloyRecipes(consumer, metalType, outputBlend, TagsRegistry.Items.METAL_DUST_TAGS, "dust_blend");
				addAlloyRecipes(consumer, metalType, outputBlend, TagsRegistry.Items.METAL_INGOT_TAGS, "ingot_blend");

				addOreSmeltingRecipes(consumer, outputBlend, metalType.getIngotItem(), 1.0f, 200);
			}

		}


		for (Roshar.Polestone polestone : Roshar.Polestone.values())
		{
			compressRecipe(BlocksRegistry.GEM_BLOCKS.get(polestone).get(), TagsRegistry.Items.GEM_TAGS.get(polestone)).save(consumer);
			decompressRecipe(consumer, ItemsRegistry.POLESTONE_BROAMS.get(polestone).get(), TagsRegistry.Items.GEM_BLOCK_ITEM_TAGS.get(polestone), polestone.getName() + "_block_deconstruct");

			addOreSmeltingRecipes(consumer, BlocksRegistry.GEM_ORE.get(polestone).get(), ItemsRegistry.POLESTONE_MARKS.get(polestone).get(), 1.0f, 1000);
			addOreSmeltingRecipes(consumer, BlocksRegistry.GEM_ORE_DEEPSLATE.get(polestone).get(), ItemsRegistry.POLESTONE_BROAMS.get(polestone).get(), 1.0f, 1000);
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
						.save(consumer, ResourceLocationHelper.prefix(s + metalType.getName()));
				break;
			case PEWTER:
				ShapelessRecipeBuilder.shapeless(output, 5)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.requires(materialTag.get(Metals.MetalType.LEAD))
						.save(consumer, ResourceLocationHelper.prefix(s + metalType.getName()));
				break;
			case BRASS:
				ShapelessRecipeBuilder.shapeless(output, 2)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.ZINC))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.save(consumer, ResourceLocationHelper.prefix(s + metalType.getName()));
				break;
			case BRONZE:
				ShapelessRecipeBuilder.shapeless(output, 4)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.requires(materialTag.get(Metals.MetalType.TIN))
						.save(consumer, ResourceLocationHelper.prefix(s + metalType.getName()));
				break;
			case DURALUMIN:
				ShapelessRecipeBuilder.shapeless(output, 5)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.ALUMINUM))
						.requires(materialTag.get(Metals.MetalType.COPPER))
						.save(consumer, ResourceLocationHelper.prefix(s + metalType.getName()));
				break;
			case NICROSIL:
				ShapelessRecipeBuilder.shapeless(output, 4)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.CHROMIUM))
						.requires(materialTag.get(Metals.MetalType.NICKEL))
						.requires(materialTag.get(Metals.MetalType.NICKEL))
						.requires(materialTag.get(Metals.MetalType.NICKEL))
						.save(consumer, ResourceLocationHelper.prefix(s + metalType.getName()));
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
						.save(consumer, ResourceLocationHelper.prefix(s + metalType.getName()));
				break;
			case ELECTRUM:
				ShapelessRecipeBuilder.shapeless(output, 2)
						.unlockedBy("has_item", has(output))
						.requires(materialTag.get(Metals.MetalType.GOLD))
						.requires(materialTag.get(Metals.MetalType.SILVER))
						.save(consumer, ResourceLocationHelper.prefix(s + metalType.getName()));
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

	protected static void addBasicMetalmindRecipes(Consumer<FinishedRecipe> consumer, Metals.MetalType metalType)
	{
		TagKey<Item> inputMaterial = metalType.getMetalIngotTag();

		if (metalType.hasFeruchemicalEffect())
		{
			ShapedRecipeBuilder.shaped(metalType.getNecklaceItem()).define('X', inputMaterial).pattern("XXX").pattern("X X").pattern(" X ").group("necklace").unlockedBy("has_material", has(inputMaterial)).save(consumer);
			ShapedRecipeBuilder.shaped(metalType.getRingItem()).define('X', inputMaterial).pattern(" X ").pattern("X X").pattern(" X ").group("ring").unlockedBy("has_material", has(inputMaterial)).save(consumer);
			ShapedRecipeBuilder.shaped(metalType.getBraceletItem()).define('X', inputMaterial).pattern(" X ").pattern("X X").pattern("X X").group("bracelet").unlockedBy("has_material", has(inputMaterial)).save(consumer);
		}
		if (metalType.hasHemalurgicEffect())
		{
			ShapedRecipeBuilder.shaped(metalType.getSpikeItem()).define('X', inputMaterial).pattern("X").pattern("X").group("spike").unlockedBy("has_material", has(inputMaterial)).save(consumer);
		}
	}

	private void decompressRecipe(Consumer<FinishedRecipe> consumer, ItemLike output, TagKey<Item> input, String name)
	{
		ShapelessRecipeBuilder.shapeless(output, 9)
				.unlockedBy("has_item", has(output))
				.requires(input)
				.save(consumer, ResourceLocationHelper.prefix("conversions/" + name));
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

	protected static void addOreSmeltingRecipes(Consumer<FinishedRecipe> consumer, ItemLike ore, Item result, float experience, int time)
	{

		String name = ResourceLocationHelper.get(result).getPath();
		String path = ResourceLocationHelper.get(ore.asItem()).getPath();
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ore), result, experience, time).unlockedBy("has_ore", has(ore)).save(consumer, new ResourceLocation(Cosmere.MODID, name + "_from_smelting_" + path));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ore), result, experience, time / 2).unlockedBy("has_ore", has(ore)).save(consumer, new ResourceLocation(Cosmere.MODID, name + "_from_blasting_" + path));
	}

	protected static void addCookingRecipes(Consumer<FinishedRecipe> consumer, ItemLike inputItem, Item result, float experience, int time)
	{
		String name = ResourceLocationHelper.get(result).getPath();
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(inputItem), result, experience, time).unlockedBy("has_item", has(inputItem)).save(consumer, new ResourceLocation(Cosmere.MODID, name + "_from_smelting"));
		SimpleCookingRecipeBuilder.cooking(Ingredient.of(inputItem), result, experience, time / 2, RecipeSerializer.SMOKING_RECIPE).unlockedBy("has_item", has(inputItem)).save(consumer, new ResourceLocation(Cosmere.MODID, name + "_from_smoking"));
		SimpleCookingRecipeBuilder.cooking(Ingredient.of(inputItem), result, experience, time, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_item", has(inputItem)).save(consumer, new ResourceLocation(Cosmere.MODID, name + "_from_campfire"));
	}
}
