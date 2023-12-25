/*
 * File updated ~ 18 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.eventHandlers;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
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
		Item item = event.getItem().getItem();
		boolean potion = item instanceof PotionItem;
		boolean metalVial = item.getDescriptionId().equals("item.allomancy.metal_vial"); // TODO: Replace the magic string with code
		final LivingEntity livingEntity = event.getEntity();
		if (potion || metalVial)
		{
			SpiritwebCapability.get(livingEntity).ifPresent(spiritweb ->
			{
				SpiritwebCapability data = (SpiritwebCapability) spiritweb;
				SandmasterySpiritwebSubmodule sb = (SandmasterySpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);
				int playerHydration = sb.getHydrationLevel();
				int maxPlayerHydration = SandmasteryConfigs.SERVER.MAX_HYDRATION.get();
				if (playerHydration < maxPlayerHydration)
				{
					if (potion)
					{
						sb.adjustHydration(maxPlayerHydration / 8);
					}
					// if(metalVial) sb.adjustHydration(Math.min(1000, maxPlayerHydration - playerHydration), true); // TODO: bottling machine should determine liquit used, and as such hydration value
				}
			});
		}
	}
}
