/*
 * File updated ~ 8 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.providers.ICosmereEffectProvider;
import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class CosmereEffectRegistryObject<COSMERE_EFFECT extends CosmereEffect> extends WrappedRegistryObject<COSMERE_EFFECT> implements ICosmereEffectProvider
{
	public CosmereEffectRegistryObject(DeferredHolder<?, COSMERE_EFFECT> registryObject)
	{
		super(registryObject);
	}

	@NotNull
	@Override
	public COSMERE_EFFECT getEffect()
	{
		return get();
	}

}
