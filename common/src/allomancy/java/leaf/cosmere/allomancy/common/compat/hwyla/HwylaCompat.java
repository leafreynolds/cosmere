/*
 * File updated ~ 8 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.compat.hwyla;

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
		registration.registerEntityComponent(BronzeSeekerTooltip.INSTANCE, LivingEntity.class);
	}
}
