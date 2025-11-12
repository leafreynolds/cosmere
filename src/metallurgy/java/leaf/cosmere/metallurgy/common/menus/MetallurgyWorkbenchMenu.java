package leaf.cosmere.metallurgy.common.menus;

import leaf.cosmere.metallurgy.common.blocks.entities.MetallurgyWorkbenchBE;
import leaf.cosmere.metallurgy.common.registries.MetallurgyBlocks;
import leaf.cosmere.metallurgy.common.registries.MetallurgyMenuTypes;
import net.minecraft.core.BlockPos;
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

public class MetallurgyWorkbenchMenu extends AbstractContainerMenu {
	public final MetallurgyWorkbenchBE blockEntity;
	private final Level level;
	private final ContainerData data;

	public MetallurgyWorkbenchMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
	}

	public MetallurgyWorkbenchMenu(int id, Inventory inv, BlockEntity entity) {
		this(id, inv, entity, new SimpleContainerData(2));
	}

	public MetallurgyWorkbenchMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
		super(MetallurgyMenuTypes.METALLURGY_WORKBENCH.get(), id);
		checkContainerSize(inv, 3);
		this.blockEntity = (MetallurgyWorkbenchBE) entity;
		this.level = inv.player.level();
		this.data = data;

		addDataSlots(this.data);

		IItemHandlerModifiable blockInv = (IItemHandlerModifiable) this.blockEntity
				.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);

		// Slot 0: Input
		addSlot(new SlotItemHandler(blockInv, 0, 8, 89));

		// Slot 1: Output
		addSlot(new SlotItemHandler(blockInv, 1, 152, 89) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
		});

		// Slot 2: Chisel
		addSlot(new SlotItemHandler(blockInv, 2, 8, 16));

		int playerInventoryOffset = 110;
		// Player Inventory
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, playerInventoryOffset + i * 18));
			}
		}

		// Player Hotbar
		for (int i = 0; i < 9; ++i) {
			addSlot(new Slot(inv, i, 8 + i * 18, playerInventoryOffset + 58));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		int workbenchSlots = 3;

		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (index < workbenchSlots) {
				if (!this.moveItemStackTo(itemstack1, workbenchSlots, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (itemstack1.getItem() instanceof leaf.cosmere.metallurgy.common.items.MetallurgistChiselItem) {
					if (!this.moveItemStackTo(itemstack1, 2, 3, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return itemstack;
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return stillValid(ContainerLevelAccess.create(this.level, this.blockEntity.getBlockPos()),
				pPlayer, MetallurgyBlocks.METALLURGY_WORKBENCH.getBlock());
	}

	public BlockPos getBlockPos() {
		return this.blockEntity.getBlockPos();
	}

	public int getCutPercentage() {
		return this.data.get(0);
	}

	public void setCutPercentage(int percentage) {
		this.data.set(0, Math.max(5, Math.min(95, percentage)));
		this.data.set(1, 1);
	}

	public boolean hasCutLineSet() {
		return this.data.get(1) != 0;
	}

	public void clearCutLine() {
		this.data.set(1, 0);
	}
}
