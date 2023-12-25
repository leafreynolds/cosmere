/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.common.registration.impl.MenuTypeDeferredRegister;
import leaf.cosmere.common.registration.impl.MenuTypeRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.blocks.entities.SandSpreader.SandSpreaderMenu;
import leaf.cosmere.sandmastery.common.items.sandpouch.SandPouchContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class SandmasteryMenuTypes
{
	public static final MenuTypeDeferredRegister MENU_TYPES = new MenuTypeDeferredRegister(Sandmastery.MODID);
	public static final MenuTypeRegistryObject<MenuType<?>> SAND_POUCH = MENU_TYPES.register(
			"sand_pouch",
			() -> IForgeMenuType.create(SandPouchContainerMenu::fromNetwork));
	public static final MenuTypeRegistryObject<MenuType<?>> SAND_SPREADER = MENU_TYPES.register(
			"sand_spreader",
			() -> IForgeMenuType.create(SandSpreaderMenu::new));

}
