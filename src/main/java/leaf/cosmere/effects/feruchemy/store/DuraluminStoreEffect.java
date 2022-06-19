/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import leaf.cosmere.registry.EffectsRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event;

//connection aka ability for people to notice you
public class DuraluminStoreEffect extends FeruchemyEffectBase
{
	public DuraluminStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
		MinecraftForge.EVENT_BUS.addListener(this::onLivingVisibilityEvent);
	}

	public void onLivingVisibilityEvent(LivingEvent.LivingVisibilityEvent event)
	{
		MobEffectInstance effectInstance = event.getEntityLiving().getEffect(this);
		if (effectInstance != null && effectInstance.getDuration() > 0)
		{
			//at max strength and wearing no armor, you could stand a block or two away from a creeper and it wont see you.
			//walk right into it though, and it will blow up.
			event.modifyVisibility(1f / (effectInstance.getAmplifier() + 2));
		}
	}

}
