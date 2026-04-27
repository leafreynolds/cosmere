/*
 * File updated ~ 27 - 4 - 2026 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.compat.jei;

import leaf.cosmere.api.Constants;
import leaf.cosmere.common.compat.jei.CustomDataSubtypeInterpreter;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class SandmasteryJEICompat implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return Constants.Resources.JEI_SANDMASTERY;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration)
	{
		registration.registerSubtypeInterpreter(SandmasteryItems.QIDO_ITEM.asItem(), CustomDataSubtypeInterpreter.ALL);
	}
}