package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.RegistryObject;

public class PlacedFeatureRegistryObject<PLACED_FEATURE extends PlacedFeature> extends WrappedRegistryObject<PLACED_FEATURE>
{

	public PlacedFeatureRegistryObject(RegistryObject<PLACED_FEATURE> registryObject)
	{
		super(registryObject);
	}
}