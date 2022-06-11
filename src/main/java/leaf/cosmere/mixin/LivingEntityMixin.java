/*
 * File created ~ 6 - 6 - 2021 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.manifestation.feruchemy.FeruchemyAtium;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
	@Inject(at = @At("RETURN"), method = "getJumpPower", cancellable = true)
	protected void handleGetJumpPower(CallbackInfoReturnable<Float> cir)
	{
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		cir.setReturnValue(cir.getReturnValue() * FeruchemyAtium.getScale(livingEntity));
	}

	@Inject(at = @At("RETURN"), method = "getSpeed", cancellable = true)
	protected void handleGetSpeed(CallbackInfoReturnable<Float> cir)
	{
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		cir.setReturnValue(cir.getReturnValue() * FeruchemyAtium.getScale(livingEntity));
	}
}

