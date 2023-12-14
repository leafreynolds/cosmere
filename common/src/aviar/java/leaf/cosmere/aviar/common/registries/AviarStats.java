/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.registries;

import leaf.cosmere.common.registration.impl.StatDeferredRegister;
import leaf.cosmere.common.registration.impl.StatRegistryObject;
import leaf.cosmere.aviar.common.Aviar;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class AviarStats
{
	public static final StatDeferredRegister STATS = new StatDeferredRegister(Aviar.MODID);


	public static void initStatEntries()
	{
		for (StatRegistryObject value : STATS.getAllItems())
		{
			Stats.CUSTOM.get(value.get(), StatFormatter.TIME);
		}
	}
}
