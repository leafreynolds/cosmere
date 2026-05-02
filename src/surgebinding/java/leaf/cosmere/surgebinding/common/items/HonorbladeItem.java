/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.BondableRadiantShardData;
import leaf.cosmere.surgebinding.common.registries.SurgebindingAttributes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
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

	public HonorbladeItem(Roshar.RadiantOrder radiantOrder, Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
		this.radiantOrder = radiantOrder;
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers()
	{
		var builder = ItemAttributeModifiers.builder();

		builder.add(Attributes.ATTACK_DAMAGE,
				new AttributeModifier(ResourceLocation.withDefaultNamespace("base_attack_damage"),
						attackDamage, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND);
		builder.add(Attributes.ATTACK_SPEED,
				new AttributeModifier(ResourceLocation.withDefaultNamespace("base_attack_speed"),
						attackSpeedIn, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND);

		Attribute firstSurge = SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(radiantOrder.getFirstSurge()).getAttribute();
		Attribute secondSurge = SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(radiantOrder.getSecondSurge()).getAttribute();

		builder.add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(firstSurge),
				new AttributeModifier(Surgebinding.rl(radiantOrder.getName() + "_primary_honorblade_surge"),
						5, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND);
		builder.add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(secondSurge),
				new AttributeModifier(Surgebinding.rl(radiantOrder.getName() + "_secondary_honorblade_surge"),
						5, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND);

		return builder.build();
	}

	@Override
	public ItemStack getDefaultInstance()
	{
		ItemStack stack = super.getDefaultInstance();
		BondableRadiantShardData data = getShardData(stack);
		data.setOrder(radiantOrder);
		data.setLiving(true);
		return stack;
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected)
	{
		// Honorblades have a fixed order — keep the per-stack data in sync in case it was
		// stripped (e.g. from a creative-pick or older save).
		if (!pLevel.isClientSide)
		{
			BondableRadiantShardData data = getShardData(pStack);
			if (data.getOrder() != radiantOrder)
			{
				data.setOrder(radiantOrder);
			}
		}
		super.inventoryTick(pStack, pLevel, pEntity, pItemSlot, pIsSelected);
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
	public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{
		final BondableRadiantShardData data = getShardData(pStack);

		UUID attunedPlayer = data.getBondedEntity();
		if (attunedPlayer != null)
		{
			pTooltipComponents.add(TextHelper.createText(data.getBondedName()));
		}

		Roshar.RadiantOrder order = data.getOrder();
		// Honorblades carry a fixed order on the item type itself; fall back to it if the
		// per-stack data hasn't been seeded yet (e.g. an unticked creative-mode pick).
		if (order == null)
		{
			order = radiantOrder;
		}
		pTooltipComponents.add(TextHelper.createText(StringHelper.fixCapitalisation(order.getName())));
	}

	@Override
	public ItemStack randomizedLootData(ItemStack stack)
	{
		// Honorblades have a fixed order — don't randomize. Mark living and stamp the order
		// so the per-stack data matches the item type.
		BondableRadiantShardData data = getShardData(stack);
		data.setOrder(radiantOrder);
		data.setLiving(true);
		return stack;
	}
}
