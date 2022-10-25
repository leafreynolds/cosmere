/*
 * File updated ~ 24 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.registries;

import leaf.cosmere.common.registration.impl.AttributeDeferredRegister;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class HemalurgyAttributes
{

	public static final AttributeDeferredRegister ATTRIBUTES = new AttributeDeferredRegister(Hemalurgy.MODID);

	public static final AttributeRegistryObject<Attribute> SPIRITWEB_INTEGRITY =
			ATTRIBUTES.register(
					"spiritweb_integrity",
					Hemalurgy.MODID,
					3,
					-16,
					16);


}
