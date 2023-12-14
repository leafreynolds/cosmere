/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.compat.jei;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@JeiPlugin
public class HemalurgyJEICompat implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return Constants.Resources.JEI_HEMALURGY;
	}


	@Override
	public void registerRecipes(IRecipeRegistration reg)
	{
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasHemalurgicEffect())
			{
				addItemInfoPage(reg, HemalurgyItems.METAL_SPIKE.get(metalType).asItem());
			}
		}
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
