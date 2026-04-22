/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviderType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class IntProviderTypeRegistryObject<PROVIDER extends IntProvider> extends WrappedRegistryObject<IntProviderType<PROVIDER>>
{

	public IntProviderTypeRegistryObject(DeferredHolder<?, IntProviderType<PROVIDER>> registryObject)
	{
		super(registryObject);
	}
}
