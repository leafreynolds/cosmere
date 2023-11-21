/*
 * File updated ~ 21 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.registries;

import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.AttributeDeferredRegister;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class AviarAttributes
{

	public static final AttributeDeferredRegister ATTRIBUTES = new AttributeDeferredRegister(Aviar.MODID);

	public static final AttributeRegistryObject<Attribute> HOSTILE_LIFE_SENSE =
			ATTRIBUTES.register(
					"hostile_life_sense",
					Cosmere.MODID,
					0,
					0,
					10);
}
