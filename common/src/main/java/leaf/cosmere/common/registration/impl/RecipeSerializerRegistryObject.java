package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSerializerRegistryObject<RECIPE_SERIALIZER extends RecipeSerializer<?>> extends WrappedRegistryObject<RECIPE_SERIALIZER>
{
	public RecipeSerializerRegistryObject(RegistryObject<RECIPE_SERIALIZER> registryObject)
	{
		super(registryObject);
	}
}