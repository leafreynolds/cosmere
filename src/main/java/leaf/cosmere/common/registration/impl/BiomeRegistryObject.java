package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.RegistryObject;

public class BiomeRegistryObject<BIOME extends Biome> extends WrappedRegistryObject<BIOME>
{

	public BiomeRegistryObject(RegistryObject<BIOME> registryObject)
	{
		super(registryObject);
	}
}