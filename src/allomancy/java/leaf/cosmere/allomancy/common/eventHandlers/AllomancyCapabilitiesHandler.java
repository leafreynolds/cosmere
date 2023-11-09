/*
 * File updated ~ 5 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.capabilities.world.IScadrial;
import leaf.cosmere.allomancy.common.capabilities.world.ScadrialCapability;
import leaf.cosmere.api.Constants;
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

@Mod.EventBusSubscriber(modid = Allomancy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AllomancyCapabilitiesHandler
{
	@SubscribeEvent
	public static void attachWorldCapabilities(AttachCapabilitiesEvent<Level> event)
	{
		Level level = event.getObject();

		//todo move this to scadrial when that dimension is set up
		if (event.getObject().dimension() == Level.OVERWORLD) //.location().toString().contains("overworld"))
		{
			event.addCapability(Constants.Resources.SCADRIAL_CAP, new ICapabilitySerializable<CompoundTag>()
			{
				final ScadrialCapability scadrial = new ScadrialCapability(level);
				final LazyOptional<IScadrial> scadrialInstance = LazyOptional.of(() -> scadrial);

				@Nonnull
				@Override
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side)
				{
					return cap == ScadrialCapability.CAPABILITY ? (LazyOptional<T>) scadrialInstance
					                                            : LazyOptional.empty();
				}

				@Override
				public CompoundTag serializeNBT()
				{
					return scadrial.serializeNBT();
				}

				@Override
				public void deserializeNBT(CompoundTag nbt)
				{
					scadrial.deserializeNBT(nbt);
				}
			});
		}

	}


	@SubscribeEvent
	public static void onWorldTick(TickEvent.LevelTickEvent event)
	{
		Level level = event.level;
		if (event.phase == TickEvent.Phase.END && event.side.isServer() && event.level.dimension() == Level.OVERWORLD)
		{
			MinecraftServer server = level.getServer();
			if (server != null && server.getPlayerList().getPlayerCount() == 0)
			{
				return;
			}

			ScadrialCapability.get(level).ifPresent(iScadrial ->
			{
				//todo change things so that mists aren't always out every night.

			});

		}

	}

}
