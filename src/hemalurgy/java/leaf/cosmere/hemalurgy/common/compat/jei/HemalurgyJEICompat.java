/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.compat.jei;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyDataComponents;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

@JeiPlugin
public class HemalurgyJEICompat implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return Constants.Resources.JEI_HEMALURGY;
	}


	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration)
	{
		//ignore the random identity uuid or every spike becomes its own subtype
		//stored powers and charge still count
		final ISubtypeInterpreter<ItemStack> spikeInterpreter = new ISubtypeInterpreter<>()
		{
			@Override
			public Object getSubtypeData(ItemStack ingredient, UidContext context)
			{
				if (context == UidContext.Recipe)
				{
					return null;
				}
				CompoundTag data = ingredient.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
				CompoundTag powers = ingredient.get(HemalurgyDataComponents.SPIKE_POWERS.get());
				if (powers != null && !powers.isEmpty())
				{
					data.put("spike_powers", powers.copy());
				}
				return data.isEmpty() ? null : data;
			}

			@Override
			public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context)
			{
				Object data = getSubtypeData(ingredient, context);
				return data == null ? "" : data.toString();
			}
		};
		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			if (metalType.hasHemalurgicEffect())
			{
				registration.registerSubtypeInterpreter(HemalurgyItems.METAL_SPIKE.get(metalType).asItem(), spikeInterpreter);
			}
		}
	}

	@Override
	public void registerRecipes(IRecipeRegistration reg)
	{
		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
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
