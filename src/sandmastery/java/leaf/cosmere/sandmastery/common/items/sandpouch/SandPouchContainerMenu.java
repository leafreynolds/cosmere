/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.items.sandpouch;

import leaf.cosmere.sandmastery.common.items.SandPouchItem;
import leaf.cosmere.sandmastery.common.registries.SandmasteryMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class SandPouchContainerMenu extends AbstractContainerMenu
{
	public static SandPouchContainerMenu fromNetwork(int windowId, Inventory inv, FriendlyByteBuf buf)
	{
		InteractionHand hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new SandPouchContainerMenu(windowId, inv, inv.player.getItemInHand(hand));
	}

	private final ItemStack pouch;

	public SandPouchContainerMenu(int windowId, Inventory playerInv, ItemStack pouch)
	{
		super(SandmasteryMenuTypes.SAND_POUCH.get(), windowId);
		this.pouch = pouch;

		IItemHandlerModifiable pouchInv = SandPouchItem.getPouchInv(pouch);
		if (pouchInv == null)
		{
			//the held stack is not a pouch item?
			// hand changed between opening the menu and syncing it
			pouchInv = new ItemStackHandler(SandpouchItemHandler.SIZE);
		}

		int invStart = 0;

		// Pouch Slots
		for (int j = 0; j < SandpouchItemHandler.SIZE; ++j)
		{
			boolean input = j == SandpouchItemHandler.SLOT_INPUT;
			addSlot(new SandPouchSlot(pouchInv, j, 8 + j * 18, 18, input));
		}

		// Player Inventory
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 41 + 17 + (i + invStart) * 18));
			}
		}

		// Player Hotbar
		for (int i = 0; i < 9; ++i)
		{
			addSlot(new Slot(playerInv, i, 8 + i * 18, 45 + 17 + (3 + invStart) * 18));
		}
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
