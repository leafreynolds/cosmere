package leaf.cosmere.sandmastery.common.eventHandlers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.items.MetalNuggetItem;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.PotionItem;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Sandmastery.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SandmasteryEntityEventHandler
{
	@SubscribeEvent
	public static void onFinishUsingItem(LivingEntityUseItemEvent.Finish event)
	{
		if (event.isCanceled())
		{
			return;
		}

		final LivingEntity livingEntity = event.getEntity();
		if (event.getItem().getItem() instanceof PotionItem item)
		{
			SpiritwebCapability.get(livingEntity).ifPresent(spiritweb ->
			{
				SpiritwebCapability data = (SpiritwebCapability) spiritweb;
				SandmasterySpiritwebSubmodule sb = (SandmasterySpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);
				int playerHydration = sb.getHydrationLevel();
				int maxPlayerHydration = sb.MAX_HYDRATION;
				if(playerHydration < maxPlayerHydration)
				{
					sb.adjustHydration(Math.min(1000, maxPlayerHydration - playerHydration), true);
				}
			});
		}
	}
}
