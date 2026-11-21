/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.common.registration.impl.AttributeDeferredRegister;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class SandmasteryAttributes
{
	public static final AttributeDeferredRegister ATTRIBUTES = new AttributeDeferredRegister(Sandmastery.MODID);

	public static final AttributeRegistryObject<Attribute> RIBBONS =
			ATTRIBUTES.register(
					"ribbons",
					Sandmastery.MODID,
					0,
					0,
					24
			);
	//RLs for permanent modifiers to the player's ribbon attribute
	public static final ResourceLocation OVERMASTERY_ID = Sandmastery.rl("overmastery");
	public static final ResourceLocation OVERMASTERY_SECONDARY_ID = Sandmastery.rl("overmastery_secondary");
}
