/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.mixin;

import leaf.cosmere.allomancy.common.manifestation.AllomancyBronze;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
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
			if (!AllomancyBronze.contestConcealment(playerSpiritweb, target))
			{
				//close out of consumer, does not meet requirements for seeking.
				//either not using bronze, not in range, or not strong enough
				return;
			}

			//okay, now we can actually check if they have powers we care about
			SpiritwebCapability.get(target).ifPresent(targetSpiritweb ->
			{
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

				if (found)
				{
					cir.setReturnValue(true);
				}
			});
		});
	}

}

