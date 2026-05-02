/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.helpers.TimeHelper;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.surgebinding.common.capabilities.BondableRadiantShardData;
import leaf.cosmere.surgebinding.common.capabilities.RadiantShardData;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ShardbladeItem extends SwordItem implements IRadiantShardItem, IBondableItem, ISummonableItem
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
	public BondableRadiantShardData getShardData(ItemStack stack)
	{
		return (BondableRadiantShardData) stack.getCapability(RadiantShardData.RADIANT_SHARD_DATA).resolve().get();
	}

	@Override
	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
	{
		final BondableRadiantShardData shardData = new BondableRadiantShardData(stack);
		if (nbt != null)
		{
			shardData.deserializeNBT(nbt);
		}
		return shardData;
	}

	@Override
	public @Nullable CompoundTag getShareTag(@NotNull ItemStack stack)
	{
		final BondableRadiantShardData data = getShardData(stack);
		CompoundTag tag = stack.getOrCreateTag();
		tag.put("shard_data", data.serializeNBT());
		return tag;
	}

	@Override
	public void readShareTag(@NotNull ItemStack stack, @Nullable CompoundTag nbt)
	{
		super.readShareTag(stack, nbt);
		if (nbt != null && nbt.contains("shard_data"))
		{
			final BondableRadiantShardData data = getShardData(stack);
			data.deserializeNBT(nbt.getCompound("shard_data"));
		}
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
		super.inventoryTick(pStack, pLevel, pEntity, pItemSlot, pIsSelected);
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
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
