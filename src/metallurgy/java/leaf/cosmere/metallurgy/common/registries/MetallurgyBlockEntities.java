package leaf.cosmere.metallurgy.common.registries;

import leaf.cosmere.metallurgy.common.Metallurgy;
import leaf.cosmere.metallurgy.common.blocks.entities.MetallurgyWorkbenchBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MetallurgyBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITY_TYPES, Metallurgy.MODID);

    public static final RegistryObject<BlockEntityType<MetallurgyWorkbenchBE>> METALLURGY_WORKBENCH =
            BLOCK_ENTITIES.register("metallurgy_workbench",
                    () -> BlockEntityType.Builder.of(MetallurgyWorkbenchBE::new,
                            MetallurgyBlocks.METALLURGY_WORKBENCH.getBlock()).build(null));
}
