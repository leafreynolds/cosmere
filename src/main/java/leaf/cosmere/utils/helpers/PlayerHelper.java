/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.*;

import java.util.Optional;
import java.util.UUID;

public class PlayerHelper
{
	public static String getPlayerName(UUID id, MinecraftServer server)
	{
		if (server != null)
		{
			final Optional<GameProfile> gameProfile = server.getProfileCache().get(id);
			if (gameProfile.isPresent())
			{
				return gameProfile.get().getName();
			}
		}
		return "OFFLINE Player";
	}

	//Basically a modified copy of the pick function in GameRenderer.java, but with the ability to set pick distance
	public static HitResult pickWithRange(LivingEntity player, int range)
	{
		final Minecraft minecraft = Minecraft.getInstance();
		minecraft.getProfiler().push("cosmere-pick");

		float partialTicks = minecraft.getFrameTime();

		HitResult hitResult = player.pick(range, partialTicks, false);
		Vec3 eyePosition = player.getEyePosition(partialTicks);
		double distToBlock = hitResult.getLocation().distanceToSqr(eyePosition);

		Vec3 viewVector = player.getViewVector(1.0F);
		Vec3 viewVectorFromEyes = eyePosition.add(viewVector.x * (double) range, viewVector.y * (double) range, viewVector.z * (double) range);

		AABB aabb = player.getBoundingBox().expandTowards(viewVector.scale(range)).inflate(1.0D, 1.0D, 1.0D);
		EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(player, eyePosition, viewVectorFromEyes, aabb, potentialEntityHit -> !potentialEntityHit.isSpectator(), distToBlock);

		if (entityHitResult != null)
		{
			Vec3 entityHitResultLocation = entityHitResult.getLocation();
			double distToEntity = eyePosition.distanceToSqr(entityHitResultLocation);
			if (distToEntity < distToBlock)
			{
				hitResult = entityHitResult;
			}
		}

		minecraft.getProfiler().pop();
		return hitResult;
	}
}
