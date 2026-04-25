/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.client.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.capabilities.world.IScadrial;
import leaf.cosmere.allomancy.common.capabilities.world.ScadrialCapability;
import leaf.cosmere.allomancy.common.manifestation.AllomancyIronSteel;
import leaf.cosmere.allomancy.common.manifestation.AllomancyTin;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;

import java.util.Optional;

@EventBusSubscriber(modid = Allomancy.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class AllomancyClientEvents
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onRecipesUpdated(final RecipesUpdatedEvent event)
	{
		AllomancyIronSteel.invalidateWhitelist();
	}

	@SubscribeEvent
	public static void onRenderFog(ViewportEvent.RenderFog event)
	{
		final Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		Level level = mc.level;

		//check dimension -
		if (player == null || level == null || event.getCamera().getFluidInCamera() != FogType.NONE)
		{
			return;
		}

		//check capability
		// todo - check this only happens to whatever dimension scadrial is attached to. currently overworld.
		Optional<IScadrial> capOpt = ScadrialCapability.get(level);

		if (capOpt.isEmpty())
		{
			return;
		}

		//must exist
		ScadrialCapability scadrial = (ScadrialCapability) capOpt.get();

		scadrial.tickFog(event, player);
	}

	@SubscribeEvent
	public static void onPlaySoundEvent(PlaySoundEvent event)
	{
		AllomancyTin.onSound(event);
	}
}
