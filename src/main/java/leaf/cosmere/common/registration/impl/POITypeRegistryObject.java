package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class POITypeRegistryObject<POIType> extends WrappedRegistryObject<PoiType>
{

	public POITypeRegistryObject(DeferredHolder<? super PoiType, PoiType> registryObject)
	{
		super(registryObject);
	}
}
