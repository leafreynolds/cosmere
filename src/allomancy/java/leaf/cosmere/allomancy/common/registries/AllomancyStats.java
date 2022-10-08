/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.StatDeferredRegister;
import leaf.cosmere.common.registration.impl.StatRegistryObject;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AllomancyStats
{
	public static final StatDeferredRegister STATS = new StatDeferredRegister(Allomancy.MODID);

	public static final Map<Metals.MetalType, StatRegistryObject> ALLOMANCY_BURN_TIME =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasAssociatedManifestation)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType -> STATS.register("time_since_started_burning_" + metalType.getName())
					));


	public static void initStatEntries()
	{
		for (StatRegistryObject value : ALLOMANCY_BURN_TIME.values())
		{
			Stats.CUSTOM.get(value.get(), StatFormatter.TIME);
		}
	}
}
