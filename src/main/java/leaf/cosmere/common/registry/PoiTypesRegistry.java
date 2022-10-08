/*
 * File updated ~ 20 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.POITypeDeferredRegister;
import leaf.cosmere.common.registration.impl.POITypeRegistryObject;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class PoiTypesRegistry
{
	public static final POITypeDeferredRegister POINT_OF_INTERESTS = new POITypeDeferredRegister(Cosmere.MODID);

	public static POITypeRegistryObject<PoiType> METAL_TRADER_POI = POINT_OF_INTERESTS.registerPOI("metal_trader", () -> (BlocksRegistry.METALWORKING_TABLE));

}
