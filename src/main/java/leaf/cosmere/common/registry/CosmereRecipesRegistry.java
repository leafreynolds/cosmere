package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.recipes.GodMetalNuggetsCompress;
import leaf.cosmere.common.recipes.GodMetalNuggetsDecompress;
import leaf.cosmere.common.recipes.GodMetalAlloyNuggetRecipe;
import leaf.cosmere.common.registration.impl.RecipeSerializerDeferredRegister;
import leaf.cosmere.common.registration.impl.RecipeSerializerRegistryObject;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class CosmereRecipesRegistry
{
	public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(Cosmere.MODID);

	public static final RecipeSerializerRegistryObject<GodMetalAlloyNuggetRecipe> GOD_METAL_ALLOY_NUGGET_RECIPE =
			RECIPE_SERIALIZERS.register("crafting_god_metal_alloy_nugget_recipe",
					() -> new SimpleCraftingRecipeSerializer<>(GodMetalAlloyNuggetRecipe::new));

	public static final RecipeSerializerRegistryObject<GodMetalNuggetsCompress> GOD_METAL_ALLOY_NUGGET_COMPRESS =
			RECIPE_SERIALIZERS.register("crafting_god_metal_alloy_nugget_compress",
					() -> new SimpleCraftingRecipeSerializer<>(GodMetalNuggetsCompress::new));

	public static final RecipeSerializerRegistryObject<GodMetalNuggetsDecompress> GOD_METAL_ALLOY_NUGGET_DECOMPRESS =
			RECIPE_SERIALIZERS.register("crafting_god_metal_alloy_nugget_decompress",
					() -> new SimpleCraftingRecipeSerializer<>(GodMetalNuggetsDecompress::new));
}
