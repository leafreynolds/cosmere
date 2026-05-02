package leaf.cosmere.surgebinding.common.utils;

import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Map;
import java.util.Random;

public class ParticleHelper
{
	public static void spawnLeakEffect(ServerLevel level, int num, LivingEntity entity)
	{
		ParticleOptions particleOptions = ParticleTypes.EFFECT;
		for(int i = 0; i < num; i++)
		{
			Random rand = new Random();
			double xRand = entity.getX() + rand.nextDouble(-0.5, 0.5);
			double y = entity.getY() + 1.5D;
			double zRand = entity.getZ() + rand.nextDouble(-0.5, 0.5);
			level.sendParticles(particleOptions, xRand, y, zRand, 0, 0.5, 0, 0,0);
		}
	}
	public static void spawnBurstEffect(ServerLevel level, LivingEntity entity)
	{
		ParticleOptions particleOptions = new DustParticleOptions( new Vector3f(149F/255F,
					242F/255F,
					252F/255F),
				1.0F);
		for(int i = 0; i < 72; i++)
		{
			double rad = Math.toRadians(i*5);
			double x = ((2D) * Math.cos(rad));
			double xSpeed = Math.cos(rad) * 0.75D;
			double z = (2D) * Math.sin(rad);
			double zSpeed = Math.sin(rad) * 0.75D;
			double ySpeed = 0.2D;
			level.addParticle(particleOptions, true, x, entity.getY(), z, xSpeed, ySpeed, zSpeed);
		}
	}

}

