/*
 * File updated ~ 18 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.common.registration.impl.AttributeDeferredRegister;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.UUID;

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
	//UUIDs for permanent modifier to player's ribbon attribute
	public static final UUID OVERMASTERY_UUID = UUID.nameUUIDFromBytes("Overmastery".getBytes());
	public static final UUID OVERMASTERY_SECONDARY_UUID = UUID.nameUUIDFromBytes("Overmastery".getBytes());
}
