package leaf.cosmere.sandmastery.client.items;

import leaf.cosmere.sandmastery.common.items.AnimatedItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class AnimatedItemRenderer extends GeoItemRenderer<AnimatedItem>
{
	public AnimatedItemRenderer()
	{
		super(new AnimatedItemModel());
	}
}
