/*
 * File updated ~ 18 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.effects;

import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.damagesource.DamageSource;

public class DehydratedEffect extends CosmereEffect
{
	public static final DamageSource DEHYDRATED = (new DamageSource("dehydrated")).bypassArmor().bypassMagic().bypassEnchantments();

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
		data.getLiving().hurt(DEHYDRATED, 4.0F);
	}
}
