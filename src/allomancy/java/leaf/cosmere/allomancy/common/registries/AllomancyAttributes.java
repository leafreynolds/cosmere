/*
 * File updated ~ 24 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.AttributeDeferredRegister;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AllomancyAttributes
{
	public static final AttributeDeferredRegister ATTRIBUTES = new AttributeDeferredRegister(Allomancy.MODID);

	public static final Map<Metals.MetalType, AttributeRegistryObject<Attribute>> ALLOMANCY_ATTRIBUTES =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasAssociatedManifestation)
					.collect(Collectors.toMap(
							Function.identity(),
							type ->
									ATTRIBUTES.register(
											type.getName(),
											Allomancy.MODID,
											0,
											0,
											20
									)));


}
