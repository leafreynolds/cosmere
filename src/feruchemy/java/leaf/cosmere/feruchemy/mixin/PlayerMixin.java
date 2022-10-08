/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

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

