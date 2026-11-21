package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class MenuTypeDeferredRegister extends WrappedDeferredRegister<MenuType<?>>
{

	public MenuTypeDeferredRegister(String modid)
	{
		super(modid, Registries.MENU);
	}

	public MenuTypeRegistryObject<MenuType<?>> register(String name, Supplier<MenuType<?>> sup)
	{
		return register(name, sup, MenuTypeRegistryObject::new);
	}
}
