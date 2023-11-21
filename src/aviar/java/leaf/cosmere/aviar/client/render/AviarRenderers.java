/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.client.render;

import leaf.cosmere.aviar.common.registries.AviarEntityTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ParrotRenderer;

public class AviarRenderers
{

	public static void register()
	{
		EntityRenderers.register(AviarEntityTypes.AVIAR_ENTITY.get(), ParrotRenderer::new);
	}


}
