/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.example.common.registries;

import leaf.cosmere.common.registration.impl.RecipeSerializerDeferredRegister;
import leaf.cosmere.example.common.Example;

// for the non-json based recipes
public class ExampleRecipes
{
	public static final RecipeSerializerDeferredRegister SPECIAL_RECIPES = new RecipeSerializerDeferredRegister(Example.MODID);

}
