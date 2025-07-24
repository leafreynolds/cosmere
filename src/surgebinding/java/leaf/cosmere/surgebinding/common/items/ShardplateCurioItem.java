package leaf.cosmere.surgebinding.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.api.*;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.charge.IChargeable;
import leaf.cosmere.common.items.ChargeableItemBase;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardplateData;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static leaf.cosmere.surgebinding.common.registries.SurgebindingItems.SHARDPLATE_SUITS;

public class ShardplateCurioItem extends ChargeableItemBase implements ICurioItem, IHasColour
{
	public static final Capability<DynamicShardplateData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>()
	{
	});


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
		ShardplateCurioItem item = (ShardplateCurioItem)itemStack.getItem();
		int charge = Math.min(amount, item.getMaxCharge(itemStack) - item.getCharge(itemStack));
		if(SurgebindingSpiritwebSubmodule.getSubmodule(spiritweb).getStormlight() >= charge)
		{
			item.adjustCharge(itemStack, charge);
			return charge;
		}
		return 0;
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
		if(SpiritwebCapability.get(entity).isPresent())
		{
			cap = (SpiritwebCapability) SpiritwebCapability.get(entity).resolve().get();
			SurgebindingSpiritwebSubmodule ssm = SurgebindingSpiritwebSubmodule.getSubmodule(cap);
			if(ssm.getStormlight() > 0 && !((ShardplateCurioItem)stack.getItem()).isFullCharged(stack))
			{
				int charge = chargeWithRadiant(cap, stack, Math.min(ssm.getStormlight(), 30));
				ssm.adjustStormlight(-charge, true);
			}
		}
		if(((ShardplateCurioItem)stack.getItem()).getCharge(stack) != 0)
		{
			entity.addEffect(EffectsHelper.getNewEffect(MobEffects.JUMP, 0));
			entity.addEffect(EffectsHelper.getNewEffect(MobEffects.GLOWING,0));
		}

		ICurioItem.super.curioTick(slotContext, stack);
	}

	@Override
	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
	{
		final DynamicShardplateData dynamicShardplateData = new DynamicShardplateData();

		if (nbt != null)
		{
			dynamicShardplateData.deserializeNBT(nbt); // todo check if this breaks things?
		}

		return dynamicShardplateData;
	}

	public boolean isLiving()
	{
		return false;
	}


	@Override
	public Color getColour()
	{
		return Roshar.getDeadplate();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack)
	{
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

		Multimap<Attribute, AttributeModifier> defaultModifiers;

		builder.putAll(ICurioItem.super.getAttributeModifiers(slotContext, uuid, stack));
		if (getCharge(stack) > 0)
		{
			builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", 22, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", 0.4f, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", 0.4D, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.FLYING_SPEED, new AttributeModifier(uuid, "Armor jump", 1.3, AttributeModifier.Operation.MULTIPLY_BASE));
			builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Armor run", 1.2, AttributeModifier.Operation.MULTIPLY_BASE));
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Armor damage", 1.2, AttributeModifier.Operation.MULTIPLY_TOTAL));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(uuid, "Armor attack speed", 1.1, AttributeModifier.Operation.MULTIPLY_TOTAL));
			builder.put(ForgeMod.STEP_HEIGHT_ADDITION.get(), new AttributeModifier(uuid, "Armor stepper", 0.8, AttributeModifier.Operation.ADDITION));
		}
		else
		{
			builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Armor run", 0.7, AttributeModifier.Operation.MULTIPLY_BASE));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(uuid, "Armor attack speed", 0.9, AttributeModifier.Operation.MULTIPLY_TOTAL));
		}
		defaultModifiers = builder.build();
		return defaultModifiers;
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<net.minecraft.network.chat.Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{
		String attunedPlayerName = getAttunedPlayerName(pStack);
		UUID attunedPlayer = getAttunedPlayer(pStack);
		if (attunedPlayer != null)
		{
			pTooltipComponents.add(TextHelper.createText(attunedPlayerName));
		}
		pTooltipComponents.add(TextHelper.createText(String.format("%s/%s", getCharge(pStack), getMaxCharge(pStack))).withStyle(ChatFormatting.GRAY));


		if (!InventoryScreen.hasShiftDown())
		{
			pTooltipComponents.add(Constants.Translations.TOOLTIP_HOLD_SHIFT);
			return;
		}

		final DynamicShardplateData data = pStack.getCapability(ShardplateCurioItem.CAPABILITY).resolve().get();
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
	public @Nullable CompoundTag getShareTag(@NotNull ItemStack stack)
	{
		final DynamicShardplateData data = stack.getCapability(ShardplateCurioItem.CAPABILITY).resolve().get();
		CompoundTag tag = stack.getOrCreateTag();

		tag.put("shard_data", data.serializeNBT());

		return tag;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt)
	{
		super.readShareTag(stack, nbt);

		if (nbt != null)
		{
			final DynamicShardplateData data = stack.getCapability(ShardplateCurioItem.CAPABILITY).resolve().get();

			if (nbt.contains("shard_data"))
			{
				data.deserializeNBT(nbt.getCompound("shard_data"));
			}
		}

	}
}

