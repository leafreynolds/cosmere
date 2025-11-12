package leaf.cosmere.metallurgy.common.registries;

import leaf.cosmere.common.registration.impl.BlockDeferredRegister;
import leaf.cosmere.common.registration.impl.BlockRegistryObject;
import leaf.cosmere.metallurgy.common.Metallurgy;
import leaf.cosmere.metallurgy.common.blocks.MetallurgyWorkbenchBlock;
import net.minecraft.world.item.BlockItem;

public class MetallurgyBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Metallurgy.MODID);

    public static final BlockRegistryObject<MetallurgyWorkbenchBlock, BlockItem> METALLURGY_WORKBENCH =
            BLOCKS.register("metallurgy_workbench", MetallurgyWorkbenchBlock::new);
}
