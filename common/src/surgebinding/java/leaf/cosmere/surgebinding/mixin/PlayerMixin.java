/*
 * File updated ~ 23 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.mixin;

import leaf.cosmere.surgebinding.common.manifestation.SurgeGravitation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public class PlayerMixin
{
	@Redirect(
			method = "tryToStartFallFlying",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z",
					remap = false
			)
	)
	public boolean tryToStartFallFlying(ItemStack stack, LivingEntity entity)
	{
		return stack.canElytraFly(entity) || SurgeGravitation.canFly(entity);
	}
}
