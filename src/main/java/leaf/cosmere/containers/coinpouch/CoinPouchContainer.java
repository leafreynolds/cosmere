/*
 * File created ~ 21 - 5 - 2022 ~ Leaf
 */

package leaf.cosmere.containers.coinpouch;

import leaf.cosmere.Cosmere;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.NotNull;

public class CoinPouchContainer extends AbstractContainerMenu
{
	@ObjectHolder(registryName = "coin_pouch_container", value = (Cosmere.MODID + ":coin_pouch"))
	public static MenuType<CoinPouchContainer> TYPE;

	public static CoinPouchContainer fromNetwork(int windowId, Inventory inv, FriendlyByteBuf buf)
	{
		InteractionHand hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new CoinPouchContainer(windowId, inv, inv.player.getItemInHand(hand));
	}

	private final ItemStack pouch;

	public CoinPouchContainer(int windowId, Inventory playerInv, ItemStack pouch)
	{
		super(TYPE, windowId);
		this.pouch = pouch;
		int i;
		int j;

		IItemHandlerModifiable pouchInv = (IItemHandlerModifiable) pouch.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);

		CompoundTag nbt = playerInv.getSelected().getOrCreateTag();

		int invStart = 0;

		//Bag inventory slots
		for (i = 0; i < 2; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				int k = j + i * 9;
				addSlot(new CoinPouchSlot(pouchInv, k, 8 + j * 18, 18 + i * 18));
			}
		}

		//Player Inventory slots	
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 41 + 17 + (i + invStart) * 18));
			}
		}

		//Player hotbar slots
		for (i = 0; i < 9; ++i)
		{
			addSlot(new Slot(playerInv, i, 8 + i * 18, 45 + 17 + (3 + invStart) * 18));
		}

	}

	@Override
	public boolean stillValid(@NotNull Player player)
	{
		ItemStack main = player.getMainHandItem();
		ItemStack off = player.getOffhandItem();
		return !main.isEmpty() && main == pouch || !off.isEmpty() && off == pouch;
	}

	@Override
	public ItemStack quickMoveStack(@NotNull Player playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		CompoundTag nbt = pouch.getOrCreateTag();
		int maxSlots = 18;

		Slot slot = this.slots.get(index);
		if (slot.hasItem())
		{
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (index < maxSlots)
			{
				if (!this.moveItemStackTo(itemstack1, maxSlots, this.slots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.moveItemStackTo(itemstack1, 0, maxSlots, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
			{
				slot.set(ItemStack.EMPTY);
			}
			else
			{
				slot.setChanged();
			}
		}
		return itemstack;
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
