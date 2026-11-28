/*
 * File updated ~ 27 - 4 - 2026 ~ Leaf
 */

package leaf.cosmere.allomancy.common.compat.jei;

import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.api.Constants;
import leaf.cosmere.common.compat.jei.CustomDataSubtypeInterpreter;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class AllomancyJEICompat implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return Constants.Resources.JEI_ALLOMANCY;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration)
	{
		registration.registerSubtypeInterpreter(AllomancyItems.METAL_VIAL.asItem(), CustomDataSubtypeInterpreter.ALL);
	}
}
