/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.constants.Metals;
import net.minecraft.world.item.ItemStack;

public class MetalIngotItem extends MetalItem
{

	public MetalIngotItem(Metals.MetalType metalType)
	{
		super(metalType);
	}


	@Override
	public boolean isPiglinCurrency(ItemStack stack)
	{
		final Metals.MetalType metalType = this.getMetalType();
		return metalType == Metals.MetalType.GOLD || metalType == Metals.MetalType.ELECTRUM;
	}
}
