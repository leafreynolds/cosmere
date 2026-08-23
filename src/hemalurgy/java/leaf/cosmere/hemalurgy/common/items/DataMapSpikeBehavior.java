/*
 * File created ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.items;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.charge.IChargeable;
import leaf.cosmere.common.datamaps.SpikeProperties;
import net.minecraft.world.item.ItemStack;

//spike behaviour for items declared via the hemalurgy:spike data map
public class DataMapSpikeBehavior implements IHemalurgicInfo, IChargeable
{
	private final SpikeProperties properties;

	public DataMapSpikeBehavior(SpikeProperties properties)
	{
		this.properties = properties;
	}

	@Override
	public Metals.MetalType getSpikeMetalType(ItemStack stack)
	{
		return properties.metal();
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

	//stolen powers shrink feruchemical capacity
	@Override
	public int getMaxCharge(ItemStack stack)
	{
		return scaleMaxChargeByInvestiture(stack, getBaseMaxCharge(stack));
	}
}
