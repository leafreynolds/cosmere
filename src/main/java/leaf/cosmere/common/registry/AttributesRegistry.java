/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.AttributeDeferredRegister;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class AttributesRegistry
{
	public static final AttributeDeferredRegister ATTRIBUTES = new AttributeDeferredRegister(Cosmere.MODID);

	public static final AttributeRegistryObject<Attribute> NIGHT_VISION_ATTRIBUTE = ATTRIBUTES.register(Metals.MetalType.TIN.getName(), Cosmere.MODID, 0, 0, 1);
	public static final AttributeRegistryObject<Attribute> XP_RATE_ATTRIBUTE = ATTRIBUTES.register(Metals.MetalType.COPPER.getName(), Cosmere.MODID, 1, 0, 20);

	public static final AttributeRegistryObject<Attribute> SIZE_ATTRIBUTE = ATTRIBUTES.register(Metals.MetalType.ATIUM.getName(), Cosmere.MODID, 1, 0.1f, 20);

}
