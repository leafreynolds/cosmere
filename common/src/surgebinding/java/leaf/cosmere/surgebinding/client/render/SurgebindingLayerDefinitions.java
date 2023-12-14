/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.client.render;

import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class SurgebindingLayerDefinitions
{
	public static final ModelLayerLocation SHARDPLATE = new ModelLayerLocation(Surgebinding.rl("shardplate"), "shardplate");
	public static final ModelLayerLocation CHULL = new ModelLayerLocation(Surgebinding.rl("chull"), "chull");

	// TODO This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation CRYPTIC = new ModelLayerLocation(Surgebinding.rl("cryptic"), "cryptic");
}
