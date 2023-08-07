package leaf.cosmere.sandmastery.common.items.sandpouch;

import leaf.cosmere.sandmastery.common.items.SandPouchItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SandPouchSlot extends SlotItemHandler
{

	private final int index;
	private final boolean input;

	public SandPouchSlot(ISandPouchItemHandler inv, int index, int x, int y, boolean input)
	{
		super(inv, index, x, y);
		this.index = index;
		this.input = input;
	}

	@Override
	public boolean mayPlace(@NotNull ItemStack stack)
	{
		return input ? SandPouchItem.SUPPORTED_ITEMS.test(stack) : false;
	}

	@Override
	public void setChanged()
	{
		container.setChanged();
	}

}
