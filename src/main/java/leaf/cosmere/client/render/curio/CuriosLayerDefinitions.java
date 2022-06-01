/*
 * File created ~ 30 - 5 - 2022 ~ Leaf
 */

package leaf.cosmere.client.render.curio;

import leaf.cosmere.Cosmere;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class CuriosLayerDefinitions
{
	public static final ModelLayerLocation SPIKE = new ModelLayerLocation(ResourceLocationHelper.prefix("spike"), "spike");
	public static final ModelLayerLocation BRACELET = new ModelLayerLocation(ResourceLocationHelper.prefix("bracelet"), "bracelet");
	public static final ModelLayerLocation NECKLACE = new ModelLayerLocation(ResourceLocationHelper.prefix("necklace"), "necklace");
}
