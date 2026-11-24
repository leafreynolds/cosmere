/*
 * File updated ~ 18 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.eventHandlers;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.commands.SandmasteryCommands;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import leaf.cosmere.sandmastery.common.datamaps.HydrationProperties;
import leaf.cosmere.sandmastery.common.datamaps.SandmasteryDataMaps;
import leaf.cosmere.sandmastery.common.diem.trial.DiemTrials;
import leaf.cosmere.sandmastery.common.diem.trial.OutsideHelp;
import leaf.cosmere.sandmastery.common.diem.trial.TrialScouting;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Sandmastery.MODID)
public class SandmasteryCommonEventHandler
{
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event)
	{
		SandmasteryCommands.register(event.getDispatcher());
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		SpiritwebCapability.get(event.getEntity()).ifPresent(spiritweb ->
		{
			SandmasterySpiritwebSubmodule submodule = SandmasterySpiritwebSubmodule.get(spiritweb);
			if (submodule.isPreDiemSave())
			{
				submodule.resetForDiem(spiritweb);
				spiritweb.syncToClients(null);
			}
		});
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event)
	{
		if (event.getEntity() instanceof ServerPlayer player)
		{
			DiemTrials.tick(player);
			TrialScouting.tick(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent event)
	{
		if (event.getEntity() instanceof ServerPlayer player)
		{
			DiemTrials.fail(player, Component.translatable("diem.sandmastery.trial.fail.died"));
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		if (event.getEntity() instanceof ServerPlayer player)
		{
			DiemTrials.fail(player, Component.translatable("diem.sandmastery.trial.fail.left_area"));
			TrialScouting.stop(player);
		}
	}

	@SubscribeEvent
	public static void onTeleport(EntityTeleportEvent event)
	{
		if (event.getEntity() instanceof ServerPlayer player)
		{
			DiemTrials.onOutsideHelp(player, "teleport");
		}
	}

	@SubscribeEvent
	public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event)
	{
		boolean masteredSand = event.getPlacedBlock().is(SandmasteryBlocks.TEMPORARY_SAND_BLOCK.getBlock());
		if (event.getEntity() instanceof ServerPlayer player && !masteredSand)
		{
			DiemTrials.onOutsideHelp(player, "placed_block");
		}
	}

	@SubscribeEvent
	public static void onEffectAdded(MobEffectEvent.Added event)
	{
		if (event.getEntity() instanceof ServerPlayer player && OutsideHelp.isBeneficial(event.getEffectInstance()))
		{
			DiemTrials.onOutsideHelp(player, "effects");
		}
	}


	@SubscribeEvent
	public static void onFinishUsingItem(LivingEntityUseItemEvent.Finish event)
	{
		final HydrationProperties hydration = SandmasteryDataMaps.getHydration(event.getItem());
		if (hydration == null)
		{
			return;
		}

		SpiritwebCapability.get(event.getEntity()).ifPresent(spiritweb ->
		{
			SpiritwebCapability data = (SpiritwebCapability) spiritweb;
			SandmasterySpiritwebSubmodule sb = (SandmasterySpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);
			if (sb.getHydrationLevel() < SandmasteryConfigs.SERVER.MAX_HYDRATION.get())
			{
				sb.adjustHydration(hydration.amount());
			}
		});
	}
}
