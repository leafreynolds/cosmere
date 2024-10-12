/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.effects;

import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.LivingEntity;

public class DehydratedEffect extends CosmereEffect
{
	public DehydratedEffect()
	{
		super();
	}

	@Override
	protected int getActiveTick()
	{
		//every 4 seconds
		return 80;
	}

	@Override
	protected int getTickOffset()
	{
		//offset tick to living tick count + 19
		return -1;
	}

	@Override
	public void applyEffectTick(ISpiritweb data, double strength)
	{
		final LivingEntity living = data.getLiving();
		//todo swap out dryout damage source
		living.hurt(living.damageSources().dryOut(), 4.0F);
	}
}
