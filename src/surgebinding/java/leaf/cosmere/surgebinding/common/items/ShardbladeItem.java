package leaf.cosmere.surgebinding.common.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class ShardbladeItem extends SwordItem
{
	protected final float attackDamage;
	protected final float attackSpeedIn;

	public ShardbladeItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, builderIn.fireResistant().attributes(SwordItem.createAttributes(tier, attackDamageIn, attackSpeedIn)));
		this.attackDamage = attackDamageIn + tier.getAttackDamageBonus();
		this.attackSpeedIn = attackSpeedIn;
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
		return false;
	}

	public boolean canSummonDismiss(Player player)
	{
		return true;
	}
}
