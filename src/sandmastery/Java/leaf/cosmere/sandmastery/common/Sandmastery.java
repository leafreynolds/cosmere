package leaf.cosmere.sandmastery.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;

@Mod(Sandmastery.MODID)
public class Sandmastery implements IModModule
{
    public static final String MODID = "sandmastery";
    public static Sandmastery instance;
    public final Version versionNumber;

    public Sandmastery() {
        Cosmere.addModule(instance = this);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onConfigLoad);
        modBus.addListener(this::commonSetup);

        SandmasteryItems.ITEMS.register(modBus);
        SandmasteryAttributes.ATTRIBUTES.register(modBus);
        SandmasteryManifestations.MANIFESTATIONS.register(modBus);

        //Set our version number to match the mods.toml file, which matches the one in our build.gradle
        versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(Sandmastery.MODID, path);
    }

    @Override
    public Version getVersion() { return versionNumber; }

    @Override
    public String getName() { return MODID; }

    @Nullable
    @Override
    public ISpiritwebSubmodule makeSubmodule()
    {
        return new SandmasterySpiritwebSubmodule();
    }

    private void onConfigLoad(ModConfigEvent configEvent)
    {
        ModConfig config = configEvent.getConfig();
        if (config.getModId().equals(MODID))
        {

        }

    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        CosmereAPI.logger.info("Cosmere: Sand Mastery module Version {} initializing...", versionNumber);

        event.enqueueWork(() ->
        {
        });
    }
}