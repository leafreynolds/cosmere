package leaf.cosmere.common.registration;

import leaf.cosmere.common.Cosmere;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class WrappedDeferredRegister<T>
{
	protected final DeferredRegister<T> internal;
	protected final String modid;

	protected WrappedDeferredRegister(DeferredRegister<T> internal, String modid)
	{
		this.internal = internal;
		this.modid = modid;
	}

	protected WrappedDeferredRegister(String modid, Registry<T> registry)
	{
		this(DeferredRegister.create(registry, modid), modid);
	}

	protected WrappedDeferredRegister(String modid, ResourceKey<? extends Registry<T>> registryName)
	{
		this(DeferredRegister.create(registryName, modid), modid);
	}

	protected <I extends T, W extends WrappedRegistryObject<I>> W register(String name, Supplier<? extends I> sup, Function<DeferredHolder<T, I>, W> objectWrapper)
	{
		return objectWrapper.apply(internal.register(name, sup));
	}

	public void register(IEventBus bus)
	{
		internal.register(bus);
	}

	public void createAndRegister(IEventBus bus)
	{
		createAndRegister(bus, UnaryOperator.identity());
	}

	public void createAndRegister(IEventBus bus, UnaryOperator<RegistryBuilder<T>> builderModifier)
	{
		bus.addListener(NewRegistryEvent.class, event -> {
			RegistryBuilder<T> builder = builderModifier.apply(new RegistryBuilder<>(internal.getRegistryKey()));
			event.register(builder.create());
		});
	}

	public void createAndRegisterManifestation(IEventBus bus)
	{
		createAndRegister(bus, builder -> builder.defaultKey(Cosmere.rl("none")));
	}
}
