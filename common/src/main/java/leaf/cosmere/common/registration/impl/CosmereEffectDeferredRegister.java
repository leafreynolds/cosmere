/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.providers.ICosmereEffectProvider;
import leaf.cosmere.common.registration.WrappedDeferredRegister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class CosmereEffectDeferredRegister extends WrappedDeferredRegister<CosmereEffect>
{
	private final List<ICosmereEffectProvider> effectsInRegistry = new ArrayList<>();


	public CosmereEffectDeferredRegister(String modid)
	{
		super(modid, CosmereAPI.cosmereEffectRegistryName());
	}

	public <COSMERE_EFFECT extends CosmereEffect> CosmereEffectRegistryObject<COSMERE_EFFECT> register(String name, Supplier<? extends COSMERE_EFFECT> sup)
	{
		final CosmereEffectRegistryObject<COSMERE_EFFECT> registeredItem = register(name, sup, CosmereEffectRegistryObject::new);
		effectsInRegistry.add(registeredItem);
		return registeredItem;
	}


	public List<ICosmereEffectProvider> getEffectsInRegistry()
	{
		return Collections.unmodifiableList(effectsInRegistry);
	}
}
