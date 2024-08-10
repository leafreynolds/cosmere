/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.math.VectorHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import leaf.cosmere.sandmastery.common.utils.SandmasteryConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class MasteryLaunch extends SandmasteryManifestation
{
	public MasteryLaunch(Taldain.Mastery mastery)
	{
		super(mastery);
	}

	@Override
	public int getRibbonsPerLevel(ISpiritweb data)
	{
		return 3;
	}

	@Override
	public boolean tick(ISpiritweb data)
	{
		SandmasterySpiritwebSubmodule submodule = SandmasterySpiritwebSubmodule.get(data);
		submodule.tickLaunchCooldown();
		if (!submodule.launchReady())
		{
			return false;
		}

		boolean enabledViaHotkey = MiscHelper.enabledViaHotkey(data, SandmasteryConstants.LAUNCH_HOTKEY_FLAG);
		if (getMode(data) > 0 && enabledViaHotkey)
		{
			submodule.setLaunchCooldown(10);
			return performEffectServer(data);
		}
		return false;
	}

	protected boolean performEffectServer(ISpiritweb data)
	{
		SpiritwebCapability playerSpiritweb = (SpiritwebCapability) data;
		ServerPlayer player = (ServerPlayer) data.getLiving();
		SandmasterySpiritwebSubmodule submodule = (SandmasterySpiritwebSubmodule) playerSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);
		if (notEnoughChargedSand(data))
		{
			return false;
		}

		int launchesAllowed = (this.getMode(data) / 3);
		if (MiscHelper.distanceFromGround(player) > 2)
		{
			if (submodule.getLaunches() >= launchesAllowed)
			{
				return false;
			}
			submodule.addLaunch();
			launch(player);
		}
		else
		{
			submodule.resetLaunches();
			launch(player);
		}

		BlockPos groundPos = MiscHelper.blockPosAtGround(data.getLiving());
		MiscHelper.spawnMasteredSandLine((ServerLevel) data.getLiving().level(), data.getLiving().getEyePosition(), new Vec3(groundPos.getX(), groundPos.getY(), groundPos.getZ()));

		submodule.adjustHydration(-getHydrationCost(data), true, data);
		useChargedSand(data);
		return true;
	}

	protected void launch(ServerPlayer player)
	{
		float scaleFactor = 2f; // todo: find a good value
		Vec3 direction = player.getForward();
		Vec3 add = player.getDeltaMovement().add(direction.multiply(scaleFactor, scaleFactor, scaleFactor));
		player.setDeltaMovement(VectorHelper.ClampMagnitude(add, 10));
		player.hurtMarked = true; // Allow the game to move the player
	}

//	protected boolean performEffectServer(ISpiritweb data)
//	{
//		SandmasterySpiritwebSubmodule submodule = SandmasterySpiritwebSubmodule.get(data);
//
//		int scaleFactor = getMode(data);
//		if (notEnoughChargedSand(data))
//		{
//			return false;
//		}
//
//		LivingEntity living = data.getLiving();
//		Vec3 direction = living.getForward();
//		Vec3 add = living.getDeltaMovement().add(direction.multiply(scaleFactor, scaleFactor, scaleFactor));
//		living.setDeltaMovement(VectorHelper.ClampMagnitude(add, 10));
//		living.hurtMarked = true; // Allow the game to move the player
//		living.resetFallDistance();
//
//		data.setMode(this, getMode(data) - 1);
//		data.syncToClients(null);
//
//		submodule.adjustHydration(-getHydrationCost(data), true, data);
//		useChargedSand(data);
//		return true;
//	}
}
