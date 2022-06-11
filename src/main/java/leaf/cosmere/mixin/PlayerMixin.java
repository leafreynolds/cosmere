/*
 * File created ~ 6 - 6 - 2021 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.manifestation.feruchemy.FeruchemyAtium;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin
{
	/*@Inject(at = @At("RETURN"), method = "getSpeed", cancellable = true)
	protected void handleGetSpeed(CallbackInfoReturnable<Float> cir)
	{
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		cir.setReturnValue(cir.getReturnValue() * (FeruchemyAtium.getScale(livingEntity)));
	}*/
}

