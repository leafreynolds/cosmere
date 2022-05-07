/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.compat.hwyla;

import mcp.mobius.waila.api.*;
import net.minecraft.world.entity.LivingEntity;

@WailaPlugin
public class HwylaCompat implements IWailaPlugin
{
	@Override
	public void registerClient(IWailaClientRegistration registration)
	{
		registration.registerComponentProvider(SpiritWebTooltip.INSTANCE, TooltipPosition.BODY, LivingEntity.class);
	}
}
