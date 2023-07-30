package leaf.cosmere.sandmastery.common.itemgroups;

import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class SandmasteryItemGroups
{
    public static CreativeModeTab ITEMS = new CreativeModeTab(Sandmastery.MODID)
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(SandmasteryItems.QIDO_ITEM);
        }
    };
}
