/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import net.minecraft.world.entity.LivingEntity;

public class FeruchemyEffectBase extends CosmereEffect implements IHasMetalType
{
	protected final Metals.MetalType metalType;

	public FeruchemyEffectBase(Metals.MetalType type)
	{
		super();
		metalType = type;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}


	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, double strength)
	{
		if (entityLivingBaseIn.level.isClientSide)
		{
			//client side only?
		}
	}

}
