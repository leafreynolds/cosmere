package leaf.cosmere.common.registration;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class DoubleDeferredRegister<PRIMARY, SECONDARY>
{

	private final DeferredRegister<PRIMARY> primaryRegister;
	private final DeferredRegister<SECONDARY> secondaryRegister;

	public DoubleDeferredRegister(DeferredRegister<PRIMARY> primaryRegistry, DeferredRegister<SECONDARY> secondaryRegistry)
	{
		this.primaryRegister = primaryRegistry;
		this.secondaryRegister = secondaryRegistry;
	}

	public DoubleDeferredRegister(String modid, Registry<PRIMARY> primaryRegistry, Registry<SECONDARY> secondaryRegistry)
	{
		this(DeferredRegister.create(primaryRegistry, modid), DeferredRegister.create(secondaryRegistry, modid));
	}

	protected DoubleDeferredRegister(String modid, ResourceKey<? extends Registry<PRIMARY>> primaryRegistryName,
	                                 ResourceKey<? extends Registry<SECONDARY>> secondaryRegistryName)
	{
		this(DeferredRegister.create(primaryRegistryName, modid), DeferredRegister.create(secondaryRegistryName, modid));
	}

	public <P extends PRIMARY, S extends SECONDARY, W extends DoubleWrappedRegistryObject<P, S>> W register(String name, Supplier<? extends P> primarySupplier,
	                                                                                                        Supplier<? extends S> secondarySupplier, BiFunction<DeferredHolder<PRIMARY, P>, DeferredHolder<SECONDARY, S>, W> objectWrapper)
	{
		return objectWrapper.apply(primaryRegister.register(name, primarySupplier), secondaryRegister.register(name, secondarySupplier));
	}

	public <P extends PRIMARY, S extends SECONDARY, W extends DoubleWrappedRegistryObject<P, S>> W register(String name, Supplier<? extends P> primarySupplier,
	                                                                                                        Function<P, S> secondarySupplier, BiFunction<DeferredHolder<PRIMARY, P>, DeferredHolder<SECONDARY, S>, W> objectWrapper)
	{
		return registerAdvanced(name, primarySupplier, holder -> secondarySupplier.apply(holder.get()), objectWrapper);
	}

	public <P extends PRIMARY, S extends SECONDARY, W extends DoubleWrappedRegistryObject<P, S>> W registerAdvanced(String name, Supplier<? extends P> primarySupplier,
	                                                                                                                Function<DeferredHolder<PRIMARY, P>, S> secondarySupplier, BiFunction<DeferredHolder<PRIMARY, P>, DeferredHolder<SECONDARY, S>, W> objectWrapper)
	{
		DeferredHolder<PRIMARY, P> primaryObject = primaryRegister.register(name, primarySupplier);
		return objectWrapper.apply(primaryObject, secondaryRegister.register(name, () -> secondarySupplier.apply(primaryObject)));
	}

	public void register(IEventBus bus)
	{
		primaryRegister.register(bus);
		secondaryRegister.register(bus);
	}
}
