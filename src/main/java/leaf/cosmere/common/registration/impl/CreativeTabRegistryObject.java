/*
 * File updated ~ 9 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabRegistryObject extends WrappedRegistryObject<CreativeModeTab>
{

	public CreativeTabRegistryObject(RegistryObject<CreativeModeTab> registryObject)
	{
		super(registryObject);
	}
}