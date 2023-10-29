/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.mixin;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyAtium;
import leaf.cosmere.feruchemy.common.registries.FeruchemyEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(Entity.class)
public class EntityMixin
{

	@Inject(at = @At("RETURN"), method = "canEnterPose", cancellable = true)
	public void handleCanEnterPose(Pose pose, CallbackInfoReturnable<Boolean> cir)
	{
		Entity entity = (Entity) (Object) this;

		if (entity instanceof LivingEntity livingEntity)
		{
			float scale = FeruchemyAtium.getScale(livingEntity);
			if (scale > 0.01 || scale < -0.01)
			{
				EntityDimensions entityDimensions = livingEntity.getDimensions(pose);
				entityDimensions = entityDimensions.scale(scale);
				double f = entityDimensions.width / 2.0F;
				Vec3 vector3d = new Vec3(livingEntity.getX() - f, livingEntity.getY(), livingEntity.getZ() - f);
				Vec3 vector3d1 = new Vec3(livingEntity.getX() + f, livingEntity.getY() + (double) entityDimensions.height, livingEntity.getZ() + f);
				AABB box = new AABB(vector3d, vector3d1);

				cir.setReturnValue(livingEntity.level.noCollision(livingEntity, box.deflate(1.0E-7D)));
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "isSteppingCarefully", cancellable = true)
	public void handleIsSteppingCarefully(CallbackInfoReturnable<Boolean> cir)
	{
		//exit out early if they're already stepping lightly
		if (cir.getReturnValue())
		{
			return;
		}

		Entity entity = (Entity) (Object) this;
		if (entity instanceof LivingEntity livingEntity)
		{
			SpiritwebCapability.get(livingEntity).ifPresent(data ->
			{
				CosmereEffect iron = FeruchemyEffects.STORING_EFFECTS.get(Metals.MetalType.IRON).get();
				if (data.totalStrengthOfEffect(iron) > 2)
				{
					cir.setReturnValue(true);
				}
			});
		}
	}

	//region Feru steel run on water
	//Special thanks to ExpandAbility api mod for showing how this works!
	//https://github.com/florensie/ExpandAbility/blob/9b80bb256ed4b9dd305c3e1c3b3f6f730b57a237/common/src/main/java/be/florens/expandability/mixin/fluidcollision/EntityMixin.java
	// originally they used @modify variable on the return of collide, but I want to use CallbackInfoReturnable to set value inside getting spiritweb data
	@Inject(at = @At("RETURN"), method = "collide", cancellable = true)
	private void collideWithFluid(CallbackInfoReturnable<Vec3> cir)
	{
		Vec3 originalDisplacement = cir.getReturnValue();

		Entity entity = (Entity) (Object) this;
		if (!(entity instanceof LivingEntity livingEntity) || livingEntity.isCrouching())
		{
			return;
		}

		SpiritwebCapability.get(livingEntity).ifPresent(data ->
		{
			CosmereEffect steel = FeruchemyEffects.TAPPING_EFFECTS.get(Metals.MetalType.STEEL).get();
			if (data.totalStrengthOfEffect(steel) > 5)
			{
				if (originalDisplacement.y <= 0.0 && !isTouchingFluid(livingEntity, livingEntity.getBoundingBox().deflate(0.001D)))
				{
					Map<Vec3, Double> points = findFluidDistances(livingEntity, originalDisplacement);
					Double highestDistance = null;

					for (Map.Entry<Vec3, Double> point : points.entrySet())
					{
						if (highestDistance == null || (point.getValue() != null && point.getValue() > highestDistance))
						{
							highestDistance = point.getValue();
						}
					}

					if (highestDistance != null)
					{
						Vec3 finalDisplacement = new Vec3(originalDisplacement.x, highestDistance, originalDisplacement.z);
						AABB finalBox = livingEntity.getBoundingBox().move(finalDisplacement).deflate(0.001D);
						if (!isTouchingFluid(livingEntity, finalBox))
						{
							livingEntity.fallDistance = 0.0F;
							livingEntity.setOnGround(true);
							cir.setReturnValue(finalDisplacement);
						}
					}
				}
			}
		});

		//do nothing, use original value
	}

	//Special thanks to ExpandAbility api mod for showing how this works!
	@Unique
	private static Map<Vec3, Double> findFluidDistances(LivingEntity entity, Vec3 originalDisplacement)
	{
		AABB box = entity.getBoundingBox().move(originalDisplacement);

		HashMap<Vec3, Double> points = new HashMap<>();
		points.put(new Vec3(box.minX, box.minY, box.minZ), null);
		points.put(new Vec3(box.minX, box.minY, box.maxZ), null);
		points.put(new Vec3(box.maxX, box.minY, box.minZ), null);
		points.put(new Vec3(box.maxX, box.minY, box.maxZ), null);

		double fluidStepHeight = entity.isOnGround() ? Math.max(1.0, entity.maxUpStep) : 0.0;

		for (Map.Entry<Vec3, Double> entry : points.entrySet())
		{
			for (int i = 0; ; i--)
			{
				BlockPos landingPos = new BlockPos(entry.getKey()).offset(0.0, i + fluidStepHeight, 0.0);
				FluidState landingState = entity.getCommandSenderWorld().getFluidState(landingPos);

				double distanceToFluidSurface = landingPos.getY() + landingState.getOwnHeight() - entity.getY();
				double limitingVelocity = originalDisplacement.y;

				if (distanceToFluidSurface < limitingVelocity || distanceToFluidSurface > fluidStepHeight)
				{
					break;
				}

				if (!landingState.isEmpty())
				{
					entry.setValue(distanceToFluidSurface);
					break;
				}
			}
		}

		return points;
	}

	//Special thanks to ExpandAbility api mod for showing how this works!
	@Unique
	private static boolean isTouchingFluid(LivingEntity entity, AABB box)
	{
		int minX = Mth.floor(box.minX);
		int maxX = Mth.ceil(box.maxX);
		int minY = Mth.floor(box.minY);
		int maxY = Mth.ceil(box.maxY);
		int minZ = Mth.floor(box.minZ);
		int maxZ = Mth.ceil(box.maxZ);
		Level world = entity.getCommandSenderWorld();

		if (world.hasChunksAt(minX, minY, minZ, maxX, maxY, maxZ))
		{
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

			for (int i = minX; i < maxX; ++i)
			{
				for (int j = minY; j < maxY; ++j)
				{
					for (int k = minZ; k < maxZ; ++k)
					{
						mutable.set(i, j, k);
						FluidState fluidState = world.getFluidState(mutable);

						if (!fluidState.isEmpty())
						{
							double surfaceY = fluidState.getHeight(world, mutable) + j;

							if (surfaceY >= box.minY)
							{
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}
	//endregion
}

