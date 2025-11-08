/*
 * File updated ~ 8 - 11 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.entity;

import leaf.cosmere.surgebinding.common.registries.SurgebindingEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Chull extends AbstractChestedHorse
{
	public Chull(EntityType<? extends Chull> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}

	public static AttributeSupplier.Builder createAttributes()
	{
		return Monster.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, (double) 0.35F)
				.add(Attributes.FOLLOW_RANGE, 12.0D)
				.add(Attributes.MAX_HEALTH, 24.0D)
				.add(Attributes.JUMP_STRENGTH, 0.1D)
				.add(Attributes.ATTACK_DAMAGE, 5.0D);
	}

	public boolean canMate(Animal pOtherAnimal)
	{
		if (pOtherAnimal != this && pOtherAnimal instanceof Chull otherChull)
		{
			return this.canParent() && otherChull.canParent();
		}

		return false;
	}

	@Override
	public int getMaxSpawnClusterSize()
	{
		return 4;
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent)
	{
		EntityType<? extends AbstractHorse> entitytype = SurgebindingEntityTypes.CHULL.getEntityType();
		AbstractHorse abstracthorse = entitytype.create(pLevel);
		this.setOffspringAttributes(pOtherParent, abstracthorse);

		//todo texture variants?

		return abstracthorse;
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit)
	{
		super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);

		//chull chests??

		//gems?

		//this.getAge()

		//todo baby chulls drop chips, adults drop marks
	}

	@Override
	protected Vec3 getLeashOffset()
	{
		return new Vec3(0.0D, (double) this.getEyeHeight(Pose.STANDING), (double) (this.getBbWidth() * 0.25F));
	}

	@Override
	public float getEyeHeight(Pose pPose)
	{
		return getEyeHeight() * 0.1f;
	}

	@Override
	public double getPassengersRidingOffset()
	{
		return ((this.getBbHeight() / 2f) - 0.75d);
	}
}
