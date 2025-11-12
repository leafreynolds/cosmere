package leaf.cosmere.metallurgy;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.metallurgy.common.Metallurgy;
import leaf.cosmere.metallurgy.common.registries.MetallurgyItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class MetallurgyRecipeGen extends BaseRecipeProvider implements IConditionBuilder {
    public MetallurgyRecipeGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, existingFileHelper, Metallurgy.MODID);
    }

    @Override
    protected ResourceLocation makeRL(String path) {
        return Metallurgy.rl(path);
    }

    @Override
    protected void addRecipes(Consumer<FinishedRecipe> consumer) {
        // Iron Chisel Recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MetallurgyItems.IRON_CHISEL.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern(" I")
                .pattern("S ")
                .unlockedBy("has_material", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        // Diamond Chisel Recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MetallurgyItems.DIAMOND_CHISEL.get())
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('S', Tags.Items.RODS_WOODEN)
                .pattern(" D")
                .pattern("S ")
                .unlockedBy("has_material", has(Tags.Items.GEMS_DIAMOND))
                .save(consumer);

        // Netherite Chisel Recipe (smithing table upgrade from diamond)
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(MetallurgyItems.DIAMOND_CHISEL.get()),
                Ingredient.of(Tags.Items.INGOTS_NETHERITE),
                RecipeCategory.TOOLS,
                MetallurgyItems.NETHERITE_CHISEL.get())
                .unlocks("has_netherite_ingot", has(Tags.Items.INGOTS_NETHERITE))
                .save(consumer, Metallurgy.rl("netherite_chisel_smithing"));
    }
}
