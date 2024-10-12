/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.recipes.VialMixingRecipe;
import leaf.cosmere.common.registration.impl.RecipeSerializerDeferredRegister;
import leaf.cosmere.common.registration.impl.RecipeSerializerRegistryObject;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

// for the non-json based recipes
public class AllomancyRecipes
{
	public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(Allomancy.MODID);

	public static final RecipeSerializerRegistryObject<VialMixingRecipe> VIAL_MIX =
			RECIPE_SERIALIZERS.register("crafting_vial_mix", () -> new SimpleCraftingRecipeSerializer<>(VialMixingRecipe::new));
}
