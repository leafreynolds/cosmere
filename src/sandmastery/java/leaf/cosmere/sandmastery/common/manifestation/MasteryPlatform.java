/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import leaf.cosmere.sandmastery.common.utils.SandmasteryConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MasteryPlatform extends SandmasteryManifestation
{
	public MasteryPlatform(Taldain.Mastery mastery)
	{
		super(mastery);
	}

	@Override
	public boolean tick(ISpiritweb data)
	{
		boolean enabledViaHotkey = MiscHelper.enabledViaHotkey(data, SandmasteryConstants.PLATFORM_HOTKEY_FLAG);

		if (notEnoughChargedSand(data))
		{
			return false;
		}
		if (getMode(data) > 0 && enabledViaHotkey)
		{
			return performEffectServer(data);
		}
		return false;
	}

	private void setBlockIfAir(Level level, BlockPos pos, BlockState state)
	{
		Block block = level.getBlockState(pos).getBlock();
		if (block != Blocks.AIR && block != SandmasteryBlocks.TEMPORARY_SAND_BLOCK.getBlock())
		{
			return;
		}
		level.setBlockAndUpdate(pos, state);
	}

	protected boolean performEffectServer(ISpiritweb data)
	{
		SpiritwebCapability playerSpiritweb = (SpiritwebCapability) data;
		SandmasterySpiritwebSubmodule submodule = (SandmasterySpiritwebSubmodule) playerSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);
		LivingEntity living = data.getLiving();
		Level level = living.level();
		BlockPos pos = living.blockPosition().below();
		BlockState state = SandmasteryBlocks.TEMPORARY_SAND_BLOCK.getBlock().defaultBlockState();
		int size = getMode(data) / 2;
		pos = pos.offset(-(size + 1), living.isCrouching() ? -1 : 0, -(size + 1));
		for (int x = -size; x <= size; x++)
		{
			pos = pos.offset(1, 0, 0);
			for (int z = -size; z <= size; z++)
			{
				pos = pos.offset(0, 0, 1);
				setBlockIfAir(level, pos, state);
			}
			pos = pos.offset(0, 0, -(size * 2) - 1);
		}

		BlockPos groundPos = MiscHelper.blockPosAtGround(data.getLiving());
		MiscHelper.spawnMasteredSandLine((ServerLevel) data.getLiving().level(), data.getLiving().getEyePosition(), new Vec3(groundPos.getX(), groundPos.getY(), groundPos.getZ()));

		submodule.adjustHydration(-getHydrationCost(data), true, data);
		return true;
	}
}
