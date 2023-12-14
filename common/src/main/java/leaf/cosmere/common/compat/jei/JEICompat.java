/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.compat.jei;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.registry.ItemsRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@JeiPlugin
public class JEICompat implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return Constants.Resources.JEI;
	}

	@Override
	public void registerRecipes(IRecipeRegistration reg)
	{
		addItemInfoPage(reg, ItemsRegistry.GUIDE.get());
	}

	private void addItemInfoPage(IRecipeRegistration reg, Item item)
	{
		reg.addIngredientInfo(
				item.getDefaultInstance(),
				VanillaTypes.ITEM_STACK,
				TextHelper.createTranslatedText(String.format("item.%s.%s.tooltip", getPluginUid().getNamespace(), item)
				));
	}

}
