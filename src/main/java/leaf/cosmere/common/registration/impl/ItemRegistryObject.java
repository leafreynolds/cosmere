package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class ItemRegistryObject<ITEM extends Item> extends WrappedRegistryObject<ITEM> implements IItemProvider
{

	public ItemRegistryObject(RegistryObject<ITEM> registryObject)
	{
		super(registryObject);
	}

	@NotNull
	@Override
	public ITEM asItem()
	{
		return get();
	}

	public static <T extends Item> Collection<T> asItemList(Collection<ItemRegistryObject<T>> registryCollection)
	{
		Collection<T> out = new ArrayList<T>(registryCollection.size());
		for(ItemRegistryObject<T> object : registryCollection)
		{
			out.add(object.asItem());
		}
		return out;
	}

}