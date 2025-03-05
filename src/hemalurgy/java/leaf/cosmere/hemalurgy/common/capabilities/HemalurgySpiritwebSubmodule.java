/*
 * File updated ~ 5 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.capabilities;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.hemalurgy.common.config.HemalurgyConfigs;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class HemalurgySpiritwebSubmodule implements ISpiritwebSubmodule
{
	@Override
	public void tickServer(ISpiritweb spiritweb)
	{
		//check player for spiritweb integrity?

		final int tickToCheck = HemalurgyConfigs.SERVER.SPIRITWEB_INTEGRITY_TICK_CHECK.get();
		if (spiritweb.getLiving().tickCount % tickToCheck == 0)
		{
			AttributeInstance attributeInstance = spiritweb.getLiving().getAttribute(HemalurgyAttributes.SPIRITWEB_INTEGRITY.get());
			if (attributeInstance != null)
			{
				if (attributeInstance.getValue() < 0)
				{
					//ideas:
					//nausea
					//slowness
					//darkness
					//blindness
					//random messages

				}
			}
		}
	}

	@Override
	public void drainInvestiture(ISpiritweb data, double strength)
	{

	}
}
