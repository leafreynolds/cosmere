/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.providers;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

@MethodsReturnNonnullByDefault
public interface IAttributeProvider extends IBaseProvider
{

	Attribute getAttribute();

	@Override
	default ResourceLocation getRegistryName()
	{
		return ForgeRegistries.ATTRIBUTES.getKey(getAttribute());
	}

	@Override
	default String getTranslationKey()
	{
		return getAttribute().getDescriptionId();
	}
}