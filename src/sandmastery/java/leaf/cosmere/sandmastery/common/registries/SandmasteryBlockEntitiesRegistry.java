/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.blocks.entities.DiemCheckpointBE;
import leaf.cosmere.sandmastery.common.blocks.entities.DiemStoneBE;
import leaf.cosmere.sandmastery.common.blocks.entities.GlowingSandBE;
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
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DiemCheckpointBE>> DIEM_CHECKPOINT_BE = BLOCK_ENTITIES.register("diem_checkpoint_be", () -> BlockEntityType.Builder.of(DiemCheckpointBE::new, SandmasteryBlocks.DIEM_CHECKPOINT.getBlock()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DiemStoneBE>> DIEM_STONE_BE = BLOCK_ENTITIES.register("diem_stone_be", () -> BlockEntityType.Builder.of(DiemStoneBE::new, SandmasteryBlocks.DIEM_STONE.getBlock()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GlowingSandBE>> GLOWING_SAND_BE = BLOCK_ENTITIES.register("glowing_sand_be", () -> BlockEntityType.Builder.of(GlowingSandBE::new, SandmasteryBlocks.TALDAIN_GLOWING_SAND.getBlock()).build(null));
}
