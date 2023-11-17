/*
 * File updated ~ 26 - 5 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.utils;

import leaf.cosmere.api.*;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryDimensions;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class MiscHelper
{
	public static boolean checkIfNearbyInvestiture(ServerLevel pLevel, BlockPos pPos, boolean includeMobs)
	{
		int range = 6;
		AABB areaOfEffect = new AABB(pPos).inflate(range, range, range);
		List<LivingEntity> entitiesToCheckForInvesiture = pLevel.getEntitiesOfClass(LivingEntity.class, areaOfEffect, e -> true);

		AtomicBoolean foundSomething = new AtomicBoolean(false);

		for (LivingEntity target : entitiesToCheckForInvesiture)
		{
			SpiritwebCapability.get(target).ifPresent(targetSpiritweb -> {
				boolean concealed = false;
				final AttributeMap targetAttributes = target.getAttributes();
				final Attribute cognitiveConcealmentAttr = AttributesRegistry.COGNITIVE_CONCEALMENT.get();
				if (targetAttributes.hasAttribute(cognitiveConcealmentAttr))
				{
					concealed = targetAttributes.getValue(cognitiveConcealmentAttr) > 0;
				}
				for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
				{
					if (concealed)
					{
						foundSomething.set(false);
						break;
					}

					final boolean targetIsPlayer = target instanceof Player;

					if (manifestation.getManifestationType() == Manifestations.ManifestationTypes.SANDMASTERY)
						continue; //sandmastery uses charged sand, and as such won't charge it either

					boolean test = targetSpiritweb.hasManifestation(manifestation);
					boolean test2 = manifestation.isActive(targetSpiritweb);
					Manifestation test3 = manifestation;

					//if target is not a player and has any manifestations at all
					if (!targetIsPlayer && targetSpiritweb.hasManifestation(manifestation) && includeMobs)
					{
						foundSomething.set(true);
						break;
					}
					//if target is player and has any active manifestations,
					else if (targetIsPlayer && manifestation.isActive(targetSpiritweb))
					{
						foundSomething.set(true);
						break;
					}
				}
			});
		}
		return foundSomething.get();
	}

	public static void chargeItemFromInvestiture(ItemStack stack, Level level, Entity pEntity, int maxCharge)
	{
		if (level.isClientSide())
		{
			return;
		}
		int currCharge = StackNBTHelper.getInt(stack, Constants.NBT.CHARGE_LEVEL, 0);
		if (checkIfNearbyInvestiture((ServerLevel) level, pEntity.blockPosition(), false))
		{
			StackNBTHelper.setInt(stack, Constants.NBT.CHARGE_LEVEL, Mth.clamp(currCharge + 100, 0, maxCharge));
		}
	}

	public static boolean onTaldain(Level pLevel)
	{
		return pLevel.dimension().equals(SandmasteryDimensions.DAYSIDE_TALDAIN_DIM_KEY);
	}

	public static int distanceFromGround(LivingEntity e)
	{
		BlockPos pos = e.blockPosition();
		double y = pos.getY();
		int dist = 0;
		for (double i = y; i >= e.level.getMinBuildHeight(); i--)
		{
			BlockState block = e.level.getBlockState(pos.offset(0, -dist, 0));
			if (!block.isAir())
			{
				return dist;
			}
			dist++;
		}

		return -1;
	}

	public static boolean isActivatedAndActive(ISpiritweb data, Manifestation manifestation)
	{
		return (Keybindings.MANIFESTATION_USE_ACTIVE.isDown() && data.getSelectedManifestation() == manifestation.getManifestation());
	}

	public static int getChargeFromItemStack(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return 0;
		}

		if (stack.getItem() == SandmasteryBlocksRegistry.TALDAIN_BLACK_SAND_LAYER.asItem())
		{
			return stack.getCount() * 10;
		}
		if (stack.getItem() == SandmasteryBlocksRegistry.TALDAIN_BLACK_SAND.asItem())
		{
			return stack.getCount() * 80;
		}
		if (stack.getItem() == SandmasteryItems.SAND_JAR_ITEM.asItem())
		{
			return StackNBTHelper.getInt(stack, Constants.NBT.CHARGE_LEVEL, 0);
		}
		else
		{
			return 0;
		}
	}

	public static String intToAbbreviatedStr(int num)
	{
		if (num < 1e3)
		{
			return String.valueOf(num);
		}
		if (num < 1e6)
		{
			return String.valueOf(num / 1000) + "k";
		}
		if (num < 1e9)
		{
			return String.valueOf(num / 1000000) + "m";
		}
		if (num < 1e12)
		{
			return String.valueOf(num / 1000000000) + "b";
		}
		return "";
	}

	public static void logToChat(ISpiritweb data, String msg)
	{
		if (data.getLiving() instanceof Player player)
		{
			player.sendSystemMessage(Component.literal(msg));
		}
	}

	public static boolean isClient(ISpiritweb data)
	{
		return data.getLiving().level.isClientSide;
	}

	public static int getHotkeyFlags(ISpiritweb data)
	{
		final CompoundTag dataTag = data.getCompoundTag();
		final CompoundTag sandmasteryTag = CompoundNBTHelper.getOrCreate(dataTag, Sandmastery.MODID);
		return sandmasteryTag.getInt(SandmasteryConstants.HOTKEY_TAG);
	}

	public static boolean enabledViaHotkey(ISpiritweb data, int requiredFlag)
	{
		int hotkeyFlags = getHotkeyFlags(data);
		boolean enabledViaHotkey = false;
		if ((hotkeyFlags & requiredFlag) != 0)
		{
			enabledViaHotkey = true;
		}
		if ((hotkeyFlags & 1) != 0)
		{
			enabledViaHotkey = true;
		}
		return enabledViaHotkey;
	}

	public static int randomSlot(ItemStackHandler itemStackHandler)
	{
		return ThreadLocalRandom.current().nextInt(0, itemStackHandler.getSlots());
	}

	public static SandmasterySpiritwebSubmodule getSandmasterySubmodule(ISpiritweb data)
	{
		SpiritwebCapability playerSpiritweb = (SpiritwebCapability) data;
		SandmasterySpiritwebSubmodule submodule = (SandmasterySpiritwebSubmodule) playerSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);
		return submodule;
	}
}
