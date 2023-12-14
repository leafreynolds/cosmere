/*
 * File updated ~ 12 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.common.MinecraftForge;


public class SteelTapEffect extends FeruchemyEffectBase
{
	public SteelTapEffect(Metals.MetalType type)
	{
		super(type);

		this.addAttributeModifier(
				Attributes.ATTACK_SPEED,
				0.1F,
				AttributeModifier.Operation.MULTIPLY_TOTAL);

		this.addAttributeModifier(
				Attributes.MOVEMENT_SPEED,
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
