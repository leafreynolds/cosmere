/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.items.sandpouch;

import leaf.cosmere.sandmastery.common.items.SandPouchItem;
import leaf.cosmere.sandmastery.common.registries.SandmasteryDataComponents;
import leaf.cosmere.sandmastery.common.registries.SandmasteryMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import org.jetbrains.annotations.NotNull;

public class SandPouchContainerMenu extends AbstractContainerMenu
{
	public static SandPouchContainerMenu fromNetwork(int windowId, Inventory inv, FriendlyByteBuf buf)
	{
		InteractionHand hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new SandPouchContainerMenu(windowId, inv, inv.player.getItemInHand(hand));
	}

	public static final int BUTTON_TOGGLE_COLOUR = 0;

	private final ItemStack pouch;
	private final boolean clientSide;

	// synced to the client so the screen never reads its own (possibly stale) copy of the pouch stack
	private final DataSlot showingBlack = DataSlot.standalone();
	private final DataSlot blackLayers = DataSlot.standalone();
	private final DataSlot whiteLayers = DataSlot.standalone();

	public SandPouchContainerMenu(int windowId, Inventory playerInv, ItemStack pouch)
	{
		super(SandmasteryMenuTypes.SAND_POUCH.get(), windowId);
		this.pouch = pouch;
		this.clientSide = playerInv.player.level().isClientSide();
		addDataSlot(showingBlack);
		addDataSlot(blackLayers);
		addDataSlot(whiteLayers);
		// so the first sync on open already carries real values
		refreshSyncedData();

		// always the pouch in the main hand as it is now, never the stack object captured at open time
		IItemHandlerModifiable pouchInv = new SandpouchItemHandler(() ->
		{
			ItemStack held = playerInv.player.getMainHandItem();
			return held.getItem() instanceof SandPouchItem ? held : ItemStack.EMPTY;
		});

		// Pouch Slots
		addSlot(new SandPouchSlot(pouchInv, 0, 43, 18, true, clientSide));
		addSlot(new SandPouchSlot(pouchInv, 1, 80, 18, false, clientSide));
		addSlot(new SandPouchSlot(pouchInv, 2, 98, 18, false, clientSide));

		// Player inventory
		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 9; col++)
				addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 49 + row * 18));
		// Hotbar
		for (int col = 0; col < 9; col++)
			addSlot(new Slot(playerInv, col, 8 + col * 18, 107));
	}

	@Override
	public void broadcastChanges()
	{
		refreshSyncedData();
		super.broadcastChanges();
	}

	private void refreshSyncedData()
	{
		if (!clientSide)
		{
			SandpouchItemHandler handler = new SandpouchItemHandler(pouch);
			showingBlack.set(handler.getDisplayingBlackSand() ? 1 : 0);
			whiteLayers.set(handler.getChargedLayers());
			blackLayers.set(handler.getTotalLayers() - handler.getChargedLayers());
		}
	}

	@Override
	public boolean clickMenuButton(@NotNull Player player, int id)
	{
		// the output slots change under the cursor when the colour flips, so wait until it is empty
		if (id != BUTTON_TOGGLE_COLOUR || !getCarried().isEmpty())
		{
			return false;
		}
		pouch.set(SandmasteryDataComponents.DISPLAYING_BLACK_SAND.get(), !new SandpouchItemHandler(pouch).getDisplayingBlackSand());
		return true;
	}

	public boolean isShowingBlack()
	{
		return showingBlack.get() != 0;
	}

	// the colour the output slots are not showing has sand, the selected one has none
	public boolean shouldHighlightToggle()
	{
		return isShowingBlack() ? blackLayers.get() <= 0 && whiteLayers.get() > 0
		                        : whiteLayers.get() <= 0 && blackLayers.get() > 0;
	}

	@Override
	public ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex)
	{
		ItemStack itemStack = ItemStack.EMPTY;

		// TODO: make shift clicking actually work.. right now it duplicates the crap out of the sand. Commented out in repo until fixed
//        int maxSlots = SandpouchItemHandler.SIZE;
//
//        Slot slot = this.slots.get(pIndex);
//        if(slot.hasItem()) {
//            ItemStack itemStack1 = slot.getItem();
//            itemStack = itemStack1.copy();
//
//            if(pIndex < maxSlots) {
//                if(!this.moveItemStackTo(itemStack1, maxSlots, this.slots.size(), true)) {
//                    return ItemStack.EMPTY;
//                };
//            } else if(!this.moveItemStackTo(itemStack1, 0, maxSlots, false)) {
//                return ItemStack.EMPTY;
//            }
//
//            if (itemStack1.isEmpty())
//            {
//                slot.set(ItemStack.EMPTY);
//            }
//            else
//            {
//                slot.setChanged();
//            }
//        }

		return itemStack;
	}

	@Override
	public boolean stillValid(@NotNull Player pPlayer)
	{
		ItemStack main = pPlayer.getMainHandItem();
		ItemStack off = pPlayer.getOffhandItem();
		return !main.isEmpty() && main == pouch || !off.isEmpty() && off == pouch;
	}

	@Override
	public void clicked(int slot, int dragType, @NotNull ClickType clickTypeIn, @NotNull Player player)
	{
		if (!(slot >= 0 && getSlot(slot).getItem() == player.getItemInHand(InteractionHand.MAIN_HAND)))
		{
			super.clicked(slot, dragType, clickTypeIn, player);
		}
	}
}
