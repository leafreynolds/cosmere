/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.providers;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

@MethodsReturnNonnullByDefault
public interface IEntityTypeProvider extends IBaseProvider
{

	EntityType<?> getEntityType();

	@Override
	default ResourceLocation getRegistryName()
	{
		return BuiltInRegistries.ENTITY_TYPE.getKey(getEntityType());
	}

	@Override
	default Component getTextComponent()
	{
		return getEntityType().getDescription();
	}

	@Override
	default String getTranslationKey()
	{
		return getEntityType().getDescriptionId();
	}

}
