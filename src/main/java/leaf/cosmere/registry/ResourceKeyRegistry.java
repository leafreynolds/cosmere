/*
 * File created ~ 7 - 5 - 2022 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ResourceKeyRegistry
{
	public static final ResourceKey<Registry<AManifestation>> MANIFESTATION_TYPES = ResourceKey.createRegistryKey(ResourceLocationHelper.prefix("manifestation_types"));

}
