/*
 * File created ~ 29 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
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
    @Inject(at = @At("RETURN"),
            method = "isNoCounter",
            cancellable = true
    )
    private void getIsFeruchemyEffect(CallbackInfoReturnable<Boolean> cir)
    {
        if (getEffect() instanceof FeruchemyEffectBase)
        {
            cir.setReturnValue(true);
        }
    }
    @Inject(at = @At("RETURN"),
            method = "showIcon",
            cancellable = true
    )
    private void getShowIcon(CallbackInfoReturnable<Boolean> cir)
    {
        if (getEffect() instanceof FeruchemyEffectBase)
        {
            cir.setReturnValue(false);
        }
    }


    @Shadow
    public Effect getEffect() {
        throw new IllegalStateException("Mixin failed to shadow getPotion()");
    }
}
