package leaf.cosmere.surgebinding.common.capabilities.ideals.order;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.mixinAccess.RaidMixinAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class WindrunnerXPManager
{
	public int wrXP = 0;

	public static boolean validateIdeal(SpiritwebCapability spiritweb, int ideal)
	{
		//todo - Implement XP thresholds

		//for now, we can do this
		return switch (ideal)
		{
			case 1 -> true;
			case 2 -> ProtectThoseWhoCannotProtectThemselves(spiritweb);
			default -> false;
		};
	}

	private static boolean ProtectThoseWhoCannotProtectThemselves(SpiritwebCapability spiritweb)
	{
		final LivingEntity living = spiritweb.getLiving();

		RaidMixinAccess raid = (RaidMixinAccess) ((ServerLevel) living.level()).getRaidAt(living.blockPosition());

		if (raid != null && raid._cosmere$isHero((Player) living))
		{
			return true;
		}

		MobEffectInstance isHero = living.getEffect(MobEffects.HERO_OF_THE_VILLAGE);
		return isHero != null;
	}

	public static void xpUp()
	{

	}

	public static void xpDown()
	{

	}
}
