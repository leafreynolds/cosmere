package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class RecipeSerializerDeferredRegister extends WrappedDeferredRegister<RecipeSerializer<?>>
{

	public RecipeSerializerDeferredRegister(String modid)
	{
		super(modid, ForgeRegistries.RECIPE_SERIALIZERS);
	}

	public RecipeSerializerRegistryObject<RecipeSerializer<?>> register(String name, Supplier<RecipeSerializer<?>> sup)
	{
		return register(name, sup, RecipeSerializerRegistryObject::new);
	}
}