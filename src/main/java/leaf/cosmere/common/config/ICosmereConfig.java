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

	/**
	 * Whether the config should be synced to client?
	 * If it's added to the mod container, I think that lets forge handle overriding values.
	 * If false, then it creates the file, so it can be tracked, but not overridden
	 */
	default boolean addToContainer()
	{
		return true;
	}

	void clearCache();
}
