/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.client.render.FeruchemyLayerDefinitions;
import leaf.cosmere.feruchemy.client.render.FeruchemyRenderers;
import leaf.cosmere.feruchemy.client.render.model.BraceletModel;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyAtium;
import leaf.cosmere.feruchemy.common.registries.FeruchemyEffects;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = Feruchemy.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FeruchemyClientSetup
{

	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
		evt.registerLayerDefinition(FeruchemyLayerDefinitions.BRACELET, BraceletModel::createLayer);
		//evt.registerLayerDefinition(FeruchemyLayerDefinitions.NECKLACE, NecklaceModel::createLayer);
	}

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		MinecraftForge.EVENT_BUS.addListener(FeruchemyClientSetup::onRenderNameplateEvent);

		FeruchemyRenderers.register();

		CosmereAPI.logger.info("Feruchemy client setup complete!");
	}

	public static void onRenderNameplateEvent(RenderNameTagEvent event)
	{
		if (!(event.getEntity() instanceof LivingEntity livingEntity))
		{
			return;
		}

		MobEffectInstance effectInstance = livingEntity.getEffect(FeruchemyEffects.STORING_EFFECTS.get(Metals.MetalType.DURALUMIN).getMobEffect());
		if (effectInstance != null && effectInstance.getDuration() > 0)
		{
			if (effectInstance.getAmplifier() > 2)
			{
				event.setResult(Event.Result.DENY);
			}

		}

		final float atiumScale = FeruchemyAtium.getScale(livingEntity);
		if (atiumScale < 1)
		{
			double scale = atiumScale;
			event.getPoseStack().translate(0.0D, scale, 0.0D);
		}
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
		event.addSprite(Feruchemy.rl("icon/feruchemy"));

		for (final Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (!metalType.hasAssociatedManifestation())
			{
				continue;
			}

			String metalToLower = metalType.toString().toLowerCase(Locale.ROOT);
			event.addSprite(Feruchemy.rl("icon/feruchemy/" + metalToLower));
		}
	}

}
