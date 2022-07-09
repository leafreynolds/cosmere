/*
 * File created ~ 9 - 7 - 2022 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.LogHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatsRegistry
{
	public static final Map<Metals.MetalType, ResourceLocation> ALLOMANCY_BURN_TIME =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasAssociatedManifestation)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType -> makeCustomStat("time_since_started_burning_" + metalType.getName(), StatFormatter.TIME)
					));

	private static ResourceLocation makeCustomStat(String pKey, StatFormatter pFormatter)
	{
		ResourceLocation resourcelocation = new ResourceLocation(pKey);
		Registry.register(Registry.CUSTOM_STAT, pKey, resourcelocation);
		Stats.CUSTOM.get(resourcelocation, pFormatter);
		return resourcelocation;
	}

	public static void register()
	{
		//static call will initialize static fields
		LogHelper.info("Registering custom stats");
	}
}
