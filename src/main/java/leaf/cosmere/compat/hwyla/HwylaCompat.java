/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.compat.hwyla;

import net.minecraft.world.entity.LivingEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class HwylaCompat implements IWailaPlugin
{
	@Override
	public void registerClient(IWailaClientRegistration registration)
	{
		registration.registerEntityComponent(SpiritWebTooltip.INSTANCE, LivingEntity.class);
	}
}
