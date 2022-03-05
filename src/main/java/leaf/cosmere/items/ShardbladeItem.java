/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import net.minecraft.item.IItemTier;
import net.minecraft.item.SwordItem;

import net.minecraft.item.Item.Properties;

public class ShardbladeItem extends SwordItem
{
    public ShardbladeItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
    {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public boolean isFireResistant()
    {
        return true;
    }
}
