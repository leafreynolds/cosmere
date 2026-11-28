/*
 * File created ~ 26 - 11 - 2026 ~ Leaf
 */

package leaf.cosmere.allomancy.gametest;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.coinpouch.CoinPouchContainerMenu;
import leaf.cosmere.allomancy.common.items.CoinPouchItem;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.ItemsRegistry;
import leaf.cosmere.gametest.HarnessGameTests;
import net.minecraft.core.NonNullList;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

//opening the pouch and moving coins in and out shouldn't crash
@GameTestHolder(Allomancy.MODID)
@PrefixGameTestTemplate(false)
public class CoinPouchGameTests
{
	@GameTest(template = HarnessGameTests.FLOOR, templateNamespace = HarnessGameTests.FLOOR_NAMESPACE)
	public static void pouchMenuAcceptsEmptySlotSync(GameTestHelper helper)
	{
		CoinPouchContainerMenu menu = openPouch(helper);

		//same as the client applying the server's slot sync when the pouch opens, empty slots and all
		menu.initializeContents(0, NonNullList.withSize(menu.slots.size(), ItemStack.EMPTY), ItemStack.EMPTY);
		helper.succeed();
	}

	@GameTest(template = HarnessGameTests.FLOOR, templateNamespace = HarnessGameTests.FLOOR_NAMESPACE)
	public static void coinsGoInAndComeBackOut(GameTestHelper helper)
	{
		Slot pouchSlot = openPouch(helper).getSlot(0);
		ItemStack coins = new ItemStack(ItemsRegistry.METAL_NUGGETS.get(Metals.MetalType.COPPER).asItem(), 5);
		helper.assertTrue(CoinPouchItem.SUPPORTED_PROJECTILES.test(coins), "copper nuggets should count as coins");

		helper.assertTrue(pouchSlot.mayPlace(coins), "pouch slot refused copper nuggets");
		pouchSlot.set(coins.copy());
		helper.assertTrue(pouchSlot.getItem().getCount() == 5, "pouch slot should hold 5 nuggets, has " + pouchSlot.getItem());

		//taking the last coin out writes an empty stack back into the pouch
		pouchSlot.set(ItemStack.EMPTY);
		helper.assertTrue(pouchSlot.getItem().isEmpty(), "pouch slot should be empty, has " + pouchSlot.getItem());
		helper.succeed();
	}

	@GameTest(template = HarnessGameTests.FLOOR, templateNamespace = HarnessGameTests.FLOOR_NAMESPACE)
	public static void pouchRefusesNonCoins(GameTestHelper helper)
	{
		Slot pouchSlot = openPouch(helper).getSlot(0);
		helper.assertFalse(pouchSlot.mayPlace(new ItemStack(Items.DIAMOND)), "pouch slot accepted a diamond");
		helper.succeed();
	}

	private static CoinPouchContainerMenu openPouch(GameTestHelper helper)
	{
		Player player = helper.makeMockPlayer(GameType.SURVIVAL);
		ItemStack pouch = new ItemStack(AllomancyItems.COIN_POUCH.get());
		player.setItemInHand(InteractionHand.MAIN_HAND, pouch);
		return new CoinPouchContainerMenu(0, player.getInventory(), pouch);
	}
}
