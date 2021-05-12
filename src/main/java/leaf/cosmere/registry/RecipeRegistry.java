/*
 * File created ~ 10 - 5 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.recipes.VialMixingRecipe;
import net.minecraft.item.crafting.FireworkStarRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// for the non-json based recipes
public class RecipeRegistry
{
    public static final DeferredRegister<IRecipeSerializer<?>> SPECIAL_RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Cosmere.MODID);


    //not sure why I can't copy how minecraft fireworks do their serializer, but this works.
    public static class VialMixingRecipeSerializer extends SpecialRecipeSerializer<VialMixingRecipe>
    {
        public VialMixingRecipeSerializer() {
            super(VialMixingRecipe::new);
        }
    }
    public static final RegistryObject<SpecialRecipeSerializer<VialMixingRecipe>> VIAL_RECIPE_SERIALIZER = SPECIAL_RECIPES.register("vial_mix", VialMixingRecipeSerializer::new);

}
