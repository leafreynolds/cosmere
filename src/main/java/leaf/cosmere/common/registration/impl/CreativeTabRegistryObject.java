/*
 * File updated ~ 9 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CreativeTabRegistryObject extends WrappedRegistryObject<CreativeModeTab>
{

	public CreativeTabRegistryObject(DeferredHolder<? super CreativeModeTab, CreativeModeTab> registryObject)
	{
		super(registryObject);
	}
}
