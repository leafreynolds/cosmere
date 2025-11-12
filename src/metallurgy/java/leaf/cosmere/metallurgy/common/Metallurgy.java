package leaf.cosmere.metallurgy.common;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IModModule;
import leaf.cosmere.api.Version;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.metallurgy.common.network.MetallurgyPacketHandler;
import leaf.cosmere.metallurgy.common.registries.MetallurgyBlockEntities;
import leaf.cosmere.metallurgy.common.registries.MetallurgyBlocks;
import leaf.cosmere.metallurgy.common.registries.MetallurgyCreativeTabs;
import leaf.cosmere.metallurgy.common.registries.MetallurgyItems;
import leaf.cosmere.metallurgy.common.registries.MetallurgyMenuTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Metallurgy.MODID)
public class Metallurgy implements IModModule {
    public static final String MODID = "metallurgy";
    public static Metallurgy instance;
    public final Version versionNumber;
    private final MetallurgyPacketHandler packetHandler;

    public Metallurgy() {
        Cosmere.addModule(instance = this);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::commonSetup);

        MetallurgyItems.ITEMS.register(modBus);
        MetallurgyBlocks.BLOCKS.register(modBus);
        MetallurgyBlockEntities.BLOCK_ENTITIES.register(modBus);
        MetallurgyCreativeTabs.CREATIVE_TABS.register(modBus);
        MetallurgyMenuTypes.MENU_TYPES.register(modBus);

        // Set our version number to match the mods.toml file, which matches the one in
        // our build.gradle
        this.versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
        this.packetHandler = new MetallurgyPacketHandler();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(Metallurgy.MODID, path);
    }

    @Override
    public Version getVersion() {
        return versionNumber;
    }

    @Override
    public String getName() {
        return "Metallurgy";
    }

    @Override
    public leaf.cosmere.api.ISpiritwebSubmodule makeSubmodule() {
        // No spiritweb submodule needed for metallurgy at this time
        return null;
    }

    public static MetallurgyPacketHandler packetHandler() {
        return instance.packetHandler;
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        CosmereAPI.logger.info("Cosmere: Metallurgy module Version {} initializing...", versionNumber);

        event.enqueueWork(() -> {
        });

        packetHandler.initialize();
    }
}
