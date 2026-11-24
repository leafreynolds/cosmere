package leaf.cosmere.sandmastery.common.registries;

import com.google.common.collect.ImmutableSet;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SandmasteryPoiTypes
{
	public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, Sandmastery.MODID);

	public static final DeferredHolder<PoiType, PoiType> DIEM_STONE = POI_TYPES.register("diem_stone", () -> poiFor(SandmasteryBlocks.DIEM_STONE.getBlock()));
	public static final DeferredHolder<PoiType, PoiType> DIEM_CHECKPOINT = POI_TYPES.register("diem_checkpoint", () -> poiFor(SandmasteryBlocks.DIEM_CHECKPOINT.getBlock()));

	private static PoiType poiFor(Block block)
	{
		return new PoiType(ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates()), 0, 1);
	}
}
