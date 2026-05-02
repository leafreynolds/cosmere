/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * Per-stack BondableRadiantShardData now binds directly to the ItemStack and persists via
 * DataComponents.CUSTOM_DATA. The earlier capability-stubbed version is replaced here with
 * the real bond/release/tick wiring.
 */

package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.helpers.TimeHelper;
import leaf.cosmere.surgebinding.common.capabilities.BondableRadiantShardData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class ShardbladeItem extends SwordItem implements IRadiantShardItem, IBondableItem, ISummonableItem
{
	protected final float attackDamage;
	protected final float attackSpeedIn;

	public ShardbladeItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, builderIn.fireResistant().attributes(SwordItem.createAttributes(tier, attackDamageIn, attackSpeedIn)));
		this.attackDamage = attackDamageIn + tier.getAttackDamageBonus();
		this.attackSpeedIn = attackSpeedIn;
	}

	//Shards can't be enchanted
	@Override
	public int getEnchantmentValue(ItemStack itemStack)
	{
		return 0;
	}

	@Override
	public boolean isEnchantable(ItemStack itemStack)
	{
		return false;
	}

	@Override
	public boolean isFoil(ItemStack itemStack)
	{
		return false;
	}

	@Override
	public BondableRadiantShardData getShardData(ItemStack stack)
	{
		return new BondableRadiantShardData(stack);
	}

	@Override
	public void bond(ItemStack stack, Player entity)
	{
		getShardData(stack).setBondedEntity(entity);
	}

	@Override
	public void releaseBond(ItemStack stack)
	{
		BondableRadiantShardData data = getShardData(stack);
		data.setEmptyBond();
		data.resetBondTicks();
	}

	@Override
	public int bondTime()
	{
		return (int) TimeHelper.MinutesToSeconds(30.0) * 20;
	}

	@Override
	public boolean canSummonDismiss(LivingEntity player, ItemStack stack)
	{
		return isBondedTo(stack, player);
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected)
	{
		// Drive bond ticks while the blade is held by an attuned entity.
		if (!pLevel.isClientSide && pEntity instanceof LivingEntity living && isBondedTo(pStack, living))
		{
			getShardData(pStack).tickBondUp();
		}
		super.inventoryTick(pStack, pLevel, pEntity, pItemSlot, pIsSelected);
	}
}
