/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.soulforgery.common.registries;

import leaf.cosmere.common.registration.impl.StatDeferredRegister;
import leaf.cosmere.common.registration.impl.StatRegistryObject;
import leaf.cosmere.soulforgery.common.Soulforgery;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class SoulforgeryStats
{
	public static final StatDeferredRegister STATS = new StatDeferredRegister(Soulforgery.MODID);


	public static void initStatEntries()
	{
		for (StatRegistryObject value : STATS.getAllItems())
		{
			Stats.CUSTOM.get(value.get(), StatFormatter.TIME);
		}
	}
}
