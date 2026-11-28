package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Supplier;

public class BiomeDeferredRegister extends WrappedDeferredRegister<Biome>
{

	public BiomeDeferredRegister(String modid)
	{
		super(modid, Registries.BIOME);
	}

	public <BIOME extends Biome> BiomeRegistryObject<BIOME> register(String name, Supplier<BIOME> sup)
	{
		return register(name, sup, BiomeRegistryObject::new);
	}
}
