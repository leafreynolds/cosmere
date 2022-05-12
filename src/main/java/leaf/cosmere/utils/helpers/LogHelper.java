/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class LogHelper
{
	public static final Logger LOGGER = LogUtils.getLogger();


	public static void debug(String object)
	{
		LOGGER.debug(object);
	}

	public static void error(String object)
	{
		LOGGER.error(object);
	}

	public static void info(String object)
	{
		LOGGER.info(object);
	}

	public static void trace(String object)
	{
		LOGGER.trace(object);
	}

	public static void warn(String object)
	{
		LOGGER.warn(object);
	}
}