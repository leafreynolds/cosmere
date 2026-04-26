/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.blocks.entities.SandJarBE;
import leaf.cosmere.sandmastery.common.blocks.entities.SandSpreaderBE;
import leaf.cosmere.sandmastery.common.blocks.entities.TemporarySandBE;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SandmasteryBlockEntitiesRegistry
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Sandmastery.MODID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SandJarBE>> SAND_JAR_BE = BLOCK_ENTITIES.register("sand_jar_be", () -> BlockEntityType.Builder.of(SandJarBE::new, SandmasteryBlocks.SAND_JAR_BLOCK.getBlock()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SandSpreaderBE>> SAND_SPREADER_BE = BLOCK_ENTITIES.register("sand_spreader_be", () -> BlockEntityType.Builder.of(SandSpreaderBE::new, SandmasteryBlocks.SAND_SPREADING_TUB_BLOCK.getBlock()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TemporarySandBE>> TEMPORARY_SAND_BE = BLOCK_ENTITIES.register("temporary_sand_be", () -> BlockEntityType.Builder.of(TemporarySandBE::new, SandmasteryBlocks.TEMPORARY_SAND_BLOCK.getBlock()).build(null));
}
