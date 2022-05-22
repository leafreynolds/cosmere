/*
 * File created ~ 21 - 5 - 2022 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.client.gui.CoinPouchScreen;
import leaf.cosmere.containers.coinpouch.CoinPouchContainer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainersRegistry
{
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Cosmere.MODID);
	public static final RegistryObject<MenuType<?>> COIN_POUCH = CONTAINERS.register("coin_pouch", IForgeMenuType.create(CoinPouchContainer::fromNetwork).delegate);

	@OnlyIn(Dist.CLIENT)
	public static void registerGUIFactories()
	{
		MenuScreens.register((MenuType<CoinPouchContainer>) ContainersRegistry.COIN_POUCH.get(), CoinPouchScreen::new);
	}
}
