/*
 * File updated ~ 2 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.common.eventHandlers;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
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

	public static boolean isValidSpiritWebEntity(Entity entity)
	{
		return entity instanceof Player
				|| entity instanceof AbstractVillager
				|| entity instanceof ZombieVillager
				|| (entity instanceof Raider && !(entity instanceof Ravager))
				|| entity instanceof AbstractPiglin
				|| entity instanceof Warden
				|| entity instanceof Llama
				|| entity instanceof Cat;
	}
}
