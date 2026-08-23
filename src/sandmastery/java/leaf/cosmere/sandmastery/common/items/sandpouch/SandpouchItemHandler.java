/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.items.sandpouch;

import leaf.cosmere.sandmastery.common.items.SandPouchItem;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.registries.SandmasteryDataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

//sand pouch inventory. the stack holds a layer count, the slots are views onto it.
//slot 0 banks sand in, slots 1 and 2 hand it back as blocks and layers
public class SandpouchItemHandler implements IItemHandler, IItemHandlerModifiable
{
	public static final int SIZE = 3;

	public static final int SLOT_INPUT = 0;
	public static final int SLOT_BLOCKS = 1;
	public static final int SLOT_LAYERS = 2;

	private static final int LAYERS_PER_BLOCK = 8;

	private final ItemStack pouch;

	public SandpouchItemHandler(ItemStack pouch)
	{
		this.pouch = pouch;
	}

	public int getLayers()
	{
		return pouch.getOrDefault(SandmasteryDataComponents.SAND_LAYERS.get(), 0);
	}

	private void setLayers(int layers)
	{
		pouch.set(SandmasteryDataComponents.SAND_LAYERS.get(), Math.max(0, layers));
	}

	//layers per sand item. blocks pack eight each
	private static int layerValue(ItemStack stack)
	{
		if (isSandBlock(stack))
		{
			return LAYERS_PER_BLOCK;
		}
		if (isSandLayer(stack))
		{
			return 1;
		}
		return 0;
	}

	private static boolean isSandBlock(ItemStack stack)
	{
		return stack.getItem() == SandmasteryBlocks.TALDAIN_BLACK_SAND.asItem()
				|| stack.getItem() == SandmasteryBlocks.TALDAIN_WHITE_SAND.asItem();
	}

	private static boolean isSandLayer(ItemStack stack)
	{
		return stack.getItem() == SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.asItem()
				|| stack.getItem() == SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.asItem();
	}

	@Override
	public int getSlots()
	{
		return SIZE;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack)
	{
		return slot == SLOT_INPUT && !stack.isEmpty() && SandPouchItem.SUPPORTED_ITEMS.test(stack);
	}

	@NotNull
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		validateSlotIndex(slot);
		int layers = getLayers();
		return switch (slot)
		{
			case SLOT_BLOCKS -> makeStack(SandmasteryBlocks.TALDAIN_BLACK_SAND.asItem(), layers / LAYERS_PER_BLOCK);
			case SLOT_LAYERS -> makeStack(SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.asItem(), layers);
			//the input slot never holds anything
			// converted to layers straight away
			default -> ItemStack.EMPTY;
		};
	}

	private ItemStack makeStack(net.minecraft.world.item.Item item, int count)
	{
		int clamped = Math.min(count, getSlotLimit(SLOT_LAYERS));
		return clamped <= 0 ? ItemStack.EMPTY : new ItemStack(item, clamped);
	}

	@Override
	public void setStackInSlot(int slot, @NotNull ItemStack stack)
	{
		validateSlotIndex(slot);
		switch (slot)
		{
			case SLOT_INPUT ->
			{
				if (isItemValid(SLOT_INPUT, stack))
				{
					setLayers(getLayers() + layerValue(stack) * stack.getCount());
				}
			}
			//the output slots only shrink
			//putting a smaller stack back means the player took some
			case SLOT_BLOCKS -> takeFromOutput(SLOT_BLOCKS, stack);
			case SLOT_LAYERS -> takeFromOutput(SLOT_LAYERS, stack);
			default ->
			{
			}
		}
	}

	private void takeFromOutput(int slot, ItemStack remaining)
	{
		int before = getStackInSlot(slot).getCount();
		int taken = before - remaining.getCount();
		if (taken > 0)
		{
			setLayers(getLayers() - taken * (slot == SLOT_BLOCKS ? LAYERS_PER_BLOCK : 1));
		}
	}

	@NotNull
	@Override
	public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty() || !isItemValid(slot, stack))
		{
			return stack;
		}

		validateSlotIndex(slot);

		if (!simulate)
		{
			setLayers(getLayers() + layerValue(stack) * stack.getCount());
		}

		//the pouch swallows sand whole
		//there is never a remainder
		return ItemStack.EMPTY;
	}

	@NotNull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (amount <= 0 || slot == SLOT_INPUT)
		{
			return ItemStack.EMPTY;
		}

		validateSlotIndex(slot);

		ItemStack existing = getStackInSlot(slot);
		if (existing.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		int toExtract = Math.min(amount, Math.min(existing.getCount(), existing.getMaxStackSize()));

		if (!simulate)
		{
			setLayers(getLayers() - toExtract * (slot == SLOT_BLOCKS ? LAYERS_PER_BLOCK : 1));
		}

		return existing.copyWithCount(toExtract);
	}

	protected void validateSlotIndex(int slot)
	{
		if (slot < 0 || slot >= SIZE)
		{
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + SIZE + ")");
		}
	}
}
