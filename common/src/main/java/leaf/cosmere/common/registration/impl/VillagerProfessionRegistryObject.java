package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraftforge.registries.RegistryObject;

public class VillagerProfessionRegistryObject<VillagerProfession> extends WrappedRegistryObject<VillagerProfession>
{

	public VillagerProfessionRegistryObject(RegistryObject<VillagerProfession> registryObject)
	{
		super(registryObject);
	}
}