/*
 * File updated ~ 30 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.eventHandlers;

import leaf.cosmere.api.Constants;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.capabilities.world.HemalurgyWorldCapability;
import leaf.cosmere.hemalurgy.common.capabilities.world.IHemalurgyWorldCap;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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

@Mod.EventBusSubscriber(modid = Hemalurgy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HemalurgyCapabilitiesHandler
{
	@SubscribeEvent
	public static void attachWorldCapabilities(AttachCapabilitiesEvent<Level> event)
	{
		Level level = event.getObject();

		final ResourceLocation location = event.getObject().dimension().location();
		if (location.toString().contains("overworld"))
		{
			event.addCapability(Constants.Resources.HEMALURGY_WORLD_CAP, new ICapabilitySerializable<CompoundTag>()
			{
				final HemalurgyWorldCapability worldCapability = new HemalurgyWorldCapability(level);
				final LazyOptional<IHemalurgyWorldCap> worldCapInstance = LazyOptional.of(() -> worldCapability);

				@Nonnull
				@Override
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side)
				{
					return cap == HemalurgyWorldCapability.CAPABILITY ? (LazyOptional<T>) worldCapInstance
					                                                  : LazyOptional.empty();
				}

				@Override
				public CompoundTag serializeNBT()
				{
					return worldCapability.serializeNBT();
				}

				@Override
				public void deserializeNBT(CompoundTag nbt)
				{
					worldCapability.deserializeNBT(nbt);
				}
			});
		}

	}

	@SubscribeEvent
	public static void onWorldTick(TickEvent.LevelTickEvent event)
	{
		Level level = event.level;
		if (event.phase == TickEvent.Phase.END && event.side.isServer())
		{
			MinecraftServer server = level.getServer();
			if (server != null && server.getPlayerList().getPlayerCount() == 0)
			{
				return;
			}

			HemalurgyWorldCapability.get(level).ifPresent(IHemalurgyWorldCap::tick);

		}

	}
}
