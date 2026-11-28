/*
 * File updated ~ 7 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.client.eventHandlers;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.sandmastery.client.gui.SandPouchContainerScreen;
import leaf.cosmere.sandmastery.client.gui.SandSpreaderScreen;
import leaf.cosmere.sandmastery.client.render.DiemCheckpointRenderer;
import leaf.cosmere.sandmastery.client.render.DiemStoneRenderer;
import leaf.cosmere.sandmastery.client.render.DiemTargetRenderer;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlockEntitiesRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.blocks.entities.SandSpreader.SandSpreaderMenu;
import leaf.cosmere.sandmastery.common.items.sandpouch.SandPouchContainerMenu;
import leaf.cosmere.sandmastery.common.registries.SandmasteryEntityTypes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import leaf.cosmere.sandmastery.common.registries.SandmasteryMenuTypes;
import leaf.cosmere.sandmastery.common.utils.SandmasteryConstants;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Sandmastery.MODID, value = Dist.CLIENT)
public class SandmasteryModClientEvents
{


	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		event.enqueueWork(() ->
		{
			ItemProperties.register(
				SandmasteryItems.SAND_JAR_ITEM.asItem(),
				ResourceLocation.fromNamespaceAndPath("cosmere", "charge_level"),
				(stack, level, entity, seed) -> 100f * StackNBTHelper.getInt(stack, Constants.NBT.CHARGE_LEVEL, 0) / SandmasteryConstants.CHARGE_PER_SAND_LAYER);
		});


		CosmereAPI.logger.info("Sandmastery client setup complete!");
	}


	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block event)
	{
		event.register((state, level, pos, tintIndex) -> level != null && pos != null
		                                                 ? BiomeColors.getAverageWaterColor(level, pos)
		                                                 : DEFAULT_WATER_COLOR,
				SandmasteryBlocks.WATER_DRIPPER.getBlock());
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event)
	{
		event.register((stack, tintIndex) -> 0xFF000000 | DEFAULT_WATER_COLOR, SandmasteryBlocks.WATER_DRIPPER.asItem());
	}

	private static final int DEFAULT_WATER_COLOR = 0x3F76E4;

	@SubscribeEvent
	public static void registerMenuScreens(RegisterMenuScreensEvent event)
	{
		event.register((MenuType<SandPouchContainerMenu>) SandmasteryMenuTypes.SAND_POUCH.get(), SandPouchContainerScreen::new);
		event.register((MenuType<SandSpreaderMenu>) SandmasteryMenuTypes.SAND_SPREADER.get(), SandSpreaderScreen::new);
		CosmereAPI.logger.info("Sandmastery registered menutypes!");
	}

	@SubscribeEvent
	public static void RegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(SandmasteryEntityTypes.SAND_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(SandmasteryEntityTypes.DIEM_TARGET.get(), DiemTargetRenderer::new);
		event.registerBlockEntityRenderer(SandmasteryBlockEntitiesRegistry.DIEM_CHECKPOINT_BE.get(), DiemCheckpointRenderer::new);
		event.registerBlockEntityRenderer(SandmasteryBlockEntitiesRegistry.DIEM_STONE_BE.get(), DiemStoneRenderer::new);
	}

	// models that no block uses directly, so they aren't loaded unless asked for
	@SubscribeEvent
	public static void registerAdditionalModels(ModelEvent.RegisterAdditional event)
	{
		DiemStoneRenderer.SAND_MODELS.values().forEach(event::register);
	}

/*	//special thank you to the chisels and bits team who have an example of how to register other sprites
	@SubscribeEvent
	public static void registerIconTextures(TextureStitchEvent.Pre event)
	{
		final TextureAtlas map = event.getAtlas();
		if (!map.location().equals(InventoryMenu.BLOCK_ATLAS))
		{
			return;
		}

		event.addSprite(Sandmastery.rl("icon/sandmastery"));

		for (final Taldain.Mastery manifestation : Taldain.Mastery.values())
		{
			String abilityToLower = manifestation.toString().toLowerCase(Locale.ROOT);
			event.addSprite(Sandmastery.rl("icon/sandmastery/" + abilityToLower));
		}
	}*/

}
