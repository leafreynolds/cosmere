/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class MasteryCushion extends SandmasteryManifestation
{
	public MasteryCushion(Taldain.Mastery mastery)
	{
		super(mastery);
	}

	@Override
	public boolean tick(ISpiritweb data)
	{
		if (getMode(data) > 0)
		{
			return performEffectServer(data);
		}
		return false;
	}

	@Override
	protected int getBaseCost()
	{
		return 1;
	}

	protected boolean performEffectServer(ISpiritweb data)
	{
		SandmasterySpiritwebSubmodule submodule = SandmasterySpiritwebSubmodule.get(data);

		LivingEntity living = data.getLiving();
		Vec3 movement = living.getDeltaMovement();
		if (movement.y > 0)
		{
			return false;
		}
		double distFromGround = MiscHelper.distanceFromGround(living);
		if (!(distFromGround > 1 && distFromGround < 10))
		{
			return false;
		}

		if (notEnoughChargedSand(data))
		{
			return false;
		}

		BlockPos groundPos = MiscHelper.blockPosAtGround(data.getLiving());
		MiscHelper.spawnMasteredSandLine((ServerLevel) data.getLiving().level(), data.getLiving().getEyePosition(), new Vec3(groundPos.getX(), groundPos.getY(), groundPos.getZ()));

		living.setDeltaMovement(movement.multiply(1, 0.05, 1));
		living.hurtMarked = true;
		living.resetFallDistance();
		submodule.adjustHydration(-getHydrationCost(data), true, data);
		useChargedSand(data);
		return true;
	}
}
