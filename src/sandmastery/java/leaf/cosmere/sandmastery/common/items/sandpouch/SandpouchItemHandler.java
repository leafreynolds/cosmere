/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.items.sandpouch;

import leaf.cosmere.sandmastery.common.items.SandPouchItem;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class SandpouchItemHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundTag>
{

	private enum MODES
	{
		ADD,
		REMOVE,
		NO_CHANGE
	}

	protected NonNullList<ItemStack> stacks;
	protected int layers;

	public SandpouchItemHandler()
	{
		this(1);
	}

	public SandpouchItemHandler(int size)
	{
		stacks = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	{
		return slot == 0 ? !stack.isEmpty() && SandPouchItem.SUPPORTED_ITEMS.test(stack) : false;
	}

	@Override
	public void setStackInSlot(int slot, @NotNull ItemStack stack)
	{
		validateSlotIndex(slot);
		ItemStack prev = this.stacks.get(slot);
		int prevCount = prev.isEmpty() ? 0 : prev.getCount();
		int newCount = stack.isEmpty() ? 0 : stack.getCount();
		MODES mode = null;

		if (prevCount < newCount)
		{
			mode = MODES.ADD;
		}
		else if (newCount < prevCount)
		{
			mode = MODES.REMOVE;
		}
		else
		{
			mode = MODES.NO_CHANGE;
		}

		this.stacks.set(slot, stack);
		onContentsChanged(slot, Math.abs(prevCount - newCount), mode);
	}

	protected void onContentsChanged(int slot)
	{
		updateSlots();
	}

	protected void onContentsChanged(int slot, int count, MODES mode)
	{
		ItemStack changedStack = getStackInSlot(slot);
		boolean sandBlock = changedStack.getItem() == SandmasteryBlocks.TALDAIN_BLACK_SAND.asItem() || changedStack.getItem() == SandmasteryBlocks.TALDAIN_WHITE_SAND.asItem();
		boolean sandLayer = changedStack.getItem() == SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.asItem() || changedStack.getItem() == SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.asItem();

		switch (slot)
		{
			case 0:
				if (mode != MODES.ADD)
				{
					break; // This slot is input, and can accept both, don't update if it's removed
				}
				if (sandBlock)
				{
					layers += 8 * count; // Blocks are worth 8 layers
				}
				else if (sandLayer)
				{
					layers += count;
				}
				break;
			case 1:
				if (mode != MODES.REMOVE)
				{
					break; // this slot is output only, no item filter as only one item can ever be here
				}
				layers -= 8 * count; // Blocks are worth 8 layers
				break;
			case 2:
				if (mode != MODES.REMOVE)
				{
					break; // this slot is output only, no item filter as only one item can ever be here
				}
				layers -= count;
				break;
		}

		updateSlots();

	}

	private void updateSlots()
	{
		int numBlocks = (int) Math.floor(this.layers / 8);

		ItemStack blocksInSlot = new ItemStack(SandmasteryBlocks.TALDAIN_BLACK_SAND);
		blocksInSlot.setCount(Math.min(numBlocks, 64));

		ItemStack layersInSlot = new ItemStack(SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER);
		layersInSlot.setCount(Math.min(this.layers, 64));

		this.stacks.set(0, ItemStack.EMPTY);
		this.stacks.set(1, blocksInSlot);
		this.stacks.set(2, layersInSlot);
	}

	@Override
	public int getSlots()
	{
		return this.stacks.size();
	}

	@Override
	@NotNull
	public ItemStack getStackInSlot(int slot)
	{
		validateSlotIndex(slot);
		return this.stacks.get(slot);
	}

	@Override
	@NotNull
	public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		if (!isItemValid(slot, stack))
		{
			return stack;
		}

		validateSlotIndex(slot);

		ItemStack existing = this.stacks.get(slot);

		int limit = getStackLimit(slot, stack);

		if (!existing.isEmpty())
		{
			if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
			{
				return stack;
			}

			limit -= existing.getCount();
		}

		if (limit <= 0)
		{
			return stack;
		}

		boolean reachedLimit = stack.getCount() > limit;

		if (!simulate)
		{
			if (existing.isEmpty())
			{
				this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
			}
			else
			{
				existing.grow(reachedLimit ? limit : stack.getCount());
			}
			onContentsChanged(slot);
		}

		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	protected int getStackLimit(int slot, @NotNull ItemStack stack)
	{
		return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
	}

	@Override
	@NotNull
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (amount == 0)
		{
			return ItemStack.EMPTY;
		}

		validateSlotIndex(slot);

		ItemStack existing = this.stacks.get(slot);

		if (existing.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		int toExtract = Math.min(amount, existing.getMaxStackSize());

		if (existing.getCount() <= toExtract)
		{
			if (!simulate)
			{
				this.stacks.set(slot, ItemStack.EMPTY);
				onContentsChanged(slot, existing.getCount(), MODES.REMOVE);
				return existing;
			}
			else
			{
				return existing.copy();
			}
		}
		else
		{
			if (!simulate)
			{
				this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
				onContentsChanged(slot, toExtract, MODES.REMOVE);
			}

			return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
		}
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	@Override
	public CompoundTag serializeNBT()
	{
		updateSlots();
		ListTag nbtTagList = new ListTag();
		for (int i = 0; i < stacks.size(); i++)
		{
			if (!stacks.get(i).isEmpty())
			{
				CompoundTag itemTag = new CompoundTag();
				itemTag.putInt("Slot", i);
				stacks.get(i).save(itemTag);
				nbtTagList.add(itemTag);
			}
		}
		CompoundTag nbt = new CompoundTag();
		nbt.put("Items", nbtTagList);
		nbt.putInt("Size", stacks.size());
		nbt.putInt("Layers", layers);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : stacks.size());
		ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
		for (int i = 0; i < tagList.size(); i++)
		{
			CompoundTag itemTags = tagList.getCompound(i);
			int slot = itemTags.getInt("Slot");

			if (slot >= 0 && slot < stacks.size())
			{
				stacks.set(slot, ItemStack.of(itemTags));
			}
		}
		setLayers(nbt.contains("Layers") ? nbt.getInt("Layers") : 0);
		onLoad();
	}

	public void setSize(int size)
	{
		stacks = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	protected void onLoad()
	{
		updateSlots();
	}

	protected void validateSlotIndex(int slot)
	{
		if (slot < 0 || slot >= stacks.size())
		{
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
		}
	}

	protected void setLayers(int layers)
	{
		this.layers = layers;
		updateSlots();
	}

	public int getLayers()
	{
		return this.layers;
	}
}
