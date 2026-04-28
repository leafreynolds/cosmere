/*
 * File updated ~ 7 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.common.config;

import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public interface ICosmereConfig
{

	String getFileName();

	ModConfigSpec getConfigSpec();

	ModConfig.Type getConfigType();

	default void save()
	{
		getConfigSpec().save();
	}

	void clearCache();
}
