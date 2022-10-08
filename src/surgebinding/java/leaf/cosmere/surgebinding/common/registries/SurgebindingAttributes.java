/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.registration.impl.AttributeDeferredRegister;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SurgebindingAttributes
{
	public static final AttributeDeferredRegister ATTRIBUTES = new AttributeDeferredRegister(Surgebinding.MODID);

	public static final Map<Roshar.Surges, AttributeRegistryObject<Attribute>> SURGEBINDING_ATTRIBUTES =
			Arrays.stream(Roshar.Surges.values())
					.collect(Collectors.toMap(
							Function.identity(),
							surge ->
									ATTRIBUTES.register(
											surge.getName(),
											Surgebinding.MODID,
											0,
											0,
											10
									)));


}
