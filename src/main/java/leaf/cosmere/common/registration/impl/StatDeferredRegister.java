package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class StatDeferredRegister extends WrappedDeferredRegister<ResourceLocation>
{
	public StatDeferredRegister(String modid)
	{
		super(modid, Registry.CUSTOM_STAT_REGISTRY);
	}


	public StatRegistryObject register(String name)
	{
		return register(name, () -> new ResourceLocation(name));
	}

	public StatRegistryObject register(String name, Supplier<ResourceLocation> supplier)
	{
		return register(name, supplier, StatRegistryObject::new);
	}

}