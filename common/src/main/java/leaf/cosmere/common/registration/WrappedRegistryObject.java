package leaf.cosmere.common.registration;

import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class WrappedRegistryObject<T> implements Supplier<T>, INamedEntry
{

	protected RegistryObject<T> registryObject;

	protected WrappedRegistryObject(RegistryObject<T> registryObject)
	{
		this.registryObject = registryObject;
	}

	@Override
	public T get()
	{
		return registryObject.get();
	}

	public RegistryObject<T> getRegistryObject()
	{
		return registryObject;
	}

	@Override
	public String getInternalRegistryName()
	{
		return registryObject.getId().getPath();
	}
}