/*
 * File created ~ 7 - 9 - 2022 ~ Leaf
 */

package leaf.cosmere.handlers;

import leaf.cosmere.Cosmere;
import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.cap.world.IRoshar;
import leaf.cosmere.cap.world.RosharCapability;
import leaf.cosmere.constants.Constants;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilitiesHandler
{

	@SubscribeEvent
	public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		Entity eventEntity = event.getObject();

		if (isValidSpiritWebEntity(eventEntity))
		{
			LivingEntity livingEntity = (LivingEntity) eventEntity;
			event.addCapability(Constants.Resources.SPIRITWEB_CAP, new ICapabilitySerializable<CompoundTag>()
			{
				final SpiritwebCapability spiritweb = new SpiritwebCapability(livingEntity);
				final LazyOptional<ISpiritweb> spiritwebInstance = LazyOptional.of(() -> spiritweb);

				@Nonnull
				@Override
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side)
				{
					return cap == SpiritwebCapability.CAPABILITY ? (LazyOptional<T>) spiritwebInstance
					                                             : LazyOptional.empty();
				}

				@Override
				public CompoundTag serializeNBT()
				{
					return spiritweb.serializeNBT();
				}

				@Override
				public void deserializeNBT(CompoundTag nbt)
				{
					spiritweb.deserializeNBT(nbt);
				}
			});
		}
	}


	@SubscribeEvent
	public static void attachWorldCapabilities(AttachCapabilitiesEvent<Level> event)
	{
		Level level = event.getObject();

		if (event.getObject().dimension().location().toString().contains("roshar"))
		{
			event.addCapability(Constants.Resources.ROSHAR_CAP, new ICapabilitySerializable<CompoundTag>()
			{
				final RosharCapability roshar = new RosharCapability(level);
				final LazyOptional<IRoshar> rosharInstance = LazyOptional.of(() -> roshar);

				@Nonnull
				@Override
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side)
				{
					return cap == RosharCapability.CAPABILITY ? (LazyOptional<T>) rosharInstance
					                                          : LazyOptional.empty();
				}

				@Override
				public CompoundTag serializeNBT()
				{
					return roshar.serializeNBT();
				}

				@Override
				public void deserializeNBT(CompoundTag nbt)
				{
					roshar.deserializeNBT(nbt);
				}
			});
		}

	}

	public static boolean isValidSpiritWebEntity(Entity entity)
	{
		return entity instanceof Player
				|| entity instanceof AbstractVillager
				|| entity instanceof ZombieVillager
				|| (entity instanceof Raider && !(entity instanceof Ravager))
				|| entity instanceof AbstractPiglin
				|| entity instanceof Llama
				|| entity instanceof Cat;
	}
}
