/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.BondableRadiantShardData;
import leaf.cosmere.surgebinding.common.capabilities.IRadiantShardData;
import leaf.cosmere.surgebinding.common.registries.SurgebindingAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class HonorbladeItem extends ShardbladeItem
{
	public final Roshar.RadiantOrder radiantOrder;

	/**
	 * Modifiers applied when the item is in the mainhand of a user. copied from sword item
	 */
	private ItemAttributeModifiers attributeModifiers = null;

	// having primary and secondary means that theoretically a player could
	// hold two shards that share a surge and be twice as strong in that surge // todo more strength shouldn't really matter much in surgebinding
	protected static final UUID PRIMARY_HONORBLADE_SURGE_UUID = UUID.fromString("CB3F55D3-4865-4180-A497-9C13A33DB5CF");
	protected static final UUID SECONDARY_HONORBLADE_SURGE_UUID = UUID.fromString("FA233E1C-4180-4865-A497-BCCE9785ACA3");

	public HonorbladeItem(Roshar.RadiantOrder gemstone, Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
		this.radiantOrder = gemstone;
	}


	/**
	 * Gets a map of item attribute modifiers, used by damage when used as melee weapon.
	 */
	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack)
	{
		if (attributeModifiers == null)
		{
			//modifier ids are ResourceLocations in 1.21, and the attack attributes are mainhand only
			attributeModifiers = ItemAttributeModifiers.builder()
					.add(Attributes.ATTACK_DAMAGE,
							new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage, AttributeModifier.Operation.ADD_VALUE),
							EquipmentSlotGroup.MAINHAND)
					.add(Attributes.ATTACK_SPEED,
							new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeedIn, AttributeModifier.Operation.ADD_VALUE),
							EquipmentSlotGroup.MAINHAND)
					.add(SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(radiantOrder.getFirstSurge()).getHolder(),
							new AttributeModifier(Surgebinding.rl(radiantOrder.getName() + "_primary_surge"), 5, AttributeModifier.Operation.ADD_VALUE),
							EquipmentSlotGroup.HAND)
					.add(SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(radiantOrder.getSecondSurge()).getHolder(),
							new AttributeModifier(Surgebinding.rl(radiantOrder.getName() + "_secondary_surge"), 5, AttributeModifier.Operation.ADD_VALUE),
							EquipmentSlotGroup.HAND)
					.build();
		}

		return attributeModifiers;
	}

	@Override
	public ItemStack getDefaultInstance()
	{
		ItemStack stack = super.getDefaultInstance();
		IRadiantShardData data = getShardData(stack);
		data.setOrder(radiantOrder);
		return stack;
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected)
	{
		super.inventoryTick(pStack, pLevel, pEntity, pItemSlot, pIsSelected);
		//order is fixed by the item
		//write server side, and only when missing so the stack isn't rewritten every tick
		if (!pLevel.isClientSide)
		{
			BondableRadiantShardData data = getShardData(pStack);
			if (data.getOrder() != radiantOrder)
			{
				data.setOrder(radiantOrder);
			}
		}
	}

	@Override
	public int bondTime()
	{
		return 5;
	}

	@Override
	public boolean canSummonDismiss(LivingEntity entity, ItemStack stack)
	{
		return true;
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

	@Override
	public ItemStack randomizedLootData(ItemStack stack)
	{
		BondableRadiantShardData data = getShardData(stack);
		data.setLiving(false);
		data.setOrder(radiantOrder);
		return stack;
	}
}
