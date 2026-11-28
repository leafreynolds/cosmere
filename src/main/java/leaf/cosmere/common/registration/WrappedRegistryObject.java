package leaf.cosmere.common.registration;

import net.minecraft.core.Holder;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class WrappedRegistryObject<T> implements Supplier<T>, INamedEntry
{

	protected DeferredHolder<? super T, T> registryObject;

	protected WrappedRegistryObject(DeferredHolder<? super T, T> registryObject)
	{
		this.registryObject = registryObject;
	}

	@Override
	public T get()
	{
		return registryObject.get();
	}

	public DeferredHolder<? super T, T> getRegistryObject()
	{
		return registryObject;
	}

	public Holder<? super T> getHolder()
	{
		return registryObject;
	}

	@Override
	public String getInternalRegistryName()
	{
		return registryObject.getId().getPath();
	}
}
