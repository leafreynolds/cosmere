/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.registries;

import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.aviar.common.items.PatjisFruitItem;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.common.registration.impl.ItemDeferredRegister;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class AviarItems
{
	public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Aviar.MODID);

	//Items
	public static final ItemRegistryObject<PatjisFruitItem> PATJIS_FRUIT = ITEMS.register("fruit_of_patji", () -> new PatjisFruitItem(PropTypes.Items.SIXTEEN));


	//Eggs
	public static final ItemRegistryObject<ForgeSpawnEggItem> AVIAR_BIRD_EGG = ITEMS.registerSpawnEgg(AviarEntityTypes.AVIAR_ENTITY, 0x6c482f, 0x8a1a08);


}
