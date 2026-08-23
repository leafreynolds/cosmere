/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.helpers.TimeHelper;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.surgebinding.common.capabilities.BondableRadiantShardData;
import leaf.cosmere.surgebinding.common.capabilities.RadiantShardData;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class ShardbladeItem extends SwordItem implements IRadiantShardItem, IBondableItem, ISummonableItem
{
	protected final float attackDamage;
	protected final float attackSpeedIn;

	public ShardbladeItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, builderIn.attributes(SwordItem.createAttributes(tier, attackDamageIn, attackSpeedIn)));
		this.attackDamage = attackDamageIn + tier.getAttackDamageBonus();
		this.attackSpeedIn = attackSpeedIn;
	}

	//Shards can't be enchanted
	@Override
	public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment)
	{
		return false;
	}

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
		//no shiny.
		return false;
	}

	@Override
	public BondableRadiantShardData getShardData(ItemStack stack)
	{
		return RadiantShardData.load(stack, createShardData(stack));
	}

	@Override
	public BondableRadiantShardData createShardData(ItemStack stack)
	{
		return new BondableRadiantShardData(stack);
	}

	@Override
	public void bond(ItemStack stack, Player entity)
	{
		BondableRadiantShardData data = getShardData(stack);
		// if bonded, then don't bond again
		if (data.isBonded())
		{
			return;
		}

		data.setBondedEntity(entity);
	}

	@Override
	public void releaseBond(ItemStack stack)
	{
		BondableRadiantShardData data = getShardData(stack);
		if (data.isBonded())
		{
			data.setEmptyBond();
		}
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
		//bonding and appearance are persisted on the server and reach the client through the component
		if (!pLevel.isClientSide)
		{
			seedShardData(pStack);

			BondableRadiantShardData data = getShardData(pStack);
			if (pEntity instanceof Player player)
			{
				if (data.isLiving())
				{
					bond(pStack, player);
				}
				else if (data.bondTicks() >= bondTime())
				{
					bond(pStack, player);
				}
				else
				{
					data.tickBondUp();
				}
			}
			else
			{
				data.resetBondTicks();
			}
		}
		super.inventoryTick(pStack, pLevel, pEntity, pItemSlot, pIsSelected);
	}

	@Override
	public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{
		final BondableRadiantShardData data = getShardData(pStack);
		String attunedPlayerName = data.getBondedName();
		UUID attunedPlayer = data.getBondedEntity();
		if (attunedPlayer != null)
		{
			pTooltipComponents.add(TextHelper.createText(attunedPlayerName));
		}

		if (data.getOrder() != null)
		{
			pTooltipComponents.add(TextHelper.createText(StringHelper.fixCapitalisation(data.getOrder().getName())));
		}
	}
}
