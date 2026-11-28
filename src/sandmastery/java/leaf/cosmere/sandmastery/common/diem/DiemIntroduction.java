package leaf.cosmere.sandmastery.common.diem;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.common.blocks.entities.DiemStoneBE;
import leaf.cosmere.sandmastery.common.blocks.entities.DiemStoneBE.Look;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class DiemIntroduction
{
	private static final int GLOW_DELAY_TICKS = 20;
	private static final int GLOW_TICKS = 30;
	private static final int SPENT_SAND_TICKS = 60;

	private static final Map<UUID, Intro> INTROS = new HashMap<>();

	private static final class Intro
	{
		final BlockPos stone;
		// game time the player picked up the sand, or -1 while it's still waiting in the stone
		long reachTick = -1;
		boolean glowing;

		Intro(BlockPos stone)
		{
			this.stone = stone;
		}
	}

	private DiemIntroduction()
	{
	}

	public static void onStoneUsed(ServerPlayer player, BlockPos stone)
	{
		ServerLevel level = player.serverLevel();
		Intro intro = INTROS.get(player.getUUID());

		if (intro == null || !intro.stone.equals(stone))
		{
			stop(player);
			INTROS.put(player.getUUID(), new Intro(stone));
			DiemStoneBE.setShared(level, stone, Look.WHITE, 0);
			level.sendParticles(dust(SandmasteryBlocks.TALDAIN_WHITE_SAND.getBlock().defaultBlockState()),
					stone.getX() + 0.5, stone.getY() + 1.5, stone.getZ() + 0.5, 30, 0.15, 0.3, 0.15, 0);
			level.playSound(null, stone, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
			mastrellSays(player, "test");
			mastrellSays(player, "reach");
		}
		else if (intro.reachTick == -1)
		{
			intro.reachTick = level.getGameTime();
		}
	}

	public static void tick(ServerPlayer player)
	{
		Intro intro = INTROS.get(player.getUUID());
		if (intro == null || intro.reachTick == -1)
		{
			return;
		}

		ServerLevel level = player.serverLevel();
		long sinceReach = level.getGameTime() - intro.reachTick;
		if (sinceReach >= GLOW_DELAY_TICKS + GLOW_TICKS)
		{
			INTROS.remove(player.getUUID());
			DiemStoneBE.setShared(level, intro.stone, Look.BLACK, SPENT_SAND_TICKS);
			SpiritwebCapability.get(player).ifPresent(spiritweb ->
			{
				int ribbons = DiemProgression.detect(player, spiritweb);
				mastrellSays(player, "welcome", DiemRank.ACOLENT.getDisplayName(), ribbons);
				mastrellSays(player, "trials");
			});
		}
		else if (sinceReach >= GLOW_DELAY_TICKS && !intro.glowing)
		{
			intro.glowing = true;
			DiemStoneBE.setShared(level, intro.stone, Look.GLOWING, 0);
			level.sendParticles(dust(Blocks.GOLD_BLOCK.defaultBlockState()),
					intro.stone.getX() + 0.5, intro.stone.getY() + 1.2, intro.stone.getZ() + 0.5, 40, 0.4, 0.4, 0.4, 0.05);
			level.playSound(null, intro.stone, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.2F);
		}
	}

	// abandons the introduction
	public static void stop(ServerPlayer player)
	{
		Intro intro = INTROS.remove(player.getUUID());
		if (intro != null)
		{
			DiemStoneBE.setShared(player.serverLevel(), intro.stone, null, 0);
		}
	}

	private static BlockParticleOption dust(BlockState colour)
	{
		return new BlockParticleOption(ParticleTypes.FALLING_DUST, colour);
	}

	private static void mastrellSays(ServerPlayer player, String line, Object... args)
	{
		player.sendSystemMessage(Component.translatable("diem.sandmastery.mastrell.says",
				Component.translatable("diem.sandmastery.mastrell.name"),
				Component.translatable("diem.sandmastery.intro." + line, args)));
	}
}
