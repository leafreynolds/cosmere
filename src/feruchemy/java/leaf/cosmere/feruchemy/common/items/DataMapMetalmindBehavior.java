/*
 * File created ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.items;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.charge.IChargeable;
import leaf.cosmere.common.datamaps.MetalmindProperties;
import net.minecraft.world.item.ItemStack;

//metalmind behaviour for items declared via the feruchemy:metalmind data map
public class DataMapMetalmindBehavior implements IChargeable
{
	private final MetalmindProperties properties;

	public DataMapMetalmindBehavior(MetalmindProperties properties)
	{
		this.properties = properties;
	}

	@Override
	public Metals.MetalType getChargeMetalType(ItemStack stack)
	{
		return properties.metal();
	}

	@Override
	public float getMaxChargeModifier()
	{
		return properties.chargeModifier();
	}
}
