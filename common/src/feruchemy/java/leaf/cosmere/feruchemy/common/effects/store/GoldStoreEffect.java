/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

//health
public class GoldStoreEffect extends FeruchemyEffectBase
{
	public GoldStoreEffect(Metals.MetalType type)
	{
		super(type);
		addAttributeModifier(
				Attributes.MAX_HEALTH,
				-4.0D,
				AttributeModifier.Operation.ADDITION);
		addAttributeModifier(
				AttributesRegistry.HEALING_STRENGTH.getAttribute(),
				-1.0D,
				AttributeModifier.Operation.ADDITION);
	}

	@Override
	public void applyEffectTick(ISpiritweb data, double strength)
	{
		final LivingEntity living = data.getLiving();

		if (living.getHealth() > living.getMaxHealth())
		{
			living.setHealth(living.getMaxHealth());
		}
	}
}
