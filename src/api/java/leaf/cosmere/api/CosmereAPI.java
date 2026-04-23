/*
 * File updated ~ 2026-04-23 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.api;

import com.mojang.logging.LogUtils;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class CosmereAPI
{
	private CosmereAPI()
	{
	}

	public static final String COSMERE_MODID = "cosmere";

	public static final Logger logger = LogUtils.getLogger();

	@NotNull
	private static <T> Lazy<ResourceKey<Registry<T>>> registryKey(@SuppressWarnings("unused") @NotNull Class<T> compileTimeTypeValidator, @NotNull String path)
	{
		return Lazy.of(() -> ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(COSMERE_MODID, path)));
	}

	@NotNull
	private static final Lazy<ResourceKey<Registry<Manifestation>>> MANIFESTATION_REGISTRY_NAME = registryKey(Manifestation.class, "manifestation");

	@NotNull
	public static ResourceKey<Registry<Manifestation>> manifestationRegistryName()
	{
		return MANIFESTATION_REGISTRY_NAME.get();
	}

	@SuppressWarnings("unchecked")
	public static Registry<Manifestation> manifestationRegistry()
	{
		return (Registry<Manifestation>) BuiltInRegistries.REGISTRY.get(manifestationRegistryName().location());
	}

	@NotNull
	private static final Lazy<ResourceKey<Registry<CosmereEffect>>> COSMERE_EFFECT_REGISTRY_NAME = registryKey(CosmereEffect.class, "cosmere_effect");

	@NotNull
	public static ResourceKey<Registry<CosmereEffect>> cosmereEffectRegistryName()
	{
		return COSMERE_EFFECT_REGISTRY_NAME.get();
	}

	@SuppressWarnings("unchecked")
	public static Registry<CosmereEffect> cosmereEffectRegistry()
	{
		return (Registry<CosmereEffect>) BuiltInRegistries.REGISTRY.get(cosmereEffectRegistryName().location());
	}
}
