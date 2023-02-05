package leaf.cosmere.sandmastery.common.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public interface IItemHandlerModifiableWLayers extends IItemHandlerModifiable
{
    int getLayers();
}