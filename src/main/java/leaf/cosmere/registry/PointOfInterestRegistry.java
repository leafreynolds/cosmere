/*
 * File created ~ 20 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.registry;

import com.google.common.collect.ImmutableSet;
import leaf.cosmere.Cosmere;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class PointOfInterestRegistry
{
	public static DeferredRegister<PoiType> POINT_OF_INTERESTS = DeferredRegister.create(ForgeRegistries.POI_TYPES, Cosmere.MODID);

	public static RegistryObject<PoiType> METAL_TRADER_POI = POINT_OF_INTERESTS.register("metal_trader", () -> registerPOI(BlocksRegistry.METALWORKING_TABLE));


	private static PoiType registerPOI(Supplier<Block> block)
	{
		return new PoiType(
				ImmutableSet.copyOf(block.get().getStateDefinition().getPossibleStates()),//states
				1, //tickets(?)
				1);//range
	}
}
