package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MenuTypeRegistryObject<MENU_TYPE extends MenuType<?>> extends WrappedRegistryObject<MENU_TYPE>
{
	public MenuTypeRegistryObject(DeferredHolder<? super MENU_TYPE, MENU_TYPE> registryObject)
	{
		super(registryObject);
	}
}
