/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class ShardbladeItem extends SwordItem
{
	public ShardbladeItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
	}

	@Override
	public boolean isFireResistant()
	{
		return true;
	}
}
