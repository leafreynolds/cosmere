/*
 * File updated ~ 26 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//https://coppermind.net/wiki/Brass#Feruchemical_Use

@Mod.EventBusSubscriber(modid = Feruchemy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BrassTapEffect extends FeruchemyEffectBase
{
	public BrassTapEffect(Metals.MetalType type)
	{
		super(type);
		addAttributeModifier(
				AttributesRegistry.WARMTH.get(),
				1,//warmer when tapping
				AttributeModifier.Operation.ADDITION);

		//reduce frost damage? theres no cold damage in base minecraft is there?

		//add fire damage?
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, double strength)
	{
		if (!isActiveTick(entityLivingBaseIn))
		{
			return;
		}

		//todo move to config
		if (!entityLivingBaseIn.level.isClientSide && strength >= 5 && !entityLivingBaseIn.isInWater())
		{
			//set user on fire
			entityLivingBaseIn.setSecondsOnFire(3);
		}

	}

	@SubscribeEvent
	public static void onLivingDamageEvent(LivingDamageEvent event)
	{
		if (event.getSource().getEntity() instanceof LivingEntity livingEntity)
		{
			final int total = (int) EntityHelper.getAttributeValue(livingEntity, AttributesRegistry.WARMTH.getAttribute());
			if (total > 4)//todo move to config
			{
				//set entity being hit on fire
				event.getEntity().setSecondsOnFire(total);
			}
		}
	}
}
