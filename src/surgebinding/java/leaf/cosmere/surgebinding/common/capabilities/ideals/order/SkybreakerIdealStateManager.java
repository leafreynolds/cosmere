/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities.ideals.order;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.mixinAccess.RaidMixinAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SkybreakerIdealStateManager
{
	public static boolean validateIdeal(SpiritwebCapability spiritweb, int ideal)
	{
		return switch (ideal)
		{
			case 1 -> true;
			case 2 -> SeekJusticeLetItGuideMe(spiritweb);
			case 3 -> IdealOfDedication(spiritweb);
			case 4 -> IdealOfCrusade(spiritweb);
			case 5 -> IAmTHELAW(spiritweb);
			default -> false;
		};
	}

	private static boolean SeekJusticeLetItGuideMe(SpiritwebCapability spiritweb)
	{

		return false;
	}

	private static boolean IdealOfDedication(SpiritwebCapability spiritweb)
	{
		return false;
	}

	private static boolean IdealOfCrusade(SpiritwebCapability spiritweb)
	{
		return false;
	}

	private static boolean IAmTHELAW(SpiritwebCapability spiritweb)
	{
		return false;
	}
}
