/*
 * File updated ~ 8 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.api;

import com.mojang.logging.LogUtils;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
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
	private static <T> Lazy<ResourceKey<? extends Registry<T>>> registryKey(@SuppressWarnings("unused") @NotNull Class<T> compileTimeTypeValidator, @NotNull String path)
	{
		return Lazy.of(() -> ResourceKey.createRegistryKey(new ResourceLocation(COSMERE_MODID, path)));
	}

	@NotNull
	private static final Lazy<ResourceKey<? extends Registry<Manifestation>>> MANIFESTATION_REGISTRY_NAME = registryKey(Manifestation.class, "manifestation");
	private static IForgeRegistry<Manifestation> MANIFESTATION_REGISTRY;

	@NotNull
	public static ResourceKey<? extends Registry<Manifestation>> manifestationRegistryName()
	{
		return MANIFESTATION_REGISTRY_NAME.get();
	}


	@NotNull
	public static IForgeRegistry<Manifestation> manifestationRegistry()
	{
		if (MANIFESTATION_REGISTRY == null)
		{
			MANIFESTATION_REGISTRY = RegistryManager.ACTIVE.getRegistry(manifestationRegistryName());
		}
		return MANIFESTATION_REGISTRY;
	}

	@NotNull
	private static final Lazy<ResourceKey<? extends Registry<CosmereEffect>>> COSMERE_EFFECT_REGISTRY_NAME = registryKey(CosmereEffect.class, "cosmere_effect");
	private static IForgeRegistry<CosmereEffect> COSMERE_EFFECT_REGISTRY;

	@NotNull
	public static ResourceKey<? extends Registry<CosmereEffect>> cosmereEffectRegistryName()
	{
		return COSMERE_EFFECT_REGISTRY_NAME.get();
	}


	@NotNull
	public static IForgeRegistry<CosmereEffect> cosmereEffectRegistry()
	{
		if (COSMERE_EFFECT_REGISTRY == null)
		{
			COSMERE_EFFECT_REGISTRY = RegistryManager.ACTIVE.getRegistry(cosmereEffectRegistryName());
		}
		return COSMERE_EFFECT_REGISTRY;
	}
}