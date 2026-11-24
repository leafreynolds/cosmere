package leaf.cosmere.sandmastery.common.entities;

import leaf.cosmere.sandmastery.common.diem.trial.DiemTrials;
import leaf.cosmere.sandmastery.common.registries.SandmasteryEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class DiemTargetEntity extends Entity
{
	private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(DiemTargetEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> DECOY = SynchedEntityData.defineId(DiemTargetEntity.class, EntityDataSerializers.BOOLEAN);

	private UUID owner;
	private boolean moving;
	private Vec3 home = Vec3.ZERO;
	private Vec3 orbitCentre = Vec3.ZERO;

	public DiemTargetEntity(EntityType<? extends DiemTargetEntity> type, Level level)
	{
		super(type, level);
		noPhysics = true;
	}

	public static DiemTargetEntity create(Level level, ServerPlayer owner, Vec3 pos, Vec3 orbitCentre, float size, boolean decoy, boolean moving)
	{
		DiemTargetEntity target = new DiemTargetEntity(SandmasteryEntityTypes.DIEM_TARGET.get(), level);
		target.owner = owner.getUUID();
		target.moving = moving;
		target.home = pos;
		target.orbitCentre = orbitCentre;
		target.entityData.set(SIZE, size);
		target.entityData.set(DECOY, decoy);
		target.setPos(pos);
		return target;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder)
	{
		builder.define(SIZE, 1F);
		builder.define(DECOY, false);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key)
	{
		if (SIZE.equals(key))
		{
			refreshDimensions();
		}
		super.onSyncedDataUpdated(key);
	}

	@Override
	public EntityDimensions getDimensions(Pose pose)
	{
		return EntityDimensions.scalable(getSize(), getSize());
	}

	public float getSize()
	{
		return entityData.get(SIZE);
	}

	public boolean isDecoy()
	{
		return entityData.get(DECOY);
	}

	@Override
	public void tick()
	{
		super.tick();
		if (level().isClientSide)
		{
			return;
		}
		// orphaned when its trial ended without cleaning up (crash, owner gone)
		if (owner == null || !DiemTrials.ownsTarget(owner, this))
		{
			discard();
			return;
		}
		if (moving)
		{
			// circle the stone slowly and bob up and down
			double orbitRadius = Math.sqrt(home.distanceToSqr(orbitCentre.x, home.y, orbitCentre.z));
			double angle = Math.atan2(home.z - orbitCentre.z, home.x - orbitCentre.x) + tickCount * 0.02;
			double bob = Math.sin(tickCount * 0.1) * 1.5;
			setPos(orbitCentre.x + Math.cos(angle) * orbitRadius, home.y + bob, orbitCentre.z + Math.sin(angle) * orbitRadius);
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount)
	{
		if (level().isClientSide || isRemoved())
		{
			return false;
		}
		if (source.getDirectEntity() instanceof SandProjectile projectile && projectile.getOwner() instanceof ServerPlayer shooter && shooter.getUUID().equals(owner))
		{
			DiemTrials.onTargetHit(shooter, this);
			discard();
			return true;
		}
		return false;
	}

	@Override
	public boolean isPickable()
	{
		return !isRemoved();
	}

	@Override
	public boolean isPushable()
	{
		return false;
	}

	@Override
	public boolean shouldBeSaved()
	{
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag)
	{
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag)
	{
	}
}
