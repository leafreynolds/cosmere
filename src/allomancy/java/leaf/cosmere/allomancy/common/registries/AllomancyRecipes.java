/*
 * File updated ~ 5 - 6 - 2024 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.recipes.VialMixingRecipe;
import leaf.cosmere.common.registration.impl.RecipeSerializerDeferredRegister;
import leaf.cosmere.common.registration.impl.RecipeSerializerRegistryObject;
import net.minecraft.world.item.crafting.RecipeSerializer;

// for the non-json based recipes
public class AllomancyRecipes
{
	public static final RecipeSerializerDeferredRegister SPECIAL_RECIPES = new RecipeSerializerDeferredRegister(Allomancy.MODID);

	public static final RecipeSerializerRegistryObject<RecipeSerializer<?>> VIAL_RECIPE_SERIALIZER =
			SPECIAL_RECIPES.register("crafting_vial_mix", VialMixingRecipe.Serializer::new);

}
