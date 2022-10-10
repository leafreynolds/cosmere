/*
 * File updated ~ 10 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tier;

public class NightbloodItem extends ShardbladeItem
{
	public NightbloodItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
	}


	public boolean canSummonDismiss(Player player)
	{
		return false;
	}
}
