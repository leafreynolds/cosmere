package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.neoforged.neoforge.registries.DeferredHolder;

public class POITypeRegistryObject<POIType> extends WrappedRegistryObject<POIType>
{

	public POITypeRegistryObject(DeferredHolder<? super POIType, POIType> registryObject)
	{
		super(registryObject);
	}
}
