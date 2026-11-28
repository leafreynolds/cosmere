package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public class StatRegistryObject extends WrappedRegistryObject<ResourceLocation>
{

	public StatRegistryObject(DeferredHolder<? super ResourceLocation, ResourceLocation> registryObject)
	{
		super(registryObject);
	}
}
