/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.blocks.entities.SandSpreader;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.sandmastery.common.blocks.entities.SandSpreaderBE;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.registries.SandmasteryMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class SandSpreaderMenu extends AbstractContainerMenu
{
	public final SandSpreaderBE blockEntity;
	private final Level level;
	private final ContainerData data;

	public SandSpreaderMenu(int id, Inventory inv, FriendlyByteBuf extraData)
	{
		this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
	}

	public SandSpreaderMenu(int id, Inventory inv, BlockEntity entity, ContainerData data)
	{
		super(SandmasteryMenuTypes.SAND_SPREADER.get(), id);
		checkContainerSize(inv, 3);
		this.blockEntity = (SandSpreaderBE) entity;
		this.level = inv.player.level();
		this.data = data;
		IItemHandlerModifiable blockInv = (IItemHandlerModifiable) this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
		int invStart = 0;
		int i;
		int j;
		CosmereAPI.logger.debug(String.valueOf(blockInv.getSlots()));

		// Block Slots
		for (i = 0; i < 2; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				int k = j + i * 9;
				addSlot(new SlotItemHandler(blockInv, k, 8 + j * 18, 18 + i * 18));
			}
		}

		// Player Inventory
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 41 + 17 + (i + invStart) * 18));
			}
		}

		// Player Hotbar
		for (i = 0; i < 9; ++i)
		{
			addSlot(new Slot(inv, i, 8 + i * 18, 45 + 17 + (3 + invStart) * 18));
		}

		addDataSlots(data);
	}

	public boolean isCrafting()
	{
		return data.get(0) > 0;
	}

	public int getScaledProgress()
	{
		int progress = this.data.get(0);
		int maxProgress = this.data.get(1);  // Max Progress
		int progressArrowSize = 26; // This is the height in pixels of your arrow

		return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index)
	{

		ItemStack itemstack = ItemStack.EMPTY;
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
	public boolean stillValid(Player pPlayer)
	{
		return stillValid(ContainerLevelAccess.create(this.level, this.blockEntity.getBlockPos()), pPlayer, SandmasteryBlocks.SAND_SPREADING_TUB_BLOCK.getBlock());
	}
}
