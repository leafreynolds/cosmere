/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.compat.jei;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.compat.jei.CustomDataSubtypeInterpreter;
import leaf.cosmere.hemalurgy.common.items.IHemalurgicInfo;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.nbt.CompoundTag;
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
	public void registerItemSubtypes(ISubtypeRegistration registration)
	{
		// Spikes carry a random "stolen_identity_tag" UUID per filled stack; ignore it so
		// every attuned spike doesn't become its own JEI subtype.
		final CustomDataSubtypeInterpreter spikeInterpreter = new CustomDataSubtypeInterpreter(tag ->
		{
			CompoundTag copy = tag.copy();
			copy.remove(IHemalurgicInfo.stolen_identity_tag);
			return copy;
		});
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
