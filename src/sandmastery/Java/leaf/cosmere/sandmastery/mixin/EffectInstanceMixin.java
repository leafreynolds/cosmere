package leaf.cosmere.sandmastery.mixin;

import leaf.cosmere.sandmastery.common.effects.DehydratedEffect;
import leaf.cosmere.sandmastery.common.effects.OvermasteryEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectInstance.class)
public class EffectInstanceMixin
{
	@Inject(at = @At("RETURN"),
			method = "isNoCounter",
			cancellable = true
	)
	private void getIsSandmasteryEffect(CallbackInfoReturnable<Boolean> cir)
	{
		if (getEffect() instanceof DehydratedEffect || getEffect() instanceof OvermasteryEffect)
		{
			// always show **:** instead of the 3 second timer
			cir.setReturnValue(true);
		}
	}

	@Inject(at = @At("RETURN"),
			method = "showIcon",
			cancellable = true
	)
	private void getShowIcon(CallbackInfoReturnable<Boolean> cir)
	{
		if (getEffect() instanceof DehydratedEffect || getEffect() instanceof OvermasteryEffect)
		{
			//never show the icon on the normal HUD.
			//should only show in inventory if they're interested.
			cir.setReturnValue(false);
		}
	}


	@Shadow
	public MobEffect getEffect()
	{
		throw new IllegalStateException("Mixin failed to shadow getPotion()");
	}
}