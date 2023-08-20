package leaf.cosmere.sandmastery.client.items;

import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.items.AnimatedItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AnimatedItemModel extends AnimatedGeoModel<AnimatedItem>
{
	@Override
	public ResourceLocation getModelResource(AnimatedItem object) {
		return new ResourceLocation(Sandmastery.MODID, "geo/tmp_item.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(AnimatedItem object) {
		return new ResourceLocation(Sandmastery.MODID, "textures/item/tmp_item.png");
	}

	@Override
	public ResourceLocation getAnimationResource(AnimatedItem animatable) {
		return new ResourceLocation(Sandmastery.MODID, "animations/tmp_item.animation.json");
	}
}