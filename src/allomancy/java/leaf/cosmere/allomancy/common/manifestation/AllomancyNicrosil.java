/*
 * File updated ~ 2 - 9 - 2026 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.registries.AllomancyEffects;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffectInstance;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class AllomancyNicrosil extends AllomancyManifestation
{
	public AllomancyNicrosil(Metals.MetalType metalType)
	{
		super(metalType);
	}

	//active or not active
	@Override
	public int modeMax(ISpiritweb data)
	{
		return 1;
	}

	@Override
	public int modeMin(ISpiritweb data)
	{
		return 0;
	}

	@Override
	public boolean modeWraps(ISpiritweb data)
	{
		return false;
	}

	//Enhances Allomantic Burn of Target
	public static void onLivingHurtEvent(LivingIncomingDamageEvent event)
	{
		if (event.isCanceled())
		{
			return;
		}

		Entity trueSource = event.getSource().getEntity();
		if (trueSource instanceof Player trueSourcePlayer)
		{
			boostTarget(trueSourcePlayer, event.getEntity());
		}
	}

	//touching someone works just as well as punching them
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (event.isCanceled() || event.getLevel().isClientSide)
		{
			return;
		}

		if (event.getTarget() instanceof LivingEntity target && boostTarget(event.getEntity(), target))
		{
			//consume the interaction so we don't also mount/trade with them
			event.setCancellationResult(InteractionResult.SUCCESS);
			event.setCanceled(true);
		}
	}

	//returns true if the boost was applied
	private static boolean boostTarget(Player sourcePlayer, LivingEntity target)
	{
		//nicrosil is passed on by touch, so the hand has to be free
		if (!sourcePlayer.getMainHandItem().isEmpty())
		{
			return false;
		}

		return SpiritwebCapability.get(sourcePlayer).map(iSpiritweb ->
		{
			AllomancyNicrosil alloNicrosil = (AllomancyNicrosil) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.NICROSIL).get();

			//if manifestation is active and has nicrosil metal to burn
			if (!alloNicrosil.isActive(iSpiritweb))
			{
				return false;
			}

			//valid set up found.
			CosmereEffectInstance newEffect = EffectsHelper.getNewEffect(
					AllomancyEffects.ALLOMANCY_BOOST.get(),
					iSpiritweb.getLiving(),
					(alloNicrosil.getStrength(iSpiritweb, false))
			);

			//apply to the touched entity
			return SpiritwebCapability.get(target).map(targetSpiritweb ->
			{
				targetSpiritweb.addEffect(newEffect, sourcePlayer);
				return true;
			}).orElse(false);
		}).orElse(false);
	}
}
