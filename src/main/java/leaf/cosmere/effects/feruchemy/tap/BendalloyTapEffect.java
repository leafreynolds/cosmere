/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;


public class BendalloyTapEffect extends FeruchemyEffectBase
{
	public BendalloyTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
	}


	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		//assume we can apply the effect regardless
		boolean result = true;

		int k = 50 >> amplifier + 1;
		if (k > 0)
		{
			result = duration % k == 0;
		}

		return result;
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
	{
		if (!entityLivingBaseIn.level.isClientSide)
		{
			((Player) entityLivingBaseIn).getFoodData().eat(amplifier + 1, 0.0F);

			//todo add tough as nails mod compatibility?
		}
	}
}
