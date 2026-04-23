package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class ItemRegistryObject<ITEM extends Item> extends WrappedRegistryObject<ITEM> implements IItemProvider
{

	public ItemRegistryObject(DeferredHolder<? super ITEM, ITEM> registryObject)
	{
		super(registryObject);
	}

	@NotNull
	@Override
	public ITEM asItem()
	{
		return get();
	}
}
