/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.allomancy;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.MobEffectBase;
import leaf.cosmere.items.IHasMetalType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AllomancyEffectBase extends MobEffectBase implements IHasMetalType
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
		if (!isActiveTick(entityLivingBaseIn))
		{
			return;
		}

		if (entityLivingBaseIn.level.isClientSide)
		{
			//client side only?
		}
	}

}
