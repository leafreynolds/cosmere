package leaf.cosmere.metallurgy.common.registries;

import leaf.cosmere.metallurgy.common.Metallurgy;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MetallurgyBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITY_TYPES, Metallurgy.MODID);

    // Block entities will be registered here when we implement machines
}
