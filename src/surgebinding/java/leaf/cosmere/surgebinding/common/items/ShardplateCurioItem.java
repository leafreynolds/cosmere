/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.items.ChargeableItemBase;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardplateData;
import leaf.cosmere.surgebinding.common.capabilities.RadiantShardData;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.registries.SurgebindingDataComponents;
import leaf.cosmere.surgebinding.common.utils.ParticleHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.awt.*;
import java.util.List;
import java.util.UUID;


public class ShardplateCurioItem extends ChargeableItemBase implements ICurioItem, IRadiantShardItem
{


	public ShardplateCurioItem(Properties properties)
	{
		super(properties);
	}

	@Override
	public int getMaxCharge(ItemStack itemStack)
	{
		return 84000;
	}

	public int chargeWithRadiant(ISpiritweb spiritweb, ItemStack itemStack, int amount)
	{
		ShardplateCurioItem item = (ShardplateCurioItem) itemStack.getItem();
		int charge = Math.min(amount, item.getMaxCharge(itemStack) - item.getCharge(itemStack));
		if (SurgebindingSpiritwebSubmodule.getSubmodule(spiritweb).getStormlight() >= charge)
		{
			item.adjustCharge(itemStack, charge);
			return charge;
		}
		return 0;
	}

	@Override
	public DynamicShardplateData getShardData(ItemStack stack)
	{
		return RadiantShardData.load(stack, createShardData(stack));
	}

	@Override
	public DynamicShardplateData createShardData(ItemStack stack)
	{
		return new DynamicShardplateData(stack);
	}

	//components are per stack, so carry the appearance across on equip
	private static void copyShardData(SlotContext slotContext, ItemStack from, ItemStack to)
	{
		if (from.isEmpty() || to.isEmpty())
		{
			return;
		}

		CompoundTag data = from.get(SurgebindingDataComponents.SHARD_DATA.get());
		if (data == null)
		{
			to.remove(SurgebindingDataComponents.SHARD_DATA.get());
		}
		else
		{
			//copy so the two stacks don't share one tag
			to.set(SurgebindingDataComponents.SHARD_DATA.get(), data.copy());
		}
	}

	public boolean isFullCharged(ItemStack itemStack)
	{
		return getMaxCharge(itemStack) == getCharge(itemStack);
	}

