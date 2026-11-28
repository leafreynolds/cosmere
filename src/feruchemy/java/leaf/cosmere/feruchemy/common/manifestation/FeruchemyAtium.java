/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.feruchemy.common.registries.FeruchemyAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class FeruchemyAtium extends FeruchemyManifestation
{
	public FeruchemyAtium(Metals.MetalType metalType)
	{
		super(metalType);
	}


	@Override
	public Holder<Attribute> getAttribute()
	{
		return FeruchemyAttributes.FERUCHEMY_ATTRIBUTES.get(Metals.MetalType.ELECTRUM).getHolder();
	}

	@Override
	public void onModeChange(ISpiritweb data, int lastMode)
	{
		super.onModeChange(data, lastMode);
		//Attributes.SCALE drives the bounding box and eye height, but vanilla does not refresh
		//dimensions when its modifiers change, so we do it manually
		data.getLiving().refreshDimensions();
	}
}
