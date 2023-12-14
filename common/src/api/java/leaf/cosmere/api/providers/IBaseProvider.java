/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.providers;

import leaf.cosmere.api.text.IHasTextComponent;
import leaf.cosmere.api.text.IHasTranslationKey;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@MethodsReturnNonnullByDefault
public interface IBaseProvider extends IHasTextComponent, IHasTranslationKey
{
	ResourceLocation getRegistryName();

	default String getName()
	{
		return getRegistryName().getPath();
	}


	@Override
	default Component getTextComponent()
	{
		return Component.translatable(getTranslationKey());
	}

}