	public boolean isFoil(@NotNull ItemStack stack)
	{
		return false;
	}


	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack)
	{
		LivingEntity entity = slotContext.entity();
		SpiritwebCapability cap;
		final ShardplateCurioItem shardplateCurioItem = (ShardplateCurioItem) stack.getItem();

		if (SpiritwebCapability.get(entity).isPresent())
		{
			cap = (SpiritwebCapability) SpiritwebCapability.get(entity).get();
			SurgebindingSpiritwebSubmodule ssm = SurgebindingSpiritwebSubmodule.getSubmodule(cap);
			if (ssm.getStormlight() > 0 && !shardplateCurioItem.isFullCharged(stack))
			{
				int charge = chargeWithRadiant(cap, stack, Math.min(ssm.getStormlight(), 30));
				ssm.adjustStormlight(-charge, true);
			}
		}
		if (!entity.level().isClientSide)
		{
			//appearance is rolled server side
			seedShardData(stack);

			if (shardplateCurioItem.getCharge(stack) != 0)
			{
				entity.addEffect(EffectsHelper.getNewEffect(MobEffects.JUMP, 1));
			}
			if (shardplateCurioItem.getCharge(stack) <= shardplateCurioItem.getMaxCharge(stack) / 4)
			{
				ParticleHelper.spawnLeakEffect((ServerLevel) entity.level(), 2, entity);
			}
		}

		ICurioItem.super.curioTick(slotContext, stack);
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected)
	{
		if (!pLevel.isClientSide)
		{
			seedShardData(pStack);
		}
		super.inventoryTick(pStack, pLevel, pEntity, pItemSlot, pIsSelected);
	}

	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack)
	{
		if (slotContext.entity().level().isClientSide)
		{
			return;
		}

		// Copy the capability data if present
		copyShardData(slotContext, prevStack, stack);
		ICurioItem.super.onEquip(slotContext, prevStack, stack);
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack)
	{
		if (slotContext.entity().level().isClientSide)
		{
			return;
		}

		// Copy the capability data if present
		copyShardData(slotContext, stack, newStack);

		ICurioItem.super.onUnequip(slotContext, newStack, stack);
	}


	public Color getColour(ItemStack stack)
	{
		DynamicShardplateData data = getShardData(stack);
		Roshar.RadiantOrder order = getOrder(stack);

		if (!isLiving(stack))
		{
			return Roshar.getDeadplate();
		}
		else if (data.isColored())
		{
			return order.getPlateColor();
		}
		else
		{
			return Roshar.getDeadplate();
		}
	}


	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack)
	{
		//modifier ids are ResourceLocations now
		ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();

		builder.putAll(ICurioItem.super.getAttributeModifiers(slotContext, id, stack));
		if (getCharge(stack) > 0)
		{
			builder.put(Attributes.ARMOR, new AttributeModifier(modifierId(id, "armor"), 22, AttributeModifier.Operation.ADD_VALUE));
			builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(modifierId(id, "armor_toughness"), 0.4f, AttributeModifier.Operation.ADD_VALUE));
			builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(modifierId(id, "armor_knockback_resistance"), 0.4D, AttributeModifier.Operation.ADD_VALUE));
			builder.put(Attributes.FLYING_SPEED, new AttributeModifier(modifierId(id, "armor_jump"), 1.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
			builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(modifierId(id, "armor_run"), 1.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(modifierId(id, "armor_damage"), 0.4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(modifierId(id, "armor_attack_speed"), 1.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
			builder.put(Attributes.STEP_HEIGHT, new AttributeModifier(modifierId(id, "armor_stepper"), 0.8, AttributeModifier.Operation.ADD_VALUE));
		}
		else
		{
			builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(modifierId(id, "armor_run"), -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(modifierId(id, "armor_attack_speed"), -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		}
		return builder.build();
	}

	private static ResourceLocation modifierId(ResourceLocation slotId, String suffix)
	{
		return Surgebinding.rl(slotId.getPath() + "_" + suffix);
	}

	@Override
	public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<net.minecraft.network.chat.Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{
		String attunedPlayerName = getAttunedPlayerName(pStack);
		UUID attunedPlayer = getAttunedPlayer(pStack);
		if (attunedPlayer != null)
		{
			pTooltipComponents.add(TextHelper.createText(attunedPlayerName));
		}
		pTooltipComponents.add(TextHelper.createText(String.format("%s/%s", getCharge(pStack), getMaxCharge(pStack))).withStyle(ChatFormatting.GRAY));

		final DynamicShardplateData data = getShardData(pStack);

		if (data.getOrder() != null)
		{
			pTooltipComponents.add(TextHelper.createText(StringHelper.fixCapitalisation(data.getOrder().getName())));
		}


		if (!InventoryScreen.hasShiftDown())
		{
			pTooltipComponents.add(Constants.Translations.TOOLTIP_HOLD_SHIFT);
			return;
		}

		pTooltipComponents.add(TextHelper.createText(String.format("Faceplate: %s", data.getFaceplateID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Head: %s", data.getHeadID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Body: %s", data.getBodyID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Kama: %s", data.getKamaID())));

		pTooltipComponents.add(TextHelper.createText(String.format("Left Arm: %s", data.getLeftArmID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Left Paldron: %s", data.getLeftPaldronsID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Left Leg: %s", data.getLeftLegID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Left Boot Outside: %s", data.getLeftBootOutsideID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Left Boot Tip: %s", data.getLeftBootTipID())));

		pTooltipComponents.add(TextHelper.createText(String.format("Right Arm: %s", data.getRightArmID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Right Paldron: %s", data.getRightPaldronsID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Right Leg: %s", data.getRightLegID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Right Boot Outside: %s", data.getRightBootOutsideID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Right Boot Tip: %s", data.getRightBootTipID())));
	}


	@Override
	public void addFilled(CreativeModeTab.Output output)
	{
		for (Roshar.RadiantOrder order : EnumUtils.RADIANT_ORDERS)
		{
			if (order.equals(Roshar.RadiantOrder.BONDSMITH))
			{
				continue;
			}
			//dead
			output.accept(buildData(new ItemStack(this), order, false));
			ItemStack fullPower = new ItemStack(this);
			setCharge(fullPower, getMaxCharge(fullPower));
			buildData(fullPower, order, false);
			output.accept(fullPower);

			//living
			output.accept(buildData(new ItemStack(this), order, true));
			ItemStack fullPowerLiving = new ItemStack(this);
			setCharge(fullPowerLiving, getMaxCharge(fullPower));
			buildData(fullPowerLiving, order, true);
			output.accept(fullPowerLiving);
		}
	}

}
