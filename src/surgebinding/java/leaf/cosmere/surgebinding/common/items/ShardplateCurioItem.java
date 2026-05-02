/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * Removed Forge Capability/LazyOptional ceremony. Per-stack DynamicShardplateData binds
 * directly to the ItemStack via DataComponents.CUSTOM_DATA. `initCapabilities`/getShareTag/
 * readShareTag are gone in 1.21.1; copy-on-equip / copy-on-unequip now copies the
 * CUSTOM_DATA component directly.
 *
 * Curio attribute modifier API on 1.21.1: SlotContext + ResourceLocation id (UUID gone).
 * Operation enum renamed: ADDITION→ADD_VALUE, MULTIPLY_BASE→ADD_MULTIPLIED_BASE,
 * MULTIPLY_TOTAL→ADD_MULTIPLIED_TOTAL. ForgeMod.STEP_HEIGHT_ADDITION → Attributes.STEP_HEIGHT.
 * Multimap key type Attribute → Holder<Attribute>; wrap with BuiltInRegistries.ATTRIBUTE.wrapAsHolder.
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
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.utils.ParticleHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.awt.*;
import java.util.List;
import java.util.UUID;


public class ShardplateCurioItem extends ChargeableItemBase implements ICurioItem, IRadiantShardItem
{
	// Stable AttributeModifier IDs (replace 1.20.1 UUID-keyed modifiers).
	private static final ResourceLocation MOD_ID_ARMOR = ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, "shardplate_armor");
	private static final ResourceLocation MOD_ID_ARMOR_TOUGHNESS = ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, "shardplate_armor_toughness");
	private static final ResourceLocation MOD_ID_KNOCKBACK = ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, "shardplate_knockback_resistance");
	private static final ResourceLocation MOD_ID_FLYING_SPEED = ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, "shardplate_flying_speed");
	private static final ResourceLocation MOD_ID_MOVEMENT_SPEED = ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, "shardplate_movement_speed");
	private static final ResourceLocation MOD_ID_ATTACK_DAMAGE = ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, "shardplate_attack_damage");
	private static final ResourceLocation MOD_ID_ATTACK_SPEED = ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, "shardplate_attack_speed");
	private static final ResourceLocation MOD_ID_STEP_HEIGHT = ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, "shardplate_step_height");
	private static final ResourceLocation MOD_ID_DEAD_MOVEMENT_SPEED = ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, "shardplate_dead_movement_speed");
	private static final ResourceLocation MOD_ID_DEAD_ATTACK_SPEED = ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, "shardplate_dead_attack_speed");

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
		return new DynamicShardplateData(stack);
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
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack)
	{
		if (slotContext.entity().level().isClientSide)
		{
			return;
		}

		// Copy DataComponents.CUSTOM_DATA across (replaces the old per-capability NBT copy).
		copyCustomData(prevStack, stack);
		ICurioItem.super.onEquip(slotContext, prevStack, stack);
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack)
	{
		if (slotContext.entity().level().isClientSide)
		{
			return;
		}

		// Copy DataComponents.CUSTOM_DATA across (replaces the old per-capability NBT copy).
		copyCustomData(stack, newStack);

		ICurioItem.super.onUnequip(slotContext, newStack, stack);
	}

	private static void copyCustomData(ItemStack from, ItemStack to)
	{
		if (from.isEmpty() || to.isEmpty())
		{
			return;
		}
		CustomData data = from.get(DataComponents.CUSTOM_DATA);
		if (data != null)
		{
			to.set(DataComponents.CUSTOM_DATA, data);
		}
	}

	public Color getColour(ItemStack stack)
	{
		DynamicShardplateData data = getShardData(stack);
		Roshar.RadiantOrder order = getOrder(stack);

		if (!isLiving(stack))
		{
			return Roshar.getDeadplate();
		}
		else if (data.isColored() && order != null)
		{
			return order.getPlateColor();
		}
		else
		{
			return Roshar.getDeadplate();
		}
	}


	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack)
	{
		ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();

		Multimap<Holder<Attribute>, AttributeModifier> defaultModifiers;

		builder.putAll(ICurioItem.super.getAttributeModifiers(slotContext, uuid, stack));
		if (getCharge(stack) > 0)
		{
			builder.put(holder(Attributes.ARMOR), new AttributeModifier(MOD_ID_ARMOR, 22, AttributeModifier.Operation.ADD_VALUE));
			builder.put(holder(Attributes.ARMOR_TOUGHNESS), new AttributeModifier(MOD_ID_ARMOR_TOUGHNESS, 0.4f, AttributeModifier.Operation.ADD_VALUE));
			builder.put(holder(Attributes.KNOCKBACK_RESISTANCE), new AttributeModifier(MOD_ID_KNOCKBACK, 0.4D, AttributeModifier.Operation.ADD_VALUE));
			builder.put(holder(Attributes.FLYING_SPEED), new AttributeModifier(MOD_ID_FLYING_SPEED, 1.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
			builder.put(holder(Attributes.MOVEMENT_SPEED), new AttributeModifier(MOD_ID_MOVEMENT_SPEED, 1.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
			builder.put(holder(Attributes.ATTACK_DAMAGE), new AttributeModifier(MOD_ID_ATTACK_DAMAGE, 0.4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
			builder.put(holder(Attributes.ATTACK_SPEED), new AttributeModifier(MOD_ID_ATTACK_SPEED, 1.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
			builder.put(holder(Attributes.STEP_HEIGHT), new AttributeModifier(MOD_ID_STEP_HEIGHT, 0.8, AttributeModifier.Operation.ADD_VALUE));
		}
		else
		{
			builder.put(holder(Attributes.MOVEMENT_SPEED), new AttributeModifier(MOD_ID_DEAD_MOVEMENT_SPEED, -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
			builder.put(holder(Attributes.ATTACK_SPEED), new AttributeModifier(MOD_ID_DEAD_ATTACK_SPEED, -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		}
		defaultModifiers = builder.build();
		return defaultModifiers;
	}

	private static Holder<Attribute> holder(Holder<Attribute> attribute)
	{
		return attribute;
	}

	private static Holder<Attribute> holder(Attribute attribute)
	{
		return BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute);
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Item.TooltipContext pContext, List<net.minecraft.network.chat.Component> pTooltipComponents, TooltipFlag pIsAdvanced)
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
