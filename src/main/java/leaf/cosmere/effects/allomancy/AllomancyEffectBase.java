/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.allomancy;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.IHasMetalType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AllomancyEffectBase extends MobEffect implements IHasMetalType
{
	protected final Metals.MetalType metalType;

	public AllomancyEffectBase(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(effectType, type.getColorValue());
		metalType = type;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}


	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
	{
		if (entityLivingBaseIn.level.isClientSide)
		{
			//client side only?
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		//assume we can apply the effect regardless
		boolean result = true;
/*
        //but if we are a specific effect
        if (metalType == Metals.MetalType.BENDALLOY)
        {
            int k = 50 >> amplifier;
            if (k > 0)
            {
                result = duration % k == 0;
            }
        }*/
		return result;
	}
}
