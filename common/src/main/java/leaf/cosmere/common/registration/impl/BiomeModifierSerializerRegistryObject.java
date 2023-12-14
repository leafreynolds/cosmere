package leaf.cosmere.common.registration.impl;

import com.mojang.serialization.Codec;
import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.RegistryObject;

public class BiomeModifierSerializerRegistryObject<T extends BiomeModifier> extends WrappedRegistryObject<Codec<T>>
{

	public BiomeModifierSerializerRegistryObject(RegistryObject<Codec<T>> registryObject)
	{
		super(registryObject);
	}
}