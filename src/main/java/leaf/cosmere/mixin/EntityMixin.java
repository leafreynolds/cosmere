/*
 * File created ~ 6 - 6 - 2021 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.allomancy.AllomancyBase;
import leaf.cosmere.manifestation.feruchemy.FeruchemyAtium;
import leaf.cosmere.manifestation.feruchemy.FeruchemyBase;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(Entity.class)
public class EntityMixin
{
	@Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
	private void handleIsGlowing(CallbackInfoReturnable<Boolean> cir)
	{
		Entity e = (Entity) (Object) this;

		final boolean isServerSide = !(e.level.isClientSide);
		final boolean isInanimateEntity = !(e instanceof LivingEntity);
		if (isServerSide || isInanimateEntity)
		{
			return;
		}

		Player clientPlayer = (Player) Minecraft.getInstance().getCameraEntity();
		LivingEntity target = (LivingEntity) e;

		SpiritwebCapability.get(clientPlayer).ifPresent(playerSpiritweb ->
		{
			final AllomancyBase bronzeAllomancyManifestation = ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.BRONZE).get();
			//if the player does not have bronze, early exit
			if (!playerSpiritweb.hasManifestation(bronzeAllomancyManifestation)
					|| !playerSpiritweb.canTickManifestation(bronzeAllomancyManifestation))
			{
				return;
			}
			final double bronzeStrength = bronzeAllomancyManifestation.getStrength(playerSpiritweb, false);

			SpiritwebCapability.get(target).ifPresent(targetSpiritweb ->
			{
				//if target has copper and it's active, early exit
				MobEffectInstance effect = targetSpiritweb.getLiving().getEffect(EffectsRegistry.ALLOMANTIC_COPPER.get());
				final double copperCloudStrength =
						effect != null && effect.getDuration() > 0
						? effect.getAmplifier() : 0;

				if (copperCloudStrength >= bronzeStrength)
				{
					return;
				}

				//get allomantic strength of

				//todo range to config
				double range = bronzeAllomancyManifestation.getRange(playerSpiritweb);
				boolean found = false;

				for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
				{
					//don't tick powers that the user doesn't have
					//don't tick powers that are not active
					final boolean targetIsPlayer = target instanceof Player;

					//if target is not a player and has any manifestations at all
					if (!targetIsPlayer && targetSpiritweb.hasManifestation(manifestation))
					{
						found = true;
						break;
					}
					//if target is player and has any active manifestations,
					else if (targetIsPlayer && manifestation.isActive(targetSpiritweb))
					{
						found = true;
						break;
					}
				}

				if ((clientPlayer != null && clientPlayer.distanceTo(target) < range) && found)
				{
					cir.setReturnValue(true);
				}
			});
		});
	}


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

}

