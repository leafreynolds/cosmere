/*
 * File updated ~ 24 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.capabilities;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class HemalurgySpiritwebSubmodule implements ISpiritwebSubmodule
{
	@Override
	public void tickServer(ISpiritweb spiritweb)
	{
		//check player for spiritweb integrity?

		//todo config integrity tick check
		final int tickToCheck = 999;
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
}
