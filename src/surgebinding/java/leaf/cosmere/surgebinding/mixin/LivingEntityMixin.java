/*
 * File updated ~ 23 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.mixin;

import leaf.cosmere.surgebinding.common.manifestation.SurgeGravitation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
	@Redirect(
			method = "updateFallFlying",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z",
					remap = false
			)
	)
	public boolean canElytraFly(ItemStack stack, LivingEntity entity)
	{
		return stack.canElytraFly(entity) || SurgeGravitation.canFly(entity);
	}

	@Redirect(
			method = "updateFallFlying",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;elytraFlightTick(Lnet/minecraft/world/entity/LivingEntity;I)Z",
					remap = false
			)
	)
	public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks)
	{
		return SurgeGravitation.flyTick(entity) || stack.elytraFlightTick(entity, flightTicks);
	}
}
