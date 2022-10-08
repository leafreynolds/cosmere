package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.function.Supplier;

public class ConfiguredFeatureDeferredRegister extends WrappedDeferredRegister<ConfiguredFeature<?, ?>>
{

	public ConfiguredFeatureDeferredRegister(String modid)
	{
		super(modid, Registry.CONFIGURED_FEATURE_REGISTRY);
	}

	public <CONFIGURED_FEATURE extends ConfiguredFeature<?, ?>> ConfiguredFeatureRegistryObject<CONFIGURED_FEATURE> register(String name, Supplier<CONFIGURED_FEATURE> sup)
	{
		return register(name, sup, ConfiguredFeatureRegistryObject::new);
	}
}