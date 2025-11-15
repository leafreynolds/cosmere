/*
 * File updated ~ 5 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.capabilities;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.hemalurgy.common.config.HemalurgyConfigs;
import leaf.cosmere.hemalurgy.common.config.HemalurgyServerConfig;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyAttributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

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
				int intensity = (int) attributeInstance.getValue();
				if (intensity < HemalurgyAttributes.SPIRITWEB_INTEGRITY.getAttribute().getDefaultValue())
				{
					// bloodlust effect? whispers? having hemalurgy at all should probably result in some weird stuff
				}
				if (intensity < 0)
				{
					if (spiritweb.getLiving() instanceof Player player)
					{
						//ideas:
						//nausea
						//slowness
						//darkness
						//blindness
						//random messages
						switch (intensity)
						{
							default:	// illegal? perhaps
							case -3:
								player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, tickToCheck*2, (intensity*-1)-3, false, false, false)); // confusion = nausea
							case -2:
								player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, tickToCheck*2, (intensity*-1)-2, false, false, false));
							case -1:
								player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, tickToCheck*2, (intensity*-1)-1, false, false, false));
								break;
						}
					}
				}
			}
		}
	}

	@Override
	public void drainInvestiture(ISpiritweb data, double strength)
	{

	}
}
