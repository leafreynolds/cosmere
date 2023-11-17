/*
 * File updated ~ 26 - 10 - 2023 ~ Leaf
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


	//copper clouds, aviaar, light weavers, aluminum cages?
	public static final AttributeRegistryObject<Attribute> COGNITIVE_CONCEALMENT =
			ATTRIBUTES.register(
					"cognitive_concealment",
					Cosmere.MODID,
					0,
					0,
					99);

	public static final AttributeRegistryObject<Attribute> CONNECTION =
			ATTRIBUTES.register(
					"connection",
					Cosmere.MODID,
					0,
					-99,
					99);

	public static final AttributeRegistryObject<Attribute> COSMERE_FORTUNE =
			ATTRIBUTES.register(
					"cosmere_fortune",
					Cosmere.MODID,
					0,
					-99,
					99);

	public static final AttributeRegistryObject<Attribute> IDENTITY =
			ATTRIBUTES.register(
					"identity",
					Cosmere.MODID,
					1,
					-99,
					99);

	public static final AttributeRegistryObject<Attribute> DETERMINATION =
			ATTRIBUTES.register(
					"determination",
					Cosmere.MODID,
					1,
					-99,
					99);

	public static final AttributeRegistryObject<Attribute> WARMTH =
			ATTRIBUTES.register(
					"warmth",
					Cosmere.MODID,
					0,
					-99,
					99);

	public static final AttributeRegistryObject<Attribute> HEALING_STRENGTH =
			ATTRIBUTES.register(
					"healing_strength",
					Cosmere.MODID,
					0,
					-99,
					99);

}
