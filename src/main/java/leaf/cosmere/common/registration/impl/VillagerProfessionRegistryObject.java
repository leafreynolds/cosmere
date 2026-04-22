package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.neoforged.neoforge.registries.DeferredHolder;

public class VillagerProfessionRegistryObject<VillagerProfession> extends WrappedRegistryObject<VillagerProfession>
{

	public VillagerProfessionRegistryObject(DeferredHolder<?, VillagerProfession> registryObject)
	{
		super(registryObject);
	}
}
