/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import leaf.cosmere.sandmastery.common.utils.SandmasteryConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class MasteryElevate extends SandmasteryManifestation
{
	public MasteryElevate(Taldain.Mastery mastery)
	{
		super(mastery);
	}

	@Override
	public boolean tick(ISpiritweb data)
	{
		boolean enabledViaHotkey = MiscHelper.enabledViaHotkey(data, SandmasteryConstants.ELEVATE_HOTKEY_FLAG);
		super.tick(data);
		if (getMode(data) > 0 && enabledViaHotkey)
		{
			return performEffectServer(data);
		}
		return false;
	}

	@Override
	public int getBaseCost()
	{
		return 1;
	}

	@Override
	public int getRibbonsPerLevel(ISpiritweb data)
	{
		return getMode(data) < 3 ? 3 : 1;
	}

	protected boolean performEffectServer(ISpiritweb data)
	{
		SandmasterySpiritwebSubmodule submodule = SandmasterySpiritwebSubmodule.get(data);
		if (getMode(data) < 3)
		{
			return false;
		}
		if (notEnoughChargedSand(data))
		{
			return false;
		}

		LivingEntity living = data.getLiving();
		int distFromGround = MiscHelper.distanceFromGround(living);
		int maxLift = getMode(data) * 4;
		if (distFromGround > maxLift)
		{
			return false;
		}

		double speed = (maxLift - distFromGround) > 3 ? scaleSpeedToMode(getMode(data)) : 0.15;

		Vec3 direction = new Vec3(0, speed, 0);

		living.setDeltaMovement(direction);
		living.hurtMarked = true; // Allow the game to move the player
		living.resetFallDistance();

		BlockPos groundPos = MiscHelper.blockPosAtGround(data.getLiving());
		MiscHelper.spawnMasteredSandLine((ServerLevel) data.getLiving().level(), data.getLiving().getEyePosition(), new Vec3(groundPos.getX(), groundPos.getY(), groundPos.getZ()));

		submodule.adjustHydration(-getHydrationCost(data), true, data);
		useChargedSand(data);

		return true;
	}

	double scaleSpeedToMode(double mode)
	{
		return (mode * 0.025) + 0.15;
	}
}
