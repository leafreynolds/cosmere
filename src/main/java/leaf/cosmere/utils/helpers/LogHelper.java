/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class LogHelper
{
	public static final Logger LOGGER = LogUtils.getLogger();

	public static void info(String object)
	{
		LOGGER.info(object);
	}
}