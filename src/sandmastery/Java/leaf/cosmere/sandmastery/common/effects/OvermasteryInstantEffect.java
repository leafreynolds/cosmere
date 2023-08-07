package leaf.cosmere.sandmastery.common.effects;

import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class OvermasteryInstantEffect extends InstantenousMobEffect
{
	public OvermasteryInstantEffect(MobEffectCategory pCategory, int pColor)
	{
		super(pCategory, pColor);
	}

	@Override
	public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
		AttributeInstance availableRibbons = pLivingEntity.getAttribute(SandmasteryAttributes.RIBBONS.getAttribute());
		availableRibbons.setBaseValue(Math.min(availableRibbons.getBaseValue() + 2, 24));
	}
}
