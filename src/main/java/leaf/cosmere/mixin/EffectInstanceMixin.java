/*
 * File created ~ 29 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import leaf.cosmere.registry.EffectsRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EffectInstance.class)
public class EffectInstanceMixin
{
    @Inject(
            at = @At("RETURN"),
            method = "getIsPotionDurationMax",
            cancellable = true
    )
    private void getIsFeruchemyEffect(CallbackInfoReturnable<Boolean> cir)
    {
        if (getPotion() instanceof FeruchemyEffectBase)
        {
            cir.setReturnValue(true);
        }
    }

    @Shadow
    public Effect getPotion() {
        throw new IllegalStateException("Mixin failed to shadow getPotion()");
    }
}
