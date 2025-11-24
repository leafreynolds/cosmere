/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.compat.jei;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@JeiPlugin
public class FeruchemyJEICompat implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return Constants.Resources.JEI_FERUCHEMY;
	}

	@Override
	public void registerRecipes(IRecipeRegistration reg)
	{
		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			if (metalType.hasFeruchemicalEffect())
			{
				switch(metalType)
				{
					case NICROSIL:
						addItemInfoPage(reg, FeruchemyItems.NICROSIL_METAL_NECKLACE.asItem());
						addItemInfoPage(reg, FeruchemyItems.NICROSIL_METAL_RING.asItem());
						addItemInfoPage(reg, FeruchemyItems.NICROSIL_METAL_NECKLACE.asItem());
						break;
					default:
						addItemInfoPage(reg, FeruchemyItems.METAL_NECKLACES.get(metalType).asItem());
						addItemInfoPage(reg, FeruchemyItems.METAL_RINGS.get(metalType).asItem());
						addItemInfoPage(reg, FeruchemyItems.METAL_BRACELETS.get(metalType).asItem());
				}
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
