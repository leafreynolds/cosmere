/*
 * File updated ~ 8 - 10 - 2023 ~ Leaf
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

import java.util.Objects;

public class CosmereAPI
{
	private CosmereAPI()
	{
	}

	public static final String COSMERE_MODID = "cosmere";

	public static final Logger logger = LogUtils.getLogger();

	@NotNull
	private static <T> Lazy<ResourceKey<? extends Registry<T>>> registryKey(@SuppressWarnings("unused") @NotNull Class<T> compileTimeTypeValidator, @NotNull String path)
	{
		return Lazy.of(() -> ResourceKey.createRegistryKey(Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(COSMERE_MODID, path))));
	}

	@NotNull
	private static final Lazy<ResourceKey<? extends Registry<Manifestation>>> MANIFESTATION_REGISTRY_NAME = registryKey(Manifestation.class, "manifestation");
	private static Registry<Manifestation> MANIFESTATION_REGISTRY;

	@NotNull
	public static ResourceKey<? extends Registry<Manifestation>> manifestationRegistryName()
	{
		return MANIFESTATION_REGISTRY_NAME.get();
	}


	@NotNull
	public static Registry<Manifestation> manifestationRegistry()
	{
		if (MANIFESTATION_REGISTRY == null)
		{
			MANIFESTATION_REGISTRY = (Registry<Manifestation>) BuiltInRegistries.REGISTRY.get(manifestationRegistryName().location());
		}
		return MANIFESTATION_REGISTRY;
	}

	@NotNull
	private static final Lazy<ResourceKey<? extends Registry<CosmereEffect>>> COSMERE_EFFECT_REGISTRY_NAME = registryKey(CosmereEffect.class, "cosmere_effect");
	private static Registry<CosmereEffect> COSMERE_EFFECT_REGISTRY;

	@NotNull
	public static ResourceKey<? extends Registry<CosmereEffect>> cosmereEffectRegistryName()
	{
		return COSMERE_EFFECT_REGISTRY_NAME.get();
	}


	@NotNull
	public static Registry<CosmereEffect> cosmereEffectRegistry()
	{
		if (COSMERE_EFFECT_REGISTRY == null)
		{
			COSMERE_EFFECT_REGISTRY = (Registry<CosmereEffect>) BuiltInRegistries.REGISTRY.get(cosmereEffectRegistryName().location());
		}
		return COSMERE_EFFECT_REGISTRY;
	}
}
