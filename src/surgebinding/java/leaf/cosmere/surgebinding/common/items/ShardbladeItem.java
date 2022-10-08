/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

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

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		return enchantment.category == EnchantmentCategory.WEAPON;
	}

	@Override
	public int getEnchantmentValue(ItemStack stack)
	{
		return 20;
	}

	@Override
	public boolean isEnchantable(ItemStack pStack)
	{
		return true;
	}

	@Override
	public boolean isFoil(ItemStack pStack)
	{
		//no shiny.
		return false;
	}
}
