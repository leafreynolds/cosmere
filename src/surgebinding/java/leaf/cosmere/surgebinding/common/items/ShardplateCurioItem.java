package leaf.cosmere.surgebinding.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.api.*;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.helpers.TimeHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.items.ChargeableItemBase;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardplateData;
import leaf.cosmere.surgebinding.common.capabilities.ShardData;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.awt.*;
import java.util.*;
import java.util.List;


public class ShardplateCurioItem extends ChargeableItemBase implements ICurioItem, IShardItem
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

	@Override
	public DynamicShardplateData getShardData(ItemStack stack)
	{
		return (DynamicShardplateData) stack.getCapability(ShardData.SHARD_DATA).resolve().get();
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
			entity.addEffect(EffectsHelper.getNewEffect(MobEffects.JUMP, 1));
		}

		DynamicShardplateData data = getShardData(stack);
		if(entity instanceof Player player)
		{
			if(data.bondTicks() >= bondTime())
			{
				bond(stack, player);
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

		ICurioItem.super.curioTick(slotContext, stack);
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected)
	{
		DynamicShardplateData data = getShardData(pStack);
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
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack)
	{
		if (slotContext.entity().level().isClientSide) return;

		// Copy the capability data if present
		prevStack.getCapability(ShardData.SHARD_DATA).ifPresent(fromCap -> {
			stack.getCapability(ShardData.SHARD_DATA).ifPresent(toCap -> {
				CompoundTag nbt = fromCap.serializeNBT();
				toCap.deserializeNBT(nbt);
			});
		});
		ICurioItem.super.onEquip(slotContext, prevStack, stack);
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack)
	{
		if (slotContext.entity().level().isClientSide) return;

		// Copy the capability data if present
		stack.getCapability(ShardData.SHARD_DATA).ifPresent(fromCap -> {
			newStack.getCapability(ShardData.SHARD_DATA).ifPresent(toCap -> {
				CompoundTag nbt = fromCap.serializeNBT();
				toCap.deserializeNBT(nbt);
			});
		});

		ICurioItem.super.onUnequip(slotContext, newStack, stack);
	}




	@Override
	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
	{
		final DynamicShardplateData dynamicShardplateData = new DynamicShardplateData(stack);

		if (nbt != null && nbt.contains("shard_data"))
		{
			dynamicShardplateData.deserializeNBT(nbt.getCompound("shard_data")); // todo check if this breaks things?
			if(dynamicShardplateData.getOrder() == null)
			{
				int i = (int)(Math.random()*10);
				dynamicShardplateData.setOrder(Roshar.RadiantOrder.valueOf(i).get());
			}
		}

		return dynamicShardplateData;
	}

	@Override
	public boolean canSummonDismiss(LivingEntity player, ItemStack stack)
	{
		return isBondedTo(stack, player) && isLiving(stack);
	}

	@Override
	public void bond(ItemStack stack, Player entity)
	{
		DynamicShardplateData data = getShardData(stack);
		if(!data.isBonded())
		{
			data.setBondedEntity(entity);
			setAttunedPlayer(stack, entity);
			setAttunedPlayerName(stack, entity);
		}
	}

	@Override
	public void releaseBond(ItemStack stack)
	{
		getShardData(stack).setEmptyBond();
	}

	@Override
	public int bondTime()
	{
		return (int)TimeHelper.MinutesToSeconds(30.0) * 20;
	}

	public boolean canSummonDismiss(Player player, ItemStack stack)
	{
		return getBond(stack).is(player) && isLiving(stack);
	}


	public Color getColour(ItemStack stack)
	{
		DynamicShardplateData data = getShardData(stack);
		Roshar.RadiantOrder order = getOrder(stack);

		if(!isLiving(stack))
		{
			return Roshar.getDeadplate();
		}
		else if(data.isColored())
		{
			return order.getPlateColor();
		}
		else
		{
			return Roshar.getDeadplate();
		}
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
			builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Armor run", 1.05, AttributeModifier.Operation.MULTIPLY_BASE));
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Armor damage", 0.4, AttributeModifier.Operation.MULTIPLY_TOTAL));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(uuid, "Armor attack speed", 1.1, AttributeModifier.Operation.MULTIPLY_TOTAL));
			builder.put(ForgeMod.STEP_HEIGHT_ADDITION.get(), new AttributeModifier(uuid, "Armor stepper", 0.8, AttributeModifier.Operation.ADDITION));
		}
		else
		{
			builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Armor run", -0.3, AttributeModifier.Operation.MULTIPLY_BASE));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(uuid, "Armor attack speed", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL));
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

		final DynamicShardplateData data = getShardData(pStack);

		if(!data.isLiving())
		{
			pTooltipComponents.add(TextHelper.createText("Deadplate"));
		}
		else if(data.getOrder() != null)
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
	public @Nullable CompoundTag getShareTag(@NotNull ItemStack stack)
	{
		final DynamicShardplateData data = getShardData(stack);
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
			final DynamicShardplateData data = getShardData(stack);

			if (nbt.contains("shard_data"))
			{
				data.deserializeNBT(nbt.getCompound("shard_data"));
			}
		}

	}



	@Override
	public void addFilled(CreativeModeTab.Output output)
	{
		for(Roshar.RadiantOrder order: EnumUtils.RADIANT_ORDERS)
		{
			if(order.equals(Roshar.RadiantOrder.BONDSMITH))
			{
				continue;
			}
			//dead
			output.accept(buildData(new ItemStack(this), order, false, null));
			ItemStack fullPower = new ItemStack(this);
			setCharge(fullPower, getMaxCharge(fullPower));
			buildData(fullPower, order, false, null);
			output.accept(fullPower);

			//living
			output.accept(buildData(new ItemStack(this), order, true, null));
			ItemStack fullPowerLiving = new ItemStack(this);
			setCharge(fullPowerLiving, getMaxCharge(fullPower));
			buildData(fullPowerLiving, order, true, null);
			output.accept(fullPowerLiving);
		}
	}

}

