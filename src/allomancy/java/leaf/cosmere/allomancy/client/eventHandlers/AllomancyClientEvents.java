/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.client.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.capabilities.world.IScadrial;
import leaf.cosmere.allomancy.common.capabilities.world.ScadrialCapability;
import leaf.cosmere.allomancy.common.manifestation.AllomancyIronSteel;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Allomancy.MODID, value = Dist.CLIENT)
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
		if (player == null || event.getCamera().getFluidInCamera() != FogType.NONE)
		{
			return;
		}

		//check capability
		// todo - check this only happens to whatever dimension scadrial is attached to. currently overworld.
		LazyOptional<IScadrial> cap = ScadrialCapability.get(level);
		final Optional<IScadrial> resolve = cap.resolve();

		if (!cap.isPresent() || resolve.isEmpty())
		{
			return;
		}

		//must exist
		ScadrialCapability scadrial = (ScadrialCapability) resolve.get();

		scadrial.tickFog(event, player);
	}
}
