package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.registries.RegistryObject;

public class POITypeRegistryObject<POIType> extends WrappedRegistryObject<PoiType>
{

	public POITypeRegistryObject(RegistryObject<PoiType> registryObject)
	{
		super(registryObject);
	}
}