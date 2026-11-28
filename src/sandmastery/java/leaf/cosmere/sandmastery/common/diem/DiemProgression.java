package leaf.cosmere.sandmastery.common.diem;

import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

public final class DiemProgression
{
	private DiemProgression()
	{
	}

	// talent detection never denies anyone: untested players become Acolents with 1 or 2 ribbons. Returns the ribbons given.
	public static int detect(ServerPlayer player, ISpiritweb spiritweb)
	{
		int ribbons = player.getRandom().nextDouble() < SandmasteryConfigs.SERVER.DETECTION_TWO_RIBBON_CHANCE.get() ? 2 : 1;
		// every Sandmastery power shares the ribbons attribute, so any of them sets it
		spiritweb.giveManifestation(SandmasteryManifestations.SANDMASTERY_POWERS.get(Taldain.Mastery.PROJECTILE).get(), ribbons);
		SandmasterySpiritwebSubmodule.get(spiritweb).setDiemRank(DiemRank.ACOLENT);
		spiritweb.syncToClients(null);
		return ribbons;
	}

	// returns the new rank, or null when there is nothing to promote to (untested, or already the top rank)
	@Nullable
	public static DiemRank promote(ServerPlayer player, ISpiritweb spiritweb)
	{
		SandmasterySpiritwebSubmodule submodule = SandmasterySpiritwebSubmodule.get(spiritweb);
		DiemRank rank = submodule.getDiemRank();
		DiemRank next = rank == null ? null : rank.next();
		if (next == null)
		{
			return null;
		}
		submodule.setDiemRank(next);
		spiritweb.syncToClients(null);
		player.sendSystemMessage(Component.translatable("diem.sandmastery.promoted", next.getDisplayName()));
		return next;
	}

	// practice: charge spent becomes mastery xp, which grows ribbons up to the rank's practice cap.
	public static void addPracticeXp(ISpiritweb spiritweb, int chargeSpent)
	{
		if (chargeSpent <= 0 || !(spiritweb.getLiving() instanceof ServerPlayer player))
		{
			return;
		}
		SandmasterySpiritwebSubmodule submodule = SandmasterySpiritwebSubmodule.get(spiritweb);
		DiemRank rank = submodule.getDiemRank();
		int ribbons = SandmasterySpiritwebSubmodule.getRankRibbons(player);
		if (rank == null || ribbons >= rank.getPracticeCap())
		{
			return;
		}

		long xp = (long) submodule.getMasteryXp() + chargeSpent;
		long needed = (long) SandmasteryConfigs.SERVER.RIBBON_XP_PER_RIBBON.get() * Math.max(1, ribbons);
		if (xp < needed)
		{
			submodule.setMasteryXp((int) xp);
			return;
		}

		AttributeInstance attribute = player.getAttribute(SandmasteryAttributes.RIBBONS.getHolder());
		if (attribute == null)
		{
			return;
		}
		spiritweb.giveManifestation(SandmasteryManifestations.SANDMASTERY_POWERS.get(Taldain.Mastery.PROJECTILE).get(), (int) attribute.getBaseValue() + 1);
		submodule.setMasteryXp(0);
		spiritweb.syncToClients(null);

		player.sendSystemMessage(Component.translatable("diem.sandmastery.ribbon_gained", ribbons + 1));
		DiemRank next = rank.next();
		if (ribbons + 1 >= rank.getPracticeCap() && next != null)
		{
			player.sendSystemMessage(Component.translatable("diem.sandmastery.practice_capped", next.getDisplayName()));
		}
	}

	public static boolean nextRankEligible(ServerPlayer player, ISpiritweb spiritweb)
	{
		DiemRank rank = SandmasterySpiritwebSubmodule.get(spiritweb).getDiemRank();
		DiemRank next = rank == null ? null : rank.next();
		return next != null && SandmasterySpiritwebSubmodule.getRankRibbons(player) >= next.getMinRibbons();
	}

	public static void sendStatus(ServerPlayer player, ISpiritweb spiritweb)
	{
		DiemRank rank = SandmasterySpiritwebSubmodule.get(spiritweb).getDiemRank();
		if (rank == null)
		{
			return;
		}
		int ribbons = SandmasterySpiritwebSubmodule.getRankRibbons(player);
		player.sendSystemMessage(Component.translatable("diem.sandmastery.status", rank.getDisplayName(), ribbons));

		DiemRank next = rank.next();
		if (next == null)
		{
			player.sendSystemMessage(Component.translatable("diem.sandmastery.highest_rank"));
		}
		else if (ribbons >= next.getMinRibbons())
		{
			player.sendSystemMessage(Component.translatable("diem.sandmastery.trial_ready", next.getDisplayName()));
		}
		else
		{
			player.sendSystemMessage(Component.translatable("diem.sandmastery.trial_locked", next.getMinRibbons(), next.getDisplayName()));
		}
	}
}
