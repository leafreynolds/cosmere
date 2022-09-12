/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.client;

import leaf.cosmere.Cosmere;
import leaf.cosmere.client.render.CosmereLayer;
import leaf.cosmere.client.render.armor.ShardplateModel;
import leaf.cosmere.client.render.curio.CosmereRenderers;
import leaf.cosmere.client.render.curio.LayerDefinitions;
import leaf.cosmere.client.render.curio.model.BraceletModel;
import leaf.cosmere.client.render.curio.model.SpikeModel;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.constants.Roshar;
import leaf.cosmere.manifestation.feruchemy.FeruchemyAtium;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.registry.ContainersRegistry;
import leaf.cosmere.registry.EntityRegistry;
import leaf.cosmere.utils.helpers.LogHelper;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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

@Mod.EventBusSubscriber(modid = Cosmere.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{

	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
		evt.registerLayerDefinition(LayerDefinitions.SPIKE, SpikeModel::createLayer);
		evt.registerLayerDefinition(LayerDefinitions.BRACELET, BraceletModel::createLayer);
		//evt.registerLayerDefinition(CuriosLayerDefinitions.NECKLACE, NecklaceModel::createLayer);

		//shardplate
		evt.registerLayerDefinition(LayerDefinitions.SHARDPLATE, ShardplateModel::createBodyLayer);
	}


	@SubscribeEvent
	public static void addLayers(EntityRenderersEvent.AddLayers evt)
	{
		addPlayerLayer(evt, "default");
		addPlayerLayer(evt, "slim");
		CosmereRenderers.load();
	}

	private static void addPlayerLayer(EntityRenderersEvent.AddLayers evt, String skin) {
		EntityRenderer<? extends Player> renderer = evt.getSkin(skin);

		if (renderer instanceof LivingEntityRenderer livingRenderer)
		{
			livingRenderer.addLayer(new CosmereLayer<>(livingRenderer));
		}
	}

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		MinecraftForge.EVENT_BUS.addListener(ClientSetup::onRenderNameplateEvent);
		event.enqueueWork(() ->
		{
			ContainersRegistry.registerGUIFactories();
		});

		//special thank you to @Random on the forge discord who told me that you have to tell the block it has transparency
		RenderType cutoutMipped = RenderType.cutoutMipped();
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (!metalType.hasOre())
			{
				continue;
			}

			ItemBlockRenderTypes.setRenderLayer(metalType.getOreBlock(), cutoutMipped);
			ItemBlockRenderTypes.setRenderLayer(metalType.getDeepslateOreBlock(), cutoutMipped);
		}
		for (Roshar.Gemstone gemstone : Roshar.Gemstone.values())
		{
			ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.GEM_ORE.get(gemstone).get(), cutoutMipped);
			ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.GEM_ORE_DEEPSLATE.get(gemstone).get(), cutoutMipped);
		}

		CosmereRenderers.register();

		LogHelper.info("Client setup complete!");
	}

	public static void onRenderNameplateEvent(RenderNameTagEvent event)
	{
		if (!(event.getEntity() instanceof LivingEntity livingEntity))
		{
			return;
		}

		MobEffectInstance effectInstance = livingEntity.getEffect(Metals.MetalType.DURALUMIN.getStoringEffect());
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

	@SubscribeEvent
	public static void RegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(EntityRegistry.COIN_PROJECTILE.get(), ThrownItemRenderer::new);
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

		event.addSprite(ResourceLocationHelper.prefix("icon/blank"));
		event.addSprite(ResourceLocationHelper.prefix("icon/arrow_up"));
		event.addSprite(ResourceLocationHelper.prefix("icon/arrow_down"));
		event.addSprite(ResourceLocationHelper.prefix("icon/on"));
		event.addSprite(ResourceLocationHelper.prefix("icon/off"));

		event.addSprite(ResourceLocationHelper.prefix("icon/allomancy"));
		event.addSprite(ResourceLocationHelper.prefix("icon/feruchemy"));
		event.addSprite(ResourceLocationHelper.prefix("icon/surgebinding"));

		for (final Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (!metalType.hasAssociatedManifestation())
			{
				continue;
			}

			String metalToLower = metalType.toString().toLowerCase(Locale.ROOT);
			event.addSprite(ResourceLocationHelper.prefix("icon/allomancy/" + metalToLower));
			event.addSprite(ResourceLocationHelper.prefix("icon/feruchemy/" + metalToLower));
		}
	}

}
