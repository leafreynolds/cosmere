/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.coinpouch.CoinPouchContainerMenu;
import leaf.cosmere.common.registration.impl.MenuTypeDeferredRegister;
import leaf.cosmere.common.registration.impl.MenuTypeRegistryObject;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class AllomancyMenuTypes
{
	public static final MenuTypeDeferredRegister MENU_TYPES = new MenuTypeDeferredRegister(Allomancy.MODID);
	public static final MenuTypeRegistryObject<MenuType<?>> COIN_POUCH = MENU_TYPES.register(
			"coin_pouch",
			() -> IMenuTypeExtension.create(CoinPouchContainerMenu::fromNetwork));

}
