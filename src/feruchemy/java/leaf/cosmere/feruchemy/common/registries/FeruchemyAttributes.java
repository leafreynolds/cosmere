/*
 * File updated ~ 29 - 2 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.registries;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.AttributeDeferredRegister;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import leaf.cosmere.feruchemy.common.Feruchemy;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeruchemyAttributes
{
	public static final AttributeDeferredRegister ATTRIBUTES = new AttributeDeferredRegister(Feruchemy.MODID);

	public static final Map<Metals.MetalType, AttributeRegistryObject<Attribute>> FERUCHEMY_ATTRIBUTES =
			Arrays.stream(Metals.MetalType.values())
					.filter(metalType -> metalType != Metals.MetalType.ATIUM && metalType.hasAssociatedManifestation())
					.collect(Collectors.toMap(
							Function.identity(),
							type ->
									ATTRIBUTES.register(
											type.getName(),
											Feruchemy.MODID,
											0,
											0,
											16
									)));


}
