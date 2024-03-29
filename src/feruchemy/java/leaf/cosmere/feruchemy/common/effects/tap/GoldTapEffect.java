/*
 * File updated ~ 21 - 02 - 2024 ~ Gerbagel
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.charge.MetalmindChargeHelper;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyGold;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;


public class GoldTapEffect extends FeruchemyEffectBase
{

	public GoldTapEffect(Metals.MetalType type)
	{
		super(type);
		addAttributeModifier(
				AttributesRegistry.HEALING_STRENGTH.getAttribute(),
				1.0D,
				AttributeModifier.Operation.ADDITION);
	}

	@Override
	public void applyEffectTick(ISpiritweb data, double strength)
	{
		final LivingEntity living = data.getLiving();
		final int i = (int) (strength);
		boolean isHealTick;

		// at min tap (1) should be active every 10 seconds     // 10 seconds is 200 ticks
		int healActiveTick = FeruchemyGold.getHealActiveTick(i);
		healActiveTick = Math.max(1, healActiveTick);   // can't tick more often than every tick
		isHealTick = living.tickCount % healActiveTick == 0;

		if (isHealTick)
		{
			final int amountToHeal = 1;

			living.heal(amountToHeal);
		}

		if (living.tickCount % (getActiveTick() + getTickOffset()) == 0)
		{
			//todo move to config
			final int timeToReduceByInTicks = i * 40;
			final int ticksNeededLeftToReduce = 5;

			//remove harmful effects over time
			for (MobEffectInstance activeEffect : living.getActiveEffects())
			{
				if (!activeEffect.getEffect().isBeneficial() && activeEffect.getDuration() > ticksNeededLeftToReduce)
				{
					//never reduce down below 5 ticks
					final double clamped = Math.max(ticksNeededLeftToReduce, activeEffect.getDuration() - timeToReduceByInTicks);
					final int duration = Mth.floor(clamped);
					MobEffectInstance newInstance = new MobEffectInstance(
							activeEffect.getEffect(),
							duration,
							activeEffect.getAmplifier(),
							activeEffect.isAmbient(),
							activeEffect.isVisible(),
							activeEffect.showIcon());
					living.removeEffectNoUpdate(activeEffect.getEffect());
					living.addEffect(newInstance);
				}
			}
		}
	}

	@Override
	protected boolean isActiveTick(ISpiritweb data)
	{
		return true;
	}



	public static void onLivingHurtEvent(LivingHurtEvent event)
	{
		if (event.isCanceled())
		{
			return;
		}

		if (event.getAmount() > event.getEntity().getHealth())
		{
			int strength = (int) EntityHelper.getAttributeValue(event.getEntity(), AttributesRegistry.HEALING_STRENGTH.getAttribute(), 0);

			//take less damage when tapping
			if (strength > 6 && event.getEntity() instanceof Player player)
			{
				// the cost of not dying should be all the extra damage * 50
				int amount = (int) (event.getAmount() - player.getHealth()) * 50;
				final ItemStack metalmind = MetalmindChargeHelper.adjustMetalmindChargeExact(player, Metals.MetalType.GOLD, -amount, true, true);

				if (!metalmind.isEmpty())
				{
					event.setAmount((float) Math.floor(player.getHealth() - 1));
				}
			}
		}
	}
}
