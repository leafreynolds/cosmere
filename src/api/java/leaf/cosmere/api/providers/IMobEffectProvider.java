/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.providers;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

@MethodsReturnNonnullByDefault
public interface IMobEffectProvider extends IBaseProvider
{

	MobEffect getMobEffect();

	@Override
	default ResourceLocation getRegistryName()
	{
		return BuiltInRegistries.MOB_EFFECT.getKey(getMobEffect());
	}

	@Override
	default String getTranslationKey()
	{
		return getMobEffect().getDescriptionId();
	}
}
