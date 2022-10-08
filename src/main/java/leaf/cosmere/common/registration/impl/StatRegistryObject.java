package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class StatRegistryObject extends WrappedRegistryObject<ResourceLocation>
{

	public StatRegistryObject(RegistryObject<ResourceLocation> registryObject)
	{
		super(registryObject);
	}
}