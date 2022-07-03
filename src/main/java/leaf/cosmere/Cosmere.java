/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.commands.CosmereCommand;
import leaf.cosmere.compat.curios.CuriosCompat;
import leaf.cosmere.compat.patchouli.PatchouliCompat;
import leaf.cosmere.datagen.biome.BiomeFeatureModifier;
import leaf.cosmere.handlers.ColorHandler;
import leaf.cosmere.network.Network;
import leaf.cosmere.registry.*;
import leaf.cosmere.utils.helpers.LogHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
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
		modBus.addListener(this::onAddCaps);

/*		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init));
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::RegisterRenderers));
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::registerIconTextures));
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::retrieveRegisteredIconSprites));*/


		MinecraftForge.EVENT_BUS.register(this);

		//Register our deferred registries
		ManifestationRegistry.MANIFESTATIONS.register(modBus);
		AttributesRegistry.ATTRIBUTES.register(modBus);
		BlocksRegistry.BLOCKS.register(modBus);
		ItemsRegistry.ITEMS.register(modBus);
		EffectsRegistry.EFFECTS.register(modBus);
		LootModifierRegistry.LOOT_MODIFIERS.register(modBus);
		EntityRegistry.ENTITIES.register(modBus);
		PointOfInterestRegistry.POINT_OF_INTERESTS.register(modBus);
		VillagerProfessionRegistry.VILLAGE_PROFESSIONS.register(modBus);
		ContainersRegistry.CONTAINERS.register(modBus);
		BiomeRegistry.BIOME_MODIFIERS.register(modBus);
		BiomeRegistry.BIOME_MODIFIERS.register(BiomeFeatureModifier.ADD_FEATURE.getPath(), BiomeFeatureModifier::makeCodec);

		FeatureRegistry.CONFIGURED_FEATURES.register(modBus);
		FeatureRegistry.PLACED_FEATURES.register(modBus);

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
			CosmereCommand.registerCustomArgumentTypes();
			EntityRegistry.PrepareEntityAttributes();
			LootFunctionRegistry.Register();
		});

		DataSerializersRegistry.register();

		LogHelper.info("Common setup complete!");
	}

	private void onAddCaps(RegisterCapabilitiesEvent capabilitiesEvent)
	{
		capabilitiesEvent.register(ISpiritweb.class);
	}

	private void loadComplete(FMLLoadCompleteEvent event)
	{
		event.enqueueWork(() ->
		{
			ColorHandler.init();

		});
	}
}
