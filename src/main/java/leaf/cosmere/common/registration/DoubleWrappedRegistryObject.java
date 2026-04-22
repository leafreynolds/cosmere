package leaf.cosmere.common.registration;

import net.neoforged.neoforge.registries.DeferredHolder;

public class DoubleWrappedRegistryObject<PRIMARY, SECONDARY> implements INamedEntry
{

	protected final DeferredHolder<?, PRIMARY> primaryRO;
	protected final DeferredHolder<?, SECONDARY> secondaryRO;

	public DoubleWrappedRegistryObject(DeferredHolder<?, PRIMARY> primaryRO, DeferredHolder<?, SECONDARY> secondaryRO)
	{
		this.primaryRO = primaryRO;
		this.secondaryRO = secondaryRO;
	}

	public PRIMARY getPrimary()
	{
		return primaryRO.get();
	}

	public SECONDARY getSecondary()
	{
		return secondaryRO.get();
	}

	@Override
	public String getInternalRegistryName()
	{
		return primaryRO.getId().getPath();
	}
}
