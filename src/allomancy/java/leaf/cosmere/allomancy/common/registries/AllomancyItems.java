/*
 * File updated ~ 5 - 8 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.items.CoinPouchItem;
import leaf.cosmere.allomancy.common.items.MetalVialItem;
import leaf.cosmere.allomancy.common.items.MistcloakItem;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.common.registration.impl.ItemDeferredRegister;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;

public class AllomancyItems
{
	public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Allomancy.MODID);

	public static final ItemRegistryObject<CoinPouchItem> COIN_POUCH = ITEMS.register("coin_pouch", () -> new CoinPouchItem(PropTypes.Items.ONE.get()));

	public static final ItemRegistryObject<MetalVialItem> METAL_VIAL = ITEMS.register("metal_vial", MetalVialItem::new);
	public static final ItemRegistryObject<MistcloakItem> MISTCLOAK = ITEMS.register("mistcloak", () -> new MistcloakItem(PropTypes.Items.MISTCLOAK.get()));

}
