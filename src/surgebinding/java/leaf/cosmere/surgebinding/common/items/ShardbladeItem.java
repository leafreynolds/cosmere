
/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ShardbladeItem extends SwordItem
{
	protected final float attackDamage;
	protected final float attackSpeedIn;

	public ShardbladeItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
		this.attackDamage = attackDamageIn + tier.getAttackDamageBonus();
		this.attackSpeedIn = attackSpeedIn;
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
	public int getEnchantmentValue(ItemStack itemStack)
	{
		return 20;
	}

	@Override
	public boolean isEnchantable(ItemStack itemStack)
	{
		return true;
	}

	@Override
	public boolean isFoil(ItemStack itemStack)
	{
		//no shiny.
		return false;
	}

	public boolean canSummonDismiss(Player player)
	{
		//todo check a value on sword for whether player has held the shardblade for long enough
		return true;
	}
}
