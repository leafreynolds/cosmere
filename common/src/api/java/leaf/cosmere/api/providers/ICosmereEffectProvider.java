/*
 * File updated ~ 28 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.api.providers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import net.minecraft.resources.ResourceLocation;

public interface ICosmereEffectProvider extends IBaseProvider
{
	CosmereEffect getEffect();

	@Override
	default ResourceLocation getRegistryName()
	{
		return CosmereAPI.cosmereEffectRegistry().getKey(getEffect());
	}

	@Override
	default String getTranslationKey()
	{
		return getEffect().getTranslationKey();
	}
}
