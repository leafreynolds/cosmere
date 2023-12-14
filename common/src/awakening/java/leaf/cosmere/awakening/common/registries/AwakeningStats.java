/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.awakening.common.registries;

import leaf.cosmere.common.registration.impl.StatDeferredRegister;
import leaf.cosmere.common.registration.impl.StatRegistryObject;
import leaf.cosmere.awakening.common.Awakening;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class AwakeningStats
{
	public static final StatDeferredRegister STATS = new StatDeferredRegister(Awakening.MODID);


	public static void initStatEntries()
	{
		for (StatRegistryObject value : STATS.getAllItems())
		{
			Stats.CUSTOM.get(value.get(), StatFormatter.TIME);
		}
	}
}
