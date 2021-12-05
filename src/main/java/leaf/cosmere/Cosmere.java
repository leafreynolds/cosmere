/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.client.ClientSetup;
import leaf.cosmere.compat.curios.CuriosCompat;
import leaf.cosmere.compat.curios.PatchouliCompat;
import leaf.cosmere.handlers.ColorHandler;
import leaf.cosmere.utils.helpers.LogHelper;
import leaf.cosmere.network.Network;
import leaf.cosmere.registry.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
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


        MinecraftForge.EVENT_BUS.register(this);

        //Register our deferred registries
        BlocksRegistry.BLOCKS.register(modBus);
        ItemsRegistry.ITEMS.register(modBus);
        EffectsRegistry.EFFECTS.register(modBus);
        LootModifierRegistry.LOOT_MODIFIERS.register(modBus);
        ManifestationRegistry.MANIFESTATIONS.register(modBus);
        AttributesRegistry.ATTRIBUTES.register(modBus);
        EntityRegistry.ENTITIES.register(modBus);

        FeatureRegistry.FEATURES.register(modBus);
        RecipeRegistry.SPECIAL_RECIPES.register(modBus);

        AdvancementTriggerRegistry.init();

        Network.init();

        // init cross mod compatibility stuff, if relevant
        CuriosCompat.init();
        PatchouliCompat.init();
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            FeatureRegistry.registerConfiguredFeatures();
            EntityRegistry.PrepareEntityAttributes();
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
