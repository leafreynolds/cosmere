/*
 * File updated ~ 1 - 11 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.eventHandlers;

import leaf.cosmere.api.Constants;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capability.world.IRoshar;
import leaf.cosmere.surgebinding.common.capability.world.RosharCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Surgebinding.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SurgebindingCapabilitiesHandler
{


	@SubscribeEvent
	public static void attachWorldCapabilities(AttachCapabilitiesEvent<Level> event)
	{
		Level level = event.getObject();

		if (level.dimension().location().toString().contains("roshar"))
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


	@SubscribeEvent
	public void onWorldTick(TickEvent.LevelTickEvent event)
	{
		Level level = event.level;
		if (event.phase == TickEvent.Phase.END && event.side.isServer())
		{
			MinecraftServer server = level.getServer();
			if (server != null && server.getPlayerList().getPlayerCount() == 0)
			{
				return;
			}

			RosharCapability.get(level).ifPresent(iRoshar ->
			{

			});

		}

	}


}
