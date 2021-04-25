/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.client.ClientEvents;
import leaf.cosmere.client.ClientSetup;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.handlers.ColorHandler;
import leaf.cosmere.helpers.LogHelper;
import leaf.cosmere.network.Network;
import leaf.cosmere.registry.*;
import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.compat.curios.CuriosCompat;
import leaf.cosmere.handlers.HUDHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Cosmere.MODID)
public class Cosmere
{
    public static final String MODID = "cosmere";

    public Cosmere()
    {
        LogHelper.info("Registering Cosmere related mcgubbins!");
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::commonSetup);
        modBus.addListener(this::loadComplete);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::registerIconTextures));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::retrieveRegisteredIconSprites));

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(HUDHandler::onDrawScreenPost);

        MinecraftForge.EVENT_BUS.register(this);

        //Register our deferred registries
        BlocksRegistry.BLOCKS.register(modBus);
        ItemsRegistry.ITEMS.register(modBus);
        EffectsRegistry.EFFECTS.register(modBus);
        LootModifierRegistry.LOOT_MODIFIERS.register(modBus);
        AttributesRegistry.ATTRIBUTES.register(modBus);
        ManifestationRegistry.MANIFESTATIONS.register(modBus);

        FeatureRegistry.FEATURES.register(modBus);

        AdvancementTriggerRegistry.init();

        Network.init();

        // init cross mod compatibility stuff, if relevant
        CuriosCompat.init();
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            FeatureRegistry.registerConfiguredFeatures();
        });

        //Entity Caps
        CapabilityManager.INSTANCE.register(ISpiritweb.class, new ISpiritweb.Storage(), () -> new SpiritwebCapability(null));

        DataSerializersRegistry.register();

        LogHelper.info("Common setup complete!");
    }

    private void loadComplete(FMLLoadCompleteEvent event)
    {
        event.enqueueWork(() ->
        {
            ColorHandler.init();

        });
    }
}
