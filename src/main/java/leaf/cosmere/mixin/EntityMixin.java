/*
 * File created ~ 6 - 6 - 2021 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin
{
    @Inject(method = "isGlowing", at = @At("RETURN"), cancellable = true)
    private void handleIsGlowing(CallbackInfoReturnable<Boolean> cir)
    {
        Entity e = (Entity) (Object) this;

        final boolean isServerSide = !(e.level.isClientSide);
        final boolean isInanimateEntity = !(e instanceof LivingEntity);
        if (isServerSide || isInanimateEntity)
        {
            return;
        }

        PlayerEntity clientPlayer = (PlayerEntity) Minecraft.getInstance().getCameraEntity();
        LivingEntity target = (LivingEntity) e;

        SpiritwebCapability.get(clientPlayer).ifPresent(playerSpiritweb ->
        {
            //if the player does not have bronze, early exit
            final boolean playerHasBronzeAllomancy = playerSpiritweb.hasManifestation(
                    Manifestations.ManifestationTypes.ALLOMANCY,
                    Metals.MetalType.BRONZE.getID()
            );

            final boolean playerBronzeBurning = playerSpiritweb.canTickManifestation(
                    Manifestations.ManifestationTypes.ALLOMANCY,
                    Metals.MetalType.BRONZE.getID()
            );

            if (!playerHasBronzeAllomancy || !playerBronzeBurning)
            {
                return;
            }

            SpiritwebCapability.get(target).ifPresent(targetSpiritweb ->
            {
                //if target has copper and it's active, early exit
                if (ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.COPPER).get().isActive(targetSpiritweb))
                {
                    //check whether or not the target beats the player in allomantic power
                    return;
                }

                //get allomantic strength of

                int mode = playerSpiritweb.getMode(Manifestations.ManifestationTypes.ALLOMANCY, Metals.MetalType.BRONZE.getID());

                //todo range to config
                int range = 5 * mode;
                boolean found = false;

                //if target is player and has any active manifestations,


                //or if target is not a player and has any manifestations at all

                for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
                {
                    //don't tick powers that the user doesn't have
                    //don't tick powers that are not active
                    final boolean targetIsPlayer = target instanceof PlayerEntity;
                    if (!targetIsPlayer && targetSpiritweb.hasManifestation(manifestation.getManifestationType(), manifestation.getPowerID()))
                    {
                        found = true;
                        break;
                    }
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

