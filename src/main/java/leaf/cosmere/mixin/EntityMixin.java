/*
 * File created ~ 6 - 6 - 2021 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.allomancy.AllomancyBase;
import leaf.cosmere.manifestation.allomancy.AllomancyCopper;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin
{
	@Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
	private void handleIsGlowing(CallbackInfoReturnable<Boolean> cir)
	{
		Entity e = (Entity) (Object) this;

		final boolean isServerSide = !(e.level.isClientSide);
		final boolean isInanimateEntity = !(e instanceof LivingEntity);
		if (isServerSide || isInanimateEntity)
		{
			return;
		}

		Player clientPlayer = (Player) Minecraft.getInstance().getCameraEntity();
		LivingEntity target = (LivingEntity) e;

		SpiritwebCapability.get(clientPlayer).ifPresent(playerSpiritweb ->
		{
			final AllomancyBase bronzeAllomancyManifestation = ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.BRONZE).get();
			//if the player does not have bronze, early exit
			if (!playerSpiritweb.hasManifestation(bronzeAllomancyManifestation)
					|| !playerSpiritweb.canTickManifestation(bronzeAllomancyManifestation))
			{
				return;
			}
			final double bronzeStrength = bronzeAllomancyManifestation.getStrength(playerSpiritweb, false);

			SpiritwebCapability.get(target).ifPresent(targetSpiritweb ->
			{
				//if target has copper and it's active, early exit
				final AllomancyBase copperAllomancy = ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.COPPER).get();
				final double copperStrength = copperAllomancy.getStrength(targetSpiritweb, false);

				if (copperAllomancy.isMetalBurning(targetSpiritweb) && copperStrength >= bronzeStrength)
				{
					return;
				}

				//get allomantic strength of

				//todo range to config
				double range = bronzeAllomancyManifestation.getRange(playerSpiritweb);
				boolean found = false;

				for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
				{
					//don't tick powers that the user doesn't have
					//don't tick powers that are not active
					final boolean targetIsPlayer = target instanceof Player;

					//if target is not a player and has any manifestations at all
					if (!targetIsPlayer && targetSpiritweb.hasManifestation(manifestation))
					{
						found = true;
						break;
					}
					//if target is player and has any active manifestations,
					else if (targetIsPlayer && manifestation.isActive(targetSpiritweb))
					{
						found = true;
						break;
					}
				}

				if ((clientPlayer != null && clientPlayer.distanceTo(target) < range) && found)
				{
					cir.setReturnValue(true);
				}
			});
		});
	}

}

