/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to SizableShrimp from the Forge Project discord!
 * Java isn't my first programming language, so I didn't know you could collect and set up items like this!
 * Makes setting up items for metals a breeze~
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.blocks.TemporarySandBlock;
import leaf.cosmere.sandmastery.common.blocks.entities.SandJarBE;
import leaf.cosmere.sandmastery.common.blocks.entities.SandSpreaderBE;
import leaf.cosmere.sandmastery.common.blocks.entities.TemporarySandBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SandmasteryBlockEntitiesRegistry
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Sandmastery.MODID);

	public static final RegistryObject<BlockEntityType<SandJarBE>> SAND_JAR_BE = BLOCK_ENTITIES.register("sand_jar_be", () -> BlockEntityType.Builder.of(SandJarBE::new, SandmasteryBlocksRegistry.SAND_JAR_BLOCK.getBlock()).build(null));
	public static final RegistryObject<BlockEntityType<SandSpreaderBE>> SAND_SPREADER_BE = BLOCK_ENTITIES.register("sand_spreader_be", () -> BlockEntityType.Builder.of(SandSpreaderBE::new, SandmasteryBlocksRegistry.SAND_SPREADING_TUB_BLOCK.getBlock()).build(null));
	public static final RegistryObject<BlockEntityType<TemporarySandBE>> TEMPORARY_SAND_BE = BLOCK_ENTITIES.register("temporary_sand_be", () -> BlockEntityType.Builder.of(TemporarySandBE::new, SandmasteryBlocksRegistry.TEMPORARY_SAND_BLOCK.getBlock()).build(null));
}
