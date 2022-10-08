/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.providers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.resources.ResourceLocation;

public interface IManifestationProvider extends IBaseProvider
{
	Manifestation getManifestation();

	@Override
	default ResourceLocation getRegistryName()
	{
		return CosmereAPI.manifestationRegistry().getKey(getManifestation());
	}


	@Override
	default String getTranslationKey()
	{
		return getManifestation().getTranslationKey();
	}
}
