/*
 * File updated ~ 23 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

//connection aka ability for people to notice you
public class DuraluminStoreEffect extends FeruchemyEffectBase
{
	public DuraluminStoreEffect(Metals.MetalType type)
	{
		super(type);

		addAttributeModifier(
				AttributesRegistry.CONNECTION.getHolder(),
				-1.0D,
				AttributeModifier.Operation.ADD_VALUE);

		NeoForge.EVENT_BUS.addListener(this::onLivingChangeTargetEvent);
	}

	public void onLivingChangeTargetEvent(LivingChangeTargetEvent event)
	{
		if (event.isCanceled() || event.getNewAboutToBeSetTarget() == null)
		{
			return;
		}

		int attributeValue = (int) EntityHelper.getAttributeValue(event.getNewAboutToBeSetTarget(), AttributesRegistry.CONNECTION.getHolder());
		if (attributeValue < 0)
		{
			int abs = Math.abs(attributeValue);

			//at max strength and wearing no armor, you could stand a block or two away from a creeper and it won't see you.
			//walk right into it though, and it will blow up.
			if (abs >= 2)
			{
				event.setCanceled(true);
			}
		}
	}

}
