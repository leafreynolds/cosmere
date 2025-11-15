/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.helpers.TimeHelper;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardplateData;
import leaf.cosmere.surgebinding.common.capabilities.ShardData;
import leaf.cosmere.surgebinding.common.eventHandlers.SurgebindingCapabilitiesHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ShardbladeItem extends SwordItem implements IShardItem
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
		return false;
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
		//no shiny.
		return false;
	}

	@Override
	public ShardData getShardData(ItemStack stack)
	{
		return (ShardData)stack.getCapability(ShardData.SHARD_DATA).resolve().get();
	}


	@Override
	public void bond(ItemStack stack, Player entity)
	{
		// if bonded, then don't bond again
		if (getBond(stack) != null)
		{
			return;
		}


		getShardData(stack).setBondedEntity(entity);

	}

	@Override
	public void releaseBond(ItemStack stack)
	{
		if(getBond(stack) != null)
		{
			getShardData(stack).setEmptyBond();
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
		if (getBond(stack).is(player))
		{
			return true;
		}
		return false;
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected)
	{
		ShardData data = getShardData(pStack);
		if(pEntity instanceof Player player)
		{
			if(data.isLiving())
			{
				bond(pStack, player);
			}
			else if(data.bondTicks() >= bondTime())
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
		super.inventoryTick(pStack, pLevel, pEntity, pItemSlot, pIsSelected);
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{

		final ShardData data = getShardData(pStack);
		String attunedPlayerName = data.getBondedName();
		UUID attunedPlayer = data.getBondedEntity();
		if (attunedPlayer != null)
		{
			pTooltipComponents.add(TextHelper.createText(attunedPlayerName));
		}

		if(!data.isLiving())
		{
			pTooltipComponents.add(TextHelper.createText("Deadblade"));
		}
		else if(data.getOrder() != null)
		{
			pTooltipComponents.add(TextHelper.createText(StringHelper.fixCapitalisation(data.getOrder().getName())));
		}




	}
}
