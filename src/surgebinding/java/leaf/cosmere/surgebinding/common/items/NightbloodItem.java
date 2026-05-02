/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.helpers.TimeHelper;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.surgebinding.common.capabilities.BondData;
import leaf.cosmere.surgebinding.common.capabilities.IBondData;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.registries.SurgebindingAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class NightbloodItem extends SwordItem implements IBondableItem
{
	protected final float attackDamage;
	protected final float attackSpeedIn;

	private Multimap<Attribute, AttributeModifier> attributeModifiers = null;
	protected static final UUID NIGHTBLOOD_SURGE_UUID = UUID.fromString("CB3F55D3-4865-4180-A497-9C13A33DB5CC");

	public NightbloodItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
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
	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
	{
		final BondData bondData = new BondData(stack);
		if (nbt != null)
		{
			bondData.deserializeNBT(nbt);
		}
		return bondData;
	}

	@Override
	public void bond(ItemStack stack, Player entity)
	{
		IBondData data = getBondData(stack);
		if (data.isBonded())
		{
			return;
		}
		data.setBondedEntity(entity);
	}

	@Override
	public void releaseBond(ItemStack stack)
	{
		IBondData data = getBondData(stack);
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
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected)
	{
		IBondData data = getBondData(pStack);
		if (pEntity instanceof Player player)
		{
			if (data.bondTicks() >= bondTime())
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

	/**
	 * Gets a map of item attribute modifiers, used by damage when used as melee weapon.
	 */
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack)
	{
		if (attributeModifiers == null)
		{
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeedIn, AttributeModifier.Operation.ADDITION));

			if (SurgebindingConfigs.SERVER.NIGHTBLOOD_SPOILERS.get())
			{
				for (Roshar.Surges surge : EnumUtils.SURGES)
				{
					builder.put(SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(surge).getAttribute(), new AttributeModifier(NIGHTBLOOD_SURGE_UUID, "Nightblood", 5, AttributeModifier.Operation.ADDITION));
				}
			}

			this.attributeModifiers = builder.build();
		}

		return switch (equipmentSlot)
		{
			case MAINHAND, OFFHAND -> this.attributeModifiers;
			default -> super.getAttributeModifiers(equipmentSlot, stack);
		};
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{
		final IBondData data = getBondData(pStack);
		String attunedPlayerName = data.getBondedName();
		UUID attunedPlayer = data.getBondedEntity();
		if (attunedPlayer != null)
		{
			pTooltipComponents.add(TextHelper.createText(attunedPlayerName));
		}
	}
}
