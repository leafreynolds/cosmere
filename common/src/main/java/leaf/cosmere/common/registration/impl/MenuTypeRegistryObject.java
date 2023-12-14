package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeRegistryObject<MENU_TYPE extends MenuType<?>> extends WrappedRegistryObject<MENU_TYPE>
{
	public MenuTypeRegistryObject(RegistryObject<MENU_TYPE> registryObject)
	{
		super(registryObject);
	}
}