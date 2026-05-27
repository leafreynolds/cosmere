package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ConfiguredFeatureRegistryObject<CONFIGURED_FEATURE extends ConfiguredFeature<?, ?>> extends WrappedRegistryObject<CONFIGURED_FEATURE>
{
	public ConfiguredFeatureRegistryObject(DeferredHolder<? super CONFIGURED_FEATURE, CONFIGURED_FEATURE> registryObject)
	{
		super(registryObject);
	}
}
