/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.providers;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

@MethodsReturnNonnullByDefault
public interface IAttributeProvider extends IBaseProvider
{

	Attribute getAttribute();

	@Override
	default ResourceLocation getRegistryName()
	{
		return BuiltInRegistries.ATTRIBUTE.getKey(getAttribute());
	}

	@Override
	default String getTranslationKey()
	{
		return getAttribute().getDescriptionId();
	}
}
