/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.blocks.entities;

import leaf.cosmere.sandmastery.common.blocks.entities.SandSpreader.SandSpreaderMenu;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlockEntitiesRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SandSpreaderBE extends BlockEntity implements MenuProvider
{
	private final ItemStackHandler itemHandler = new ItemStackHandler(18)
	{
		@Override
		protected void onContentsChanged(int slot)
		{
			setChanged();
		}

		@Override
		public int getSlotLimit(int slot)
		{
			return 8;
		}
	};

	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

	protected final ContainerData data;
	private int progress = 0;
	private int maxProgress = 78;
	private int ticksSinceUpdate = 0;

	public SandSpreaderBE(BlockPos pPos, BlockState pBlockState)
	{
		super(SandmasteryBlockEntitiesRegistry.SAND_SPREADER_BE.get(), pPos, pBlockState);
		this.data = new ContainerData()
		{
			@Override
			public int get(int index)
			{
				return switch (index)
				{
					case 0 -> SandSpreaderBE.this.progress;
					case 1 -> SandSpreaderBE.this.maxProgress;
					default -> 0;
				};
			}

			@Override
			public void set(int index, int value)
			{
				switch (index)
				{
					case 0 -> SandSpreaderBE.this.progress = value;
					case 1 -> SandSpreaderBE.this.maxProgress = value;
				}
			}

			@Override
			public int getCount()
			{
				return 2;
			}
		};
	}

	@Override
	public Component getDisplayName()
	{
		return Component.literal("Sand Spreading Tub"); // TODO Translations
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer)
	{
		return new SandSpreaderMenu(pContainerId, pPlayerInventory, this, this.data);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if (cap == ForgeCapabilities.ITEM_HANDLER)
		{
			return lazyItemHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> itemHandler);
	}

	@Override
	public void invalidateCaps()
	{
		super.invalidateCaps();
		lazyItemHandler.invalidate();
	}

	@Override
	protected void saveAdditional(CompoundTag nbt)
	{
		nbt.put("inventory", itemHandler.serializeNBT());
		super.saveAdditional(nbt);
	}

	@Override
	public void load(CompoundTag nbt)
	{
		super.saveAdditional(nbt);
		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
	}

	public void drops()
	{
		SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
		for (int i = 0; i < itemHandler.getSlots(); i++)
		{
			inventory.setItem(i, itemHandler.getStackInSlot(i));
		}
		Containers.dropContents(this.level, this.worldPosition, inventory);
	}

	public boolean readyToUpdate()
	{
		this.ticksSinceUpdate++;
		if (this.ticksSinceUpdate >= 20)
		{
			this.ticksSinceUpdate = 0;
			return true;
		}
		return false;
	}

	public boolean hasCatalyst()
	{
		for (int i = 0; i < this.itemHandler.getSlots(); i++)
		{
			ItemStack item = this.itemHandler.getStackInSlot(i);
			if (item.getItem() == SandmasteryBlocks.TALDAIN_BLACK_SAND.asItem())
			{
				return true;
			}
			if (item.getItem() == SandmasteryBlocks.TALDAIN_WHITE_SAND.asItem())
			{
				return true;
			}
			if (item.getItem() == SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.asItem())
			{
				return true;
			}
			if (item.getItem() == SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.asItem())
			{
				return true;
			}
		}
		return false;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, SandSpreaderBE entity)
	{
		if (level.isClientSide())
		{
			return;
		}
		if (!entity.readyToUpdate())
		{
			return;
		}
		if (!entity.hasCatalyst())
		{
			return;
		}
		if (!MiscHelper.checkIfNearbyInvestiture((ServerLevel) level, pos, false))
		{
			return;
		}
		ItemStackHandler inv = entity.itemHandler;
		int slot = MiscHelper.randomSlot(inv);
		ItemStack item = inv.getStackInSlot(slot);
		if (item.getItem() == Blocks.SAND.asItem())
		{
			ItemStack newItem = new ItemStack(SandmasteryBlocks.TALDAIN_BLACK_SAND);
			newItem.setCount(item.getCount());
			inv.setStackInSlot(slot, newItem);
		}
		setChanged(level, pos, state);
	}
}
