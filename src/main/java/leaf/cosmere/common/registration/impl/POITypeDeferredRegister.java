package leaf.cosmere.common.registration.impl;

import com.google.common.collect.ImmutableSet;
import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class POITypeDeferredRegister extends WrappedDeferredRegister<PoiType>
{
	public POITypeDeferredRegister(String modid)
	{
		super(modid, Registries.POINT_OF_INTEREST_TYPE);
	}

	private PoiType registerPOIType(Block block)
	{
		return new PoiType(
				ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates()),//states
				1, //tickets(?)
				1);//range
	}

	public POITypeRegistryObject<PoiType> registerPOI(String name, Supplier<BlockRegistryObject<Block, BlockItem>> block)
	{
		return register(name, () -> registerPOIType(block.get().getBlock()));
	}

	private POITypeRegistryObject<PoiType> register(String name, Supplier<PoiType> supplier)
	{
		return register(name, supplier, POITypeRegistryObject::new);
	}

}