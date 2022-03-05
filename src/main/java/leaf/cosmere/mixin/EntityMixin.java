/*
 * File created ~ 6 - 6 - 2021 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
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

        if (!(e instanceof LivingEntity))
        {
            return;
        }

        PlayerEntity clientPlayer = (PlayerEntity) Minecraft.getInstance().getCameraEntity();
        LivingEntity target = (LivingEntity) e;

        SpiritwebCapability.get(clientPlayer).ifPresent(playerSpiritweb ->
        {
            //if has bronze
            if (!(playerSpiritweb.hasManifestation(Manifestations.ManifestationTypes.ALLOMANCY, Metals.MetalType.BRONZE.getID())))
            {
                return;
            }

            SpiritwebCapability.get(target).ifPresent(targetSpiritweb ->
            {
                //if has copper
                if (ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.COPPER).get().isActive(targetSpiritweb))
                {
                    //check whether or not the target beats the player in allomantic power



                    return;
                }


                //get allomantic strength of

                int mode = playerSpiritweb.getMode(Manifestations.ManifestationTypes.ALLOMANCY, Metals.MetalType.BRONZE.getID());

                //todo range to config
                int range = 5 * mode;


                //get allomantic strength of copper


                //if targets copper is on and their mode and strength is stronger than the player
                //then close out


                if (clientPlayer != null && clientPlayer.distanceTo(target) < range)
                {
                    cir.setReturnValue(true);
                }
            });
        });
    }

}

