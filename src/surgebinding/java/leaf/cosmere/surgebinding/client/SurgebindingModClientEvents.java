/*
 * File updated ~ 17 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.client.render.SurgebindingLayerDefinitions;
import leaf.cosmere.surgebinding.client.render.SurgebindingRenderers;
import leaf.cosmere.surgebinding.client.render.model.ChullModel;
import leaf.cosmere.surgebinding.client.render.model.CrypticModel;
import leaf.cosmere.surgebinding.client.render.model.ShardplateModel;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = Surgebinding.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SurgebindingModClientEvents
{

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiOverlaysEvent guiOverlaysEvent)
	{
		guiOverlaysEvent.registerAbove(
				VanillaGuiOverlay.EXPERIENCE_BAR.id(),
				"hud",
				(gui, poseStack, partialTick, width, height) -> HUDHandler.onDrawScreenPost(poseStack)
		);
	}

	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
		//shardplate
		evt.registerLayerDefinition(SurgebindingLayerDefinitions.SHARDPLATE, ShardplateModel::createBodyLayer);
		evt.registerLayerDefinition(SurgebindingLayerDefinitions.CHULL, ChullModel::createBodyLayer);
		evt.registerLayerDefinition(SurgebindingLayerDefinitions.CRYPTIC, CrypticModel::createBodyLayer);
	}


	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		SurgebindingRenderers.register();

		CosmereAPI.logger.info("Surgebinding client setup complete!");
	}


	//special thank you to the chisels and bits team who have an example of how to register other sprites
	@SubscribeEvent
	public static void registerIconTextures(TextureStitchEvent.Pre event)
	{
		final TextureAtlas map = event.getAtlas();
		if (!map.location().equals(InventoryMenu.BLOCK_ATLAS))
		{
			return;
		}

		event.addSprite(Surgebinding.rl("icon/surgebinding"));

		for (final Roshar.Surges surge : Roshar.Surges.values())
		{
			String toLowerCase = surge.toString().toLowerCase(Locale.ROOT);
			event.addSprite(Surgebinding.rl("icon/surgebinding/" + toLowerCase));
		}
	}

}
