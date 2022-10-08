/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.effects;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.effects.MobEffectBase;
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
