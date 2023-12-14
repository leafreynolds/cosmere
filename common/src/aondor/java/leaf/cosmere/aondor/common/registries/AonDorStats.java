/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aondor.common.registries;

import leaf.cosmere.common.registration.impl.StatDeferredRegister;
import leaf.cosmere.common.registration.impl.StatRegistryObject;
import leaf.cosmere.aondor.common.AonDor;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class AonDorStats
{
	public static final StatDeferredRegister STATS = new StatDeferredRegister(AonDor.MODID);


	public static void initStatEntries()
	{
		for (StatRegistryObject value : STATS.getAllItems())
		{
			Stats.CUSTOM.get(value.get(), StatFormatter.TIME);
		}
	}
}
