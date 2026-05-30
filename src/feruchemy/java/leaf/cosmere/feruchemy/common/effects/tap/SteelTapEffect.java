/*
 * File updated ~ 12 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.common.NeoForge;


public class SteelTapEffect extends FeruchemyEffectBase
{
	public SteelTapEffect(Metals.MetalType type)
	{
		super(type);

		this.addAttributeModifier(
				Attributes.ATTACK_SPEED,
				0.1F,
				AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

		this.addAttributeModifier(
				Attributes.MOVEMENT_SPEED,
				0.2F,
				AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

		NeoForge.EVENT_BUS.addListener(this::onFOVUpdate);

	}

	public void onFOVUpdate(ViewportEvent.ComputeFov event)
	{
		//todo remember to make this better? clamp isn't necessarily the best way to stop it going over the top
		event.setFOV(Mth.clamp(event.getFOV(), 0.8, 1.2));
	}
}
