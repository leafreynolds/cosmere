/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.RecipeRegistry;
import leaf.cosmere.registry.TagsRegistry;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public class RecipeGen extends RecipeProvider implements IConditionBuilder
{
    public RecipeGen(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {

        //example recipes obtained from following website on 3rd April '21. Thank you ChampionAsh5357!
        //https://github.com/ChampionAsh5357/1.16.x-Minecraft-Tutorial/blob/1.16.1-32.0.61-web/src/main/java/io/github/championash5357/tutorial/data/TutorialRecipeProvider.java

        //addBasicArmorRecipes(consumer, ItemsRegistry.RUBY.get(), ItemsRegistry.RUBY_HELMET.get(), ItemsRegistry.RUBY_CHESTPLATE.get(), ItemsRegistry.RUBY_LEGGINGS.get(), ItemsRegistry.RUBY_BOOTS.get());
        //ShapedRecipeBuilder.shapedRecipe(BlocksRegistry.WASHER.get()).key('I', Items.IRON_INGOT).key('W', Items.WATER_BUCKET).patternLine("III").patternLine("IWI").patternLine("III").addCriterion("in_water", enteredBlock(Blocks.WATER)).build(consumer);


        //addOreSmeltingRecipes(consumer, BlocksRegistry.GEM_BLOCK.get(), ItemsRegistry.GUIDE.get(), 1.0f, 200);
        addOreSmeltingRecipes(consumer, ItemsRegistry.METAL_SPIKE.get(Metals.MetalType.IRON).get(), ItemsRegistry.GUIDE.get(), 1.0f, 200);


        CustomRecipeBuilder.customRecipe(RecipeRegistry.VIAL_RECIPE_SERIALIZER.get()).build(consumer, ResourceLocationHelper.prefix("vial_mixing").toString());

        for (Metals.MetalType metalType : Metals.MetalType.values())
        {
            addBasicMetalmindRecipes(consumer, metalType);

            //theres no reason for uss to add ways to recipe blocks/ingots that minecraft already has
            final Metals.MetalType[] blacklistedTypes = {Metals.MetalType.IRON, Metals.MetalType.GOLD,};
            if (Arrays.stream(blacklistedTypes).anyMatch(metalType::equals))
            {
                continue;
            }

            compressRecipe(BlocksRegistry.METAL_BLOCKS.get(metalType).get(), TagsRegistry.Items.METAL_INGOT_TAGS.get(metalType)).build(consumer);
            decompressRecipe(consumer, ItemsRegistry.METAL_INGOTS.get(metalType).get(), TagsRegistry.Items.METAL_BLOCK_ITEM_TAGS.get(metalType), metalType.name().toLowerCase() + "_block_deconstruct");

            compressRecipe(ItemsRegistry.METAL_INGOTS.get(metalType).get(), TagsRegistry.Items.METAL_NUGGET_TAGS.get(metalType)).build(consumer);
            decompressRecipe(consumer, ItemsRegistry.METAL_NUGGETS.get(metalType).get(), TagsRegistry.Items.METAL_INGOT_TAGS.get(metalType), metalType.name().toLowerCase() + "_item_deconstruct");

            if (metalType.hasOre())
            {
                addOreSmeltingRecipes(consumer, metalType.getOreBlock(), metalType.getIngotItem(), 1.0f, 200);
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
                addAlloyRecipes(consumer, metalType, outputBlend, TagsRegistry.Items.METAL_RAW_TAGS, "blend");

                addOreSmeltingRecipes(consumer, outputBlend, metalType.getIngotItem(), 1.0f, 200);
            }

        }
    }


    //todo better alloying recipes than these debug shenanigans
    private void addAlloyRecipes(Consumer<IFinishedRecipe> consumer, Metals.MetalType metalType, Item output, Map<Metals.MetalType, ITag.INamedTag<Item>> materialTag, String recipe)
    {
        String s = String.format("alloying/%s/", recipe);
        switch (metalType)
        {
            case STEEL:
                ShapelessRecipeBuilder.shapelessRecipe(output, 4)
                        .addCriterion("has_item", hasItem(output))
                        .addIngredient(Ingredient.fromTag(Tags.Items.INGOTS_IRON))
                        .addIngredient(Ingredient.fromTag(Tags.Items.INGOTS_IRON))
                        .addIngredient(Ingredient.fromTag(Tags.Items.INGOTS_IRON))
                        .addIngredient(Ingredient.fromItems(Items.COAL, Items.CHARCOAL))
                        .build(consumer, ResourceLocationHelper.prefix(s + metalType.name().toLowerCase(Locale.ROOT)));
                break;
            case PEWTER:
                ShapelessRecipeBuilder.shapelessRecipe(output, 5)
                        .addCriterion("has_item", hasItem(output))
                        .addIngredient(materialTag.get(Metals.MetalType.TIN))
                        .addIngredient(materialTag.get(Metals.MetalType.TIN))
                        .addIngredient(materialTag.get(Metals.MetalType.TIN))
                        .addIngredient(materialTag.get(Metals.MetalType.TIN))
                        .addIngredient(materialTag.get(Metals.MetalType.LEAD))
                        .build(consumer, ResourceLocationHelper.prefix(s + metalType.name().toLowerCase(Locale.ROOT)));
                break;
            case BRASS:
                ShapelessRecipeBuilder.shapelessRecipe(output, 2)
                        .addCriterion("has_item", hasItem(output))
                        .addIngredient(materialTag.get(Metals.MetalType.ZINC))
                        .addIngredient(materialTag.get(Metals.MetalType.COPPER))
                        .build(consumer, ResourceLocationHelper.prefix(s + metalType.name().toLowerCase(Locale.ROOT)));
                break;
            case BRONZE:
                ShapelessRecipeBuilder.shapelessRecipe(output, 4)
                        .addCriterion("has_item", hasItem(output))
                        .addIngredient(materialTag.get(Metals.MetalType.COPPER))
                        .addIngredient(materialTag.get(Metals.MetalType.COPPER))
                        .addIngredient(materialTag.get(Metals.MetalType.COPPER))
                        .addIngredient(materialTag.get(Metals.MetalType.TIN))
                        .build(consumer, ResourceLocationHelper.prefix(s + metalType.name().toLowerCase(Locale.ROOT)));
                break;
            case DURALUMIN:
                ShapelessRecipeBuilder.shapelessRecipe(output, 5)
                        .addCriterion("has_item", hasItem(output))
                        .addIngredient(materialTag.get(Metals.MetalType.ALUMINUM))
                        .addIngredient(materialTag.get(Metals.MetalType.ALUMINUM))
                        .addIngredient(materialTag.get(Metals.MetalType.ALUMINUM))
                        .addIngredient(materialTag.get(Metals.MetalType.ALUMINUM))
                        .addIngredient(materialTag.get(Metals.MetalType.COPPER))
                        .build(consumer, ResourceLocationHelper.prefix(s + metalType.name().toLowerCase(Locale.ROOT)));
                break;
            case NICROSIL:
                ShapelessRecipeBuilder.shapelessRecipe(output, 4)
                        .addCriterion("has_item", hasItem(output))
                        .addIngredient(materialTag.get(Metals.MetalType.CHROMIUM))
                        .addIngredient(materialTag.get(Metals.MetalType.NICKEL))
                        .addIngredient(materialTag.get(Metals.MetalType.NICKEL))
                        .addIngredient(materialTag.get(Metals.MetalType.NICKEL))
                        .build(consumer, ResourceLocationHelper.prefix(s + metalType.name().toLowerCase(Locale.ROOT)));
                break;
            case BENDALLOY:
                ShapelessRecipeBuilder.shapelessRecipe(output, 9)
                        .addCriterion("has_item", hasItem(output))
                        .addIngredient(materialTag.get(Metals.MetalType.CADMIUM))
                        .addIngredient(materialTag.get(Metals.MetalType.LEAD))
                        .addIngredient(materialTag.get(Metals.MetalType.LEAD))
                        .addIngredient(materialTag.get(Metals.MetalType.LEAD))
                        .addIngredient(materialTag.get(Metals.MetalType.LEAD))
                        .addIngredient(materialTag.get(Metals.MetalType.LEAD))
                        .addIngredient(materialTag.get(Metals.MetalType.LEAD))
                        .addIngredient(materialTag.get(Metals.MetalType.TIN))
                        .addIngredient(materialTag.get(Metals.MetalType.TIN))
                        .build(consumer, ResourceLocationHelper.prefix(s + metalType.name().toLowerCase(Locale.ROOT)));
                break;
            case ELECTRUM:
                ShapelessRecipeBuilder.shapelessRecipe(output, 2)
                        .addCriterion("has_item", hasItem(output))
                        .addIngredient(Ingredient.fromTag(Tags.Items.INGOTS_GOLD))
                        .addIngredient(materialTag.get(Metals.MetalType.SILVER))
                        .build(consumer, ResourceLocationHelper.prefix(s + metalType.name().toLowerCase(Locale.ROOT)));
                break;
        }
    }


    protected static void addBasicArmorRecipes(Consumer<IFinishedRecipe> consumer, ITag<Item> inputMaterial, @Nullable Item head, @Nullable Item chest, @Nullable Item legs, @Nullable Item feet)
    {
        if (head != null)
        {
            ShapedRecipeBuilder.shapedRecipe(head).key('X', inputMaterial).patternLine("XXX").patternLine("X X").setGroup("helmets").addCriterion("has_material", hasItem(inputMaterial)).build(consumer);
        }
        if (chest != null)
        {
            ShapedRecipeBuilder.shapedRecipe(chest).key('X', inputMaterial).patternLine("X X").patternLine("XXX").patternLine("XXX").setGroup("chestplates").addCriterion("has_material", hasItem(inputMaterial)).build(consumer);
        }
        if (legs != null)
        {
            ShapedRecipeBuilder.shapedRecipe(legs).key('X', inputMaterial).patternLine("XXX").patternLine("X X").patternLine("X X").setGroup("leggings").addCriterion("has_material", hasItem(inputMaterial)).build(consumer);
        }
        if (feet != null)
        {
            ShapedRecipeBuilder.shapedRecipe(feet).key('X', inputMaterial).patternLine("X X").patternLine("X X").setGroup("boots").addCriterion("has_material", hasItem(inputMaterial)).build(consumer);
        }
    }

    protected static void addBasicMetalmindRecipes(Consumer<IFinishedRecipe> consumer, Metals.MetalType metalType)
    {
        ITag.INamedTag<Item> inputMaterial = metalType.getMetalIngotTag();

        if (metalType.hasFeruchemicalEffect())
        {
            ShapedRecipeBuilder.shapedRecipe(metalType.getNecklaceItem()).key('X', inputMaterial).patternLine("XXX").patternLine("X X").patternLine(" X ").setGroup("necklace").addCriterion("has_material", hasItem(inputMaterial)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(metalType.getRingItem()).key('X', inputMaterial).patternLine(" X ").patternLine("X X").patternLine(" X ").setGroup("ring").addCriterion("has_material", hasItem(inputMaterial)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(metalType.getBraceletItem()).key('X', inputMaterial).patternLine(" X ").patternLine("X X").patternLine("X X").setGroup("bracelet").addCriterion("has_material", hasItem(inputMaterial)).build(consumer);
        }
        if (metalType.hasHemalurgicEffect())
        {
            ShapedRecipeBuilder.shapedRecipe(metalType.getSpikeItem()).key('X', inputMaterial).patternLine(" X ").patternLine(" X ").setGroup("spike").addCriterion("has_material", hasItem(inputMaterial)).build(consumer);
        }
    }

    private void decompressRecipe(Consumer<IFinishedRecipe> consumer, IItemProvider output, ITag<Item> input, String name)
    {
        ShapelessRecipeBuilder.shapelessRecipe(output, 9)
                .addCriterion("has_item", hasItem(output))
                .addIngredient(input)
                .build(consumer, ResourceLocationHelper.prefix("conversions/" + name));
    }

    private ShapedRecipeBuilder compressRecipe(IItemProvider output, ITag<Item> input)
    {
        return ShapedRecipeBuilder.shapedRecipe(output)
                .key('I', input)
                .patternLine("III")
                .patternLine("III")
                .patternLine("III")
                .addCriterion("has_item", hasItem(input));
    }

    protected static void addOreSmeltingRecipes(Consumer<IFinishedRecipe> consumer, IItemProvider ore, Item result, float experience, int time)
    {
        String name = result.getRegistryName().getPath();
        String path = ore.asItem().getRegistryName().getPath();
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ore), result, experience, time).addCriterion("has_ore", hasItem(ore)).build(consumer, new ResourceLocation(Cosmere.MODID, name + "_from_smelting_" + path));
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ore), result, experience, time / 2).addCriterion("has_ore", hasItem(ore)).build(consumer, new ResourceLocation(Cosmere.MODID, name + "_from_blasting_" + path));
    }

    protected static void addCookingRecipes(Consumer<IFinishedRecipe> consumer, IItemProvider inputItem, Item result, float experience, int time)
    {
        String name = result.getRegistryName().getPath();
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(inputItem), result, experience, time).addCriterion("has_item", hasItem(inputItem)).build(consumer, new ResourceLocation(Cosmere.MODID, name + "_from_smelting"));
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(inputItem), result, experience, time / 2, IRecipeSerializer.SMOKING).addCriterion("has_item", hasItem(inputItem)).build(consumer, new ResourceLocation(Cosmere.MODID, name + "_from_smoking"));
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(inputItem), result, experience, time, IRecipeSerializer.CAMPFIRE_COOKING).addCriterion("has_item", hasItem(inputItem)).build(consumer, new ResourceLocation(Cosmere.MODID, name + "_from_campfire"));
    }
}
