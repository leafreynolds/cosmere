package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.common.registration.impl.RecipeSerializerDeferredRegister;
import leaf.cosmere.common.registration.impl.RecipeSerializerRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.recipes.ShardplateChargingRecipe;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class SurgebindingRecipes
{
	public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(Surgebinding.MODID);

	public static final RecipeSerializerRegistryObject<ShardplateChargingRecipe> PLATE_CHARGE =
			RECIPE_SERIALIZERS.register("crafting_plate_charge", () -> new SimpleCraftingRecipeSerializer<>(ShardplateChargingRecipe::new));
}
