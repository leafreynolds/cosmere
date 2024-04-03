/*
 * File updated ~ 22 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common.registries;

import leaf.cosmere.common.registration.impl.StatDeferredRegister;
import leaf.cosmere.common.registration.impl.StatRegistryObject;
import leaf.cosmere.tools.common.CosmereTools;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class ToolsStats
{
	public static final StatDeferredRegister STATS = new StatDeferredRegister(CosmereTools.MODID);


	public static void initStatEntries()
	{
		for (StatRegistryObject value : STATS.getAllItems())
		{
			Stats.CUSTOM.get(value.get(), StatFormatter.TIME);
		}
	}
}
