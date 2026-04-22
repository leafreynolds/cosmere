package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.registries.DeferredHolder;

public class BiomeRegistryObject<BIOME extends Biome> extends WrappedRegistryObject<BIOME>
{

	public BiomeRegistryObject(DeferredHolder<?, BIOME> registryObject)
	{
		super(registryObject);
	}
}
