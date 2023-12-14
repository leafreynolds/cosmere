/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.text;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

@MethodsReturnNonnullByDefault
public interface IHasTextComponent
{
	Component getTextComponent();
}