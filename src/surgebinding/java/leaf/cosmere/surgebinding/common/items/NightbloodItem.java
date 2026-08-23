/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

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
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class NightbloodItem extends SwordItem implements IBondableItem
{
	protected final float attackDamage;
	protected final float attackSpeedIn;

	private ItemAttributeModifiers attributeModifiers = null;
	protected static final UUID NIGHTBLOOD_SURGE_UUID = UUID.fromString("CB3F55D3-4865-4180-A497-9C13A33DB5CC");

	public NightbloodItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, builderIn.attributes(SwordItem.createAttributes(tier, attackDamageIn, attackSpeedIn)));
		this.attackDamage = attackDamageIn + tier.getAttackDamageBonus();
		this.attackSpeedIn = attackSpeedIn;
	}

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
		return false;
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
		//the bond is persisted on the server and reaches the client through the component
		if (!pLevel.isClientSide)
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
		}
		super.inventoryTick(pStack, pLevel, pEntity, pItemSlot, pIsSelected);
	}

	/**
	 * Gets a map of item attribute modifiers, used by damage when used as melee weapon.
	 */
	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack)
	{
		if (attributeModifiers == null)
		{
			attributeModifiers = ItemAttributeModifiers.builder()
					.add(Attributes.ATTACK_DAMAGE,
							new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage, AttributeModifier.Operation.ADD_VALUE),
							EquipmentSlotGroup.MAINHAND)
					.add(Attributes.ATTACK_SPEED,
							new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeedIn, AttributeModifier.Operation.ADD_VALUE),
							EquipmentSlotGroup.MAINHAND)
					.build();
		}

		return attributeModifiers;
	}

	@Override
	public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
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
