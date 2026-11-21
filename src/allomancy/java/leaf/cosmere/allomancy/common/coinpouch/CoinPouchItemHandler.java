/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.allomancy.common.coinpouch;

import leaf.cosmere.allomancy.common.items.CoinPouchItem;
import leaf.cosmere.allomancy.common.registries.AllomancyDataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ComponentItemHandler;

//pouch inventory, stored on the stack as the COIN_POUCH_CONTENTS component
public class CoinPouchItemHandler extends ComponentItemHandler
{
	public static final int SIZE = 18;

	public CoinPouchItemHandler(ItemStack pouch)
	{
		super(pouch, AllomancyDataComponents.COIN_POUCH_CONTENTS.get(), SIZE);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack)
	{
		return !stack.isEmpty() && CoinPouchItem.SUPPORTED_PROJECTILES.test(stack);
	}
}
