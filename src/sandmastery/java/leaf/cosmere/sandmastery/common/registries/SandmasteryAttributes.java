/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
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

	// ResourceLocation IDs for permanent attribute modifiers (replaces UUID-based modifiers from 1.20.1)
	public static final ResourceLocation OVERMASTERY_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(Sandmastery.MODID, "overmastery");
	public static final ResourceLocation OVERMASTERY_SECONDARY_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(Sandmastery.MODID, "overmastery_secondary");
}
