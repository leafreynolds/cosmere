package leaf.cosmere.sandmastery.common.diem.trial;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;

// trials must be passed with Sandmastery alone. These are the generic checks, I'm not gonna try and implement a full on anticheat
public final class OutsideHelp
{
	// the only items a trialist may wear or hold in the offhand
	public static final TagKey<Item> ALLOWED_ITEMS = ItemTags.create(Sandmastery.rl("diem_trial_allowed"));

	private OutsideHelp()
	{
	}

	// checked every tick during a trial
	@Nullable
	public static Component duringTrial(ServerPlayer player)
	{
		if (player.isCreative() || player.isSpectator())
		{
			return reason("game_mode");
		}
		// mayFly covers vanilla flight and NeoForge's creative flight attribute
		if (player.mayFly() || player.getAbilities().flying)
		{
			return reason("flight");
		}
		if (player.isFallFlying())
		{
			return reason("gliding");
		}
		if (player.isPassenger())
		{
			return reason("riding");
		}
		if (isUsingOtherPowers(player))
		{
			return reason("other_powers");
		}
		return null;
	}

	// checked before a trial starts: everything above, plus effects and equipment
	@Nullable
	public static Component beforeTrial(ServerPlayer player)
	{
		Component reason = duringTrial(player);
		if (reason != null)
		{
			return reason;
		}
		for (MobEffectInstance effect : player.getActiveEffects())
		{
			if (isBeneficial(effect))
			{
				return reason("effects");
			}
		}
		if (isWearingForbiddenItems(player))
		{
			return reason("equipment");
		}
		return null;
	}

	public static boolean isBeneficial(MobEffectInstance effect)
	{
		return effect.getEffect().value().getCategory() == MobEffectCategory.BENEFICIAL;
	}

	private static boolean isUsingOtherPowers(ServerPlayer player)
	{
		return SpiritwebCapability.get(player).map(spiritweb ->
		{
			for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
			{
				boolean otherPower = manifestation.getManifestationType() != Manifestations.ManifestationTypes.SANDMASTERY;
				if (otherPower && spiritweb.getMode(manifestation) != 0)
				{
					return true;
				}
			}
			return false;
		}).orElse(false);
	}

	private static boolean isWearingForbiddenItems(ServerPlayer player)
	{
		for (ItemStack armour : player.getArmorSlots())
		{
			if (isForbidden(armour))
			{
				return true;
			}
		}
		if (isForbidden(player.getOffhandItem()))
		{
			return true;
		}
		return CuriosApi.getCuriosInventory(player)
				.map(curios -> !curios.findCurios(OutsideHelp::isForbidden).isEmpty())
				.orElse(false);
	}

	private static boolean isForbidden(ItemStack stack)
	{
		return !stack.isEmpty() && !stack.is(ALLOWED_ITEMS);
	}

	private static Component reason(String key)
	{
		return Component.translatable("diem.sandmastery.trial.outside_help." + key);
	}
}
