/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.FOVModifierEvent;
import net.minecraftforge.common.MinecraftForge;


public class SteelTapEffect extends FeruchemyEffectBase
{
	public SteelTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);

		this.addAttributeModifier(
				Attributes.ATTACK_SPEED,
				"0191c754-d7a2-4c78-9e14-896ecc7ed0e2",
				0.1F,
				AttributeModifier.Operation.MULTIPLY_TOTAL);

		this.addAttributeModifier(
				Attributes.MOVEMENT_SPEED,
				"ede32ebb-1e66-4d26-b414-c14467885e7a",
				0.2F,
				AttributeModifier.Operation.MULTIPLY_TOTAL);

		MinecraftForge.EVENT_BUS.addListener(this::onFOVUpdate);

	}

	public void onFOVUpdate(FOVModifierEvent event)
	{
		//todo remember to make this better? clamp isn't necessarily the best way to stop it going over the top
		event.setNewFov(Mth.clamp(event.getNewFov(), 0.8f, 1.2f));
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return amplifier > 2;
	}

	final Vec3 vec = new Vec3(1.02d, 0d, 1.02d);

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
	{
		//todo replace this with something better
		//code for checking block under player
		//https://stackoverflow.com/a/62026168
		if (entityLivingBaseIn.level.getBlockState(entityLivingBaseIn.blockPosition().below()).getMaterial() == Material.WATER)
		{

			Vec3 motion = entityLivingBaseIn.getDeltaMovement().multiply(vec);
			entityLivingBaseIn.setDeltaMovement(motion);
		}
	}
}
