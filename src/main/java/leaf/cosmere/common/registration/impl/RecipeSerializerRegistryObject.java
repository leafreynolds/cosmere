/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;

public class RecipeSerializerRegistryObject<RECIPE extends Recipe<?>> extends WrappedRegistryObject<RecipeSerializer<RECIPE>>
{
	public RecipeSerializerRegistryObject(DeferredHolder<? super RecipeSerializer<RECIPE>, RecipeSerializer<RECIPE>> registryObject)
	{
		super(registryObject);
	}
}
