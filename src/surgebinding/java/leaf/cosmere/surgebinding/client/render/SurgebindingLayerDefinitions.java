
/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.client.render;

import leaf.cosmere.surgebinding.client.render.model.*;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class SurgebindingLayerDefinitions
{
	public static final ModelLayerLocation SHARDPLATE = new ModelLayerLocation(Surgebinding.rl("shardplate"), "shardplate");
	public static final ModelLayerLocation CHULL = new ModelLayerLocation(Surgebinding.rl("chull"), "chull");

	// TODO This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation CRYPTIC = new ModelLayerLocation(Surgebinding.rl("cryptic"), "cryptic");
	public static final ModelLayerLocation HONORSPREN = new ModelLayerLocation(Surgebinding.rl("honorspren"), "honorspren");
	public static final ModelLayerLocation SHARDBLADE = new ModelLayerLocation(Surgebinding.rl("shardblade"), "shardblade");

	public static void register(EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
		//shardplate
		evt.registerLayerDefinition(SurgebindingLayerDefinitions.SHARDPLATE, ShardplateModel::createBodyLayer);
		evt.registerLayerDefinition(SurgebindingLayerDefinitions.CHULL, ChullModel::createBodyLayer);
		evt.registerLayerDefinition(SurgebindingLayerDefinitions.CRYPTIC, CrypticModel::createBodyLayer);
		evt.registerLayerDefinition(SurgebindingLayerDefinitions.HONORSPREN, HonorsprenModel::createBodyLayer);

		evt.registerLayerDefinition(SurgebindingLayerDefinitions.SHARDBLADE, ShardbladeModel::createLayerDefinition);
	}
}
