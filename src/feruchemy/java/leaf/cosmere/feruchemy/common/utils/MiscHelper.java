/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.utils;

import leaf.cosmere.api.Metals;
import net.minecraft.world.entity.LivingEntity;

public class MiscHelper
{

	public static void consumeNugget(LivingEntity livingEntity, Metals.MetalType metalType)
	{
		if (metalType == null || livingEntity.level.isClientSide)
		{
			return;
		}

	}


}
