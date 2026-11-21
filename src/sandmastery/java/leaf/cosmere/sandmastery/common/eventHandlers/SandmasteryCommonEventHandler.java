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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

@EventBusSubscriber(modid = Sandmastery.MODID)
public class SandmasteryCommonEventHandler
{
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event)
	{
		SandmasteryCommands.register(event.getDispatcher());
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
