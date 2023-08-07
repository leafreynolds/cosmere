package leaf.cosmere.sandmastery.common.items.sandpouch;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public interface ISandPouchItemHandler extends IItemHandlerModifiable
{
	void setLayers(int layers);
	int getLayers();
}
