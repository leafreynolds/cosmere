/*
 * File updated ~ 7 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.mixin;

import leaf.cosmere.allomancy.common.manifestation.AllomancyManifestation;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
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


			//todo range to config
			//get allomantic strength of
			double range = bronzeAllomancyManifestation.getRange(playerSpiritweb);
			final boolean inRangeOfBronze = clientPlayer != null && clientPlayer.distanceTo(target) < range;
			if (!inRangeOfBronze)
			{
				return;
			}
			//if target has copper and it's active, early exit
			final AttributeMap targetAttributes = target.getAttributes();
			double concealmentStrength = 0;
			final Attribute cognitiveConcealmentAttr = AttributesRegistry.COGNITIVE_CONCEALMENT.get();
			if (targetAttributes.hasAttribute(cognitiveConcealmentAttr))
			{
				concealmentStrength = targetAttributes.getValue(cognitiveConcealmentAttr);
			}

			//do they have more concealment than the player has bronze strength?
			if (concealmentStrength >= bronzeStrength)
			{
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

