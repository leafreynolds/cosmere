package leaf.cosmere.metallurgy.common.registries;

import leaf.cosmere.common.registration.impl.MenuTypeDeferredRegister;
import leaf.cosmere.common.registration.impl.MenuTypeRegistryObject;
import leaf.cosmere.metallurgy.common.Metallurgy;
import leaf.cosmere.metallurgy.common.menus.MetallurgyWorkbenchMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class MetallurgyMenuTypes
{
	public static final MenuTypeDeferredRegister MENU_TYPES = new MenuTypeDeferredRegister(Metallurgy.MODID);
	
	public static final MenuTypeRegistryObject<MenuType<?>> METALLURGY_WORKBENCH = MENU_TYPES.register(
			"metallurgy_workbench",
			() -> IForgeMenuType.create(MetallurgyWorkbenchMenu::new));
}
