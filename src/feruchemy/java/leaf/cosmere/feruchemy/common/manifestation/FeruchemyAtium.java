/*
 * File updated ~ 31 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.feruchemy.common.registries.FeruchemyAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class FeruchemyAtium extends FeruchemyManifestation
{
	public FeruchemyAtium(Metals.MetalType metalType)
	{
		super(metalType);
	}


	@Override
	public Attribute getAttribute()
	{
		return FeruchemyAttributes.FERUCHEMY_ATTRIBUTES.get(Metals.MetalType.ELECTRUM).getAttribute();
	}

	@Override
	public void onModeChange(ISpiritweb data, int lastMode)
	{
		super.onModeChange(data, lastMode);
		// Vanilla Attributes.SCALE drives bounding box, eye height, step height, attack reach,
		// and model render scale, but doesn't auto-refresh dimensions when its modifiers change.
		data.getLiving().refreshDimensions();
	}
}
