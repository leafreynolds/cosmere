/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.items.CoinPouchItem;
import leaf.cosmere.allomancy.common.items.MetalVialItem;
import leaf.cosmere.common.itemgroups.CosmereItemGroups;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.common.registration.impl.ItemDeferredRegister;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;

public class AllomancyItems
{
	public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Allomancy.MODID);

	public static final ItemRegistryObject<CoinPouchItem> COIN_POUCH = ITEMS.register("coin_pouch", () -> new CoinPouchItem(PropTypes.Items.ONE.get().tab(CosmereItemGroups.ITEMS)));

	public static final ItemRegistryObject<MetalVialItem> METAL_VIAL = ITEMS.register("metal_vial", MetalVialItem::new);

}
