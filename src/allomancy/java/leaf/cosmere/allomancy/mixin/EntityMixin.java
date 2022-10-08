/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.mixin;

import leaf.cosmere.allomancy.common.manifestation.AllomancyManifestation;
import leaf.cosmere.allomancy.common.registries.AllomancyEffects;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
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
			final AllomancyManifestation bronzeAllomancyManifestation = AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.BRONZE).get();
			//if the player does not have bronze, early exit
			if (!bronzeAllomancyManifestation.isActive(playerSpiritweb))
			{
				return;
			}
			final double bronzeStrength = bronzeAllomancyManifestation.getStrength(playerSpiritweb, false);

			SpiritwebCapability.get(target).ifPresent(targetSpiritweb ->
			{
				//if target has copper and it's active, early exit
				MobEffectInstance effect = targetSpiritweb.getLiving().getEffect(AllomancyEffects.ALLOMANTIC_COPPER.get());
				final double copperCloudStrength =
						effect != null && effect.getDuration() > 0
						? effect.getAmplifier() : 0;

				if (copperCloudStrength >= bronzeStrength)
				{
					return;
				}

				//get allomantic strength of

				//todo range to config
				double range = bronzeAllomancyManifestation.getRange(playerSpiritweb);
				boolean found = false;

				for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
				{
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

