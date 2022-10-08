/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
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

	public void onFOVUpdate(ComputeFovModifierEvent event)
	{
		//todo remember to make this better? clamp isn't necessarily the best way to stop it going over the top
		event.setNewFovModifier(Mth.clamp(event.getNewFovModifier(), 0.8f, 1.2f));
	}
}
