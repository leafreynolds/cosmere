package leaf.cosmere.surgebinding.mixin;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyVariable(
            method = "move",
            at = @At("HEAD")
    )
    private Vec3 move(Vec3 pPos) {
        return pPos.xRot((float) Math.toRadians(90));
    }

    @ModifyVariable(
            method = "Lnet/minecraft/world/entity/Entity;collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/level/Level;getEntityCollisions(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
                    ordinal = 0
            ),
            ordinal = 0
    )
    private Vec3 collide(Vec3 pPos) {
        return pPos.xRot((float) Math.toRadians(90));
    }

    @Inject(
            method = "Lnet/minecraft/world/entity/Entity;collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void collide(CallbackInfoReturnable<Vec3> cir) {
        cir.setReturnValue(cir.getReturnValue().xRot((float) Math.toRadians(90)));
    }


}
