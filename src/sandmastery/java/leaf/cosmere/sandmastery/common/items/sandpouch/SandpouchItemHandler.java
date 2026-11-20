/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.items.sandpouch;

import leaf.cosmere.sandmastery.common.items.SandPouchItem;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.registries.SandmasteryDataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import static leaf.cosmere.sandmastery.common.utils.MiscHelper.isSandBlock;
import static leaf.cosmere.sandmastery.common.utils.MiscHelper.isSandLayer;
import static leaf.cosmere.sandmastery.common.utils.MiscHelper.isSandCharged;

//sand pouch inventory. the stack holds a layer count, the slots are views onto it.
//slot 0 banks sand in, slots 1 and 2 hand it back as blocks and layers
public class SandpouchItemHandler implements IItemHandlerModifiable
{
	public static final int SIZE = 3;

	// black sand shows first on a pouch that has never been toggled; anything reading the flag must use this fallback
	public static final boolean DEFAULT_DISPLAYING_BLACK_SAND = true;

	public static final int SLOT_INPUT = 0;
	public static final int SLOT_BLOCKS = 1;
	public static final int SLOT_LAYERS = 2;

	private static final int LAYERS_PER_BLOCK = 8;

	// a supplier, so a client-side menu always reads the current held stack: the server replaces that
	// stack object whenever it syncs, and a reference captured at open time would never update
	private final Supplier<ItemStack> pouch;

	public SandpouchItemHandler(ItemStack pouch)
	{
		this(() -> pouch);
	}

	public SandpouchItemHandler(Supplier<ItemStack> pouch)
	{
		this.pouch = pouch;
	}

	public boolean getDisplayingBlackSand() {
		return pouch.get().getOrDefault(SandmasteryDataComponents.DISPLAYING_BLACK_SAND, DEFAULT_DISPLAYING_BLACK_SAND);
	}

	public int getTotalLayers()
	{
		return pouch.get().getOrDefault(SandmasteryDataComponents.SAND_LAYERS.get(), 0);
	}

	private void setTotalLayers(int layers)
	{
		if (pouch.get().isEmpty())
		{
			return;
		}
		pouch.get().set(SandmasteryDataComponents.SAND_LAYERS.get(), Math.max(0, layers));
		if (getChargedLayers() > layers)
			setChargedLayers(layers);
	}

	public int getChargedLayers()
	{
		return pouch.get().getOrDefault(SandmasteryDataComponents.CHARGED_SAND_LAYERS.get(), 0);
	}

	private void setChargedLayers(int layers)
	{
		if (pouch.get().isEmpty())
		{
			return;
		}
		pouch.get().set(SandmasteryDataComponents.CHARGED_SAND_LAYERS.get(), Math.max(0, layers));
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
		boolean unchargedIsPriority = getDisplayingBlackSand();
		BlockItem blockItem = unchargedIsPriority ? SandmasteryBlocks.TALDAIN_BLACK_SAND.asItem() : SandmasteryBlocks.TALDAIN_WHITE_SAND.asItem();
		BlockItem layerItem = unchargedIsPriority ? SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.asItem() : SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.asItem();
		int layers = unchargedIsPriority ? getTotalLayers() - getChargedLayers() : getChargedLayers();
		return switch (slot)
		{
			case SLOT_BLOCKS -> makeStack(blockItem, layers / LAYERS_PER_BLOCK);
			case SLOT_LAYERS -> makeStack(layerItem, layers);
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
				insertItem(SLOT_INPUT, stack, false);
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
		ItemStack beforeStack = getStackInSlot(slot);
		int beforeCount = beforeStack.getCount();
		int taken = beforeCount - remaining.getCount();
		if (taken > 0)
		{
			setTotalLayers(getTotalLayers() - taken * (slot == SLOT_BLOCKS ? LAYERS_PER_BLOCK : 1));
			if (isSandCharged(beforeStack))
				setChargedLayers(getChargedLayers() - taken * (slot == SLOT_BLOCKS ? LAYERS_PER_BLOCK : 1));
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
			setTotalLayers(getTotalLayers() + layerValue(stack) * stack.getCount());
			if (isSandCharged(stack))
				setChargedLayers(getChargedLayers() + layerValue(stack) * stack.getCount());
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
			setTotalLayers(getTotalLayers() - toExtract * (slot == SLOT_BLOCKS ? LAYERS_PER_BLOCK : 1));
			if (isSandCharged(existing))
				setChargedLayers(getChargedLayers() - toExtract * (slot == SLOT_BLOCKS ? LAYERS_PER_BLOCK : 1));
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
