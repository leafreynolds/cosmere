package leaf.cosmere.entities.spren;

import com.google.common.collect.*;
import leaf.cosmere.registry.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.ai.controller.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.*;
import net.minecraft.network.datasync.*;
import net.minecraft.particles.*;
import net.minecraft.pathfinding.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.*;
import net.minecraft.world.*;
import net.minecraft.world.server.*;
import net.minecraftforge.api.distmarker.*;

import javax.annotation.*;
import java.util.*;

public class SprenFlameEntity extends ASprenEntity
{
	private static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(SprenFlameEntity.class, DataSerializers.BYTE);

	private float rollAmount;
	private float rollAmountO;

	private int stayOutOfFireCountdown;

	private int remainingCooldownBeforeLocatingNewFire = 0;
	private int remainingCooldownBeforeLocatingNewFlower = 0;

	@Nullable
	private BlockPos firePos = null;

	private MoveToFireGoal moveToFireGoal;

	private int underWaterTicks;

	public SprenFlameEntity(EntityType<? extends SprenFlameEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.moveControl = new FlyingMovementController(this, 20, true);
		this.lookControl = new LookController(this);
		this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
		this.setPathfindingMalus(PathNodeType.WATER_BORDER, 16.0F);
		this.setPathfindingMalus(PathNodeType.COCOA, -1.0F);
		this.setPathfindingMalus(PathNodeType.FENCE, -1.0F);
	}

	@Override
	public boolean fireImmune()
	{
		return true;
	}

	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(DATA_FLAGS_ID, (byte) 0);
	}

	public static AttributeModifierMap.MutableAttribute prepareAttributes()
	{
		return MobEntity.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.0D)
				.add(Attributes.FLYING_SPEED, 0.6F)
				.add(Attributes.MOVEMENT_SPEED, 0.3F)
				.add(Attributes.ATTACK_DAMAGE, 2.0D)
				.add(Attributes.FOLLOW_RANGE, 48.0D);
	}

	public float getWalkTargetValue(BlockPos pos, IWorldReader worldIn)
	{
		return worldIn.getBlockState(pos).isAir() ? 10.0F : 0.0F;
	}

	protected void registerGoals()
	{
		this.goalSelector.addGoal(1, new SprenFlameEntity.EnterFireGoal());
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.TORCH, Items.SOUL_TORCH), false));
		this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));

		this.goalSelector.addGoal(5, new SprenFlameEntity.UpdateFireGoal());

		this.moveToFireGoal = new MoveToFireGoal();
		this.goalSelector.addGoal(5, this.moveToFireGoal);

		this.goalSelector.addGoal(8, new SprenFlameEntity.WanderGoal());

		this.goalSelector.addGoal(9, new SwimGoal(this));
	}

	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);

		if (this.hasFire())
		{
			compound.put("FirePos", NBTUtil.writeBlockPos(this.getFirePos()));
		}

		compound.putInt("CannotEnterFireTicks", this.stayOutOfFireCountdown);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readAdditionalSaveData(CompoundNBT compound)
	{
		this.firePos = null;
		if (compound.contains("FirePos"))
		{
			this.firePos = NBTUtil.readBlockPos(compound.getCompound("FirePos"));
		}

		this.stayOutOfFireCountdown = compound.getInt("CannotEnterFireTicks");
	}


	/**
	 * Called to update the entity's position/logic.
	 */
	public void tick()
	{
		super.tick();
		if (this.random.nextFloat() < 0.05F)
		{
			for (int i = 0; i < this.random.nextInt(2) + 1; ++i)
			{
				this.addParticle(this.level,
						this.getX() - (double) 0.3F,
						this.getX() + (double) 0.3F,
						this.getZ() - (double) 0.3F,
						this.getZ() + (double) 0.3F,
						this.getY(0.5D));
			}
		}

		this.updateBodyPitch();
	}

	private void addParticle(World worldIn, double p_226397_2_, double p_226397_4_, double p_226397_6_, double p_226397_8_, double posY)
	{
		worldIn.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, MathHelper.lerp(worldIn.random.nextDouble(), p_226397_2_, p_226397_4_), posY, MathHelper.lerp(worldIn.random.nextDouble(), p_226397_6_, p_226397_8_), 0.0D, 0.0D, 0.0D);
	}

	private void startMovingTo(BlockPos pos)
	{
		Vector3d vector3d = Vector3d.atBottomCenterOf(pos);
		int i = 0;
		BlockPos blockpos = this.blockPosition();
		int j = (int) vector3d.y - blockpos.getY();
		if (j > 2)
		{
			i = 4;
		}
		else if (j < -2)
		{
			i = -4;
		}

		int k = 6;
		int l = 8;
		int i1 = blockpos.distManhattan(pos);
		if (i1 < 15)
		{
			k = i1 / 2;
			l = i1 / 2;
		}

		Vector3d vector3d1 = RandomPositionGenerator.getAirPosTowards(this, k, l, i, vector3d, (float) Math.PI / 10F);
		if (vector3d1 != null)
		{
			this.navigation.setMaxVisitedNodesMultiplier(0.5F);
			this.navigation.moveTo(vector3d1.x, vector3d1.y, vector3d1.z, 1.0D);
		}
	}


	private boolean canEnterFire()
	{
		return (this.stayOutOfFireCountdown <= 0) || (this.level.isRaining() || this.level.isNight());
	}

	@OnlyIn(Dist.CLIENT)
	public float getBodyPitch(float p_226455_1_)
	{
		return MathHelper.lerp(p_226455_1_, this.rollAmountO, this.rollAmount);
	}

	private void updateBodyPitch()
	{
		this.rollAmountO = this.rollAmount;
		this.rollAmount = Math.max(0.0F, this.rollAmount - 0.24F);
	}

	protected void customServerAiStep()
	{
		if (this.isInWaterOrBubble())
		{
			++this.underWaterTicks;
		}
		else
		{
			this.underWaterTicks = 0;
		}

		if (this.underWaterTicks > 20)
		{
			this.hurt(DamageSource.DROWN, 10.0F);
		}

	}

	public boolean hasFire()
	{
		return this.firePos != null;
	}

	@Nullable
	public BlockPos getFirePos()
	{
		return this.firePos;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	public void aiStep()
	{
		super.aiStep();
		if (!this.level.isClientSide)
		{
			if (this.stayOutOfFireCountdown > 0)
			{
				--this.stayOutOfFireCountdown;
			}

			if (this.remainingCooldownBeforeLocatingNewFire > 0)
			{
				--this.remainingCooldownBeforeLocatingNewFire;
			}

			if (this.remainingCooldownBeforeLocatingNewFlower > 0)
			{
				--this.remainingCooldownBeforeLocatingNewFlower;
			}

			if (this.tickCount % 20 == 0 && !this.isFireValid())
			{
				this.firePos = null;
			}
		}

	}

	private boolean isFireValid()
	{
		if (!this.hasFire())
		{
			return false;
		}
		else
		{
			return this.level.getBlockState(this.firePos).getBlock() == Blocks.FIRE;
		}
	}

	private boolean isTooFar(BlockPos pos)
	{
		return !this.isWithinDistance(pos, 32);
	}

	/**
	 * Returns new PathNavigateGround instance
	 */
	protected PathNavigator createNavigation(World worldIn)
	{
		FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn)
		{
			public boolean isStableDestination(BlockPos pos)
			{
				return !this.level.getBlockState(pos.below()).isAir();
			}

			public void tick()
			{
				super.tick();
			}
		};
		flyingpathnavigator.setCanOpenDoors(false);
		flyingpathnavigator.setCanFloat(false);
		flyingpathnavigator.setCanPassDoors(true);
		return flyingpathnavigator;
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
	 * the animal type)
	 */
	public boolean isFood(ItemStack stack)
	{
		return stack.getItem().is(ItemTags.FLOWERS);
	}

	protected void playStepSound(BlockPos pos, BlockState blockIn)
	{
	}

	protected SoundEvent getAmbientSound()
	{
		return null;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.BEE_HURT;
	}

	protected SoundEvent getDeathSound()
	{
		return SoundEvents.BEE_DEATH;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume()
	{
		return 0.4F;
	}

	public SprenFlameEntity getBreedOffspring(ServerWorld world, AgeableEntity mate)
	{
		return EntityRegistry.SPREN_FIRE.get().create(world);
	}

	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
	{
		return this.isBaby() ? sizeIn.height * 0.5F : sizeIn.height * 0.5F;
	}

	public boolean causeFallDamage(float distance, float damageMultiplier)
	{
		return false;
	}

	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos)
	{
	}

	protected boolean makeFlySound()
	{
		return true;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean hurt(DamageSource source, float amount)
	{
		if (this.isInvulnerableTo(source))
		{
			return false;
		}
		else
		{
			return super.hurt(source, amount);
		}
	}

	public CreatureAttribute getMobType()
	{
		return AttributesRegistry.SPREN;
	}

	protected void jumpInLiquid(ITag<Fluid> fluidTag)
	{
		this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
	}

	@OnlyIn(Dist.CLIENT)
	public Vector3d getLeashOffset()
	{
		return new Vector3d(0.0D, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.2F);
	}

	private boolean isWithinDistance(BlockPos pos, int distance)
	{
		try
		{
			SprenFlameEntity sprenFlameEntity = this;
			BlockPos position = sprenFlameEntity.blockPosition();
			return pos != null && pos.closerThan(position, distance);

		}
		catch (Exception e)
		{
			return false;
		}
	}

	class EnterFireGoal extends SprenFlameEntity.PassiveGoal
	{
		protected EnterFireGoal()
		{
		}

		public boolean canSprenStart()
		{
			if (SprenFlameEntity.this.hasFire() && SprenFlameEntity.this.canEnterFire() && SprenFlameEntity.this.firePos.closerThan(SprenFlameEntity.this.position(), 2.0D))
			{
				return true;
			}

			return false;
		}

		public boolean canSprenContinue()
		{
			return false;
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start()
		{
			if (isFireValid())
			{
				SprenFlameEntity.this.remove();
			}
		}
	}

	public class MoveToFireGoal extends SprenFlameEntity.PassiveGoal
	{
		private int ticks = SprenFlameEntity.this.level.random.nextInt(10);
		private List<BlockPos> possibleFires = Lists.newArrayList();
		@Nullable
		private Path path = null;
		private int ticksStuck;

		MoveToFireGoal()
		{
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canSprenStart()
		{
			return SprenFlameEntity.this.firePos != null && !SprenFlameEntity.this.hasRestriction() && SprenFlameEntity.this.canEnterFire() && !this.isCloseEnough(SprenFlameEntity.this.firePos);
		}

		public boolean canSprenContinue()
		{
			return this.canSprenStart();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start()
		{
			this.ticks = 0;
			this.ticksStuck = 0;
			super.start();
		}

		/**
		 * Reset the task's internal state. Called when this task is interrupted by another one
		 */
		public void stop()
		{
			this.ticks = 0;
			this.ticksStuck = 0;
			SprenFlameEntity.this.navigation.stop();
			SprenFlameEntity.this.navigation.resetMaxVisitedNodesMultiplier();
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick()
		{
			if (SprenFlameEntity.this.firePos != null)
			{
				++this.ticks;
				if (this.ticks > 600)
				{
					this.makeChosenFirePossibleFire();
				}
				else if (!SprenFlameEntity.this.navigation.isInProgress())
				{
					if (!SprenFlameEntity.this.isWithinDistance(SprenFlameEntity.this.firePos, 16))
					{
						if (SprenFlameEntity.this.isTooFar(SprenFlameEntity.this.firePos))
						{
							this.reset();
						}
						else
						{
							SprenFlameEntity.this.startMovingTo(SprenFlameEntity.this.firePos);
						}
					}
					else
					{
						boolean flag = this.startMovingToFar(SprenFlameEntity.this.firePos);
						if (!flag)
						{
							this.makeChosenFirePossibleFire();
						}
						else if (this.path != null && SprenFlameEntity.this.navigation.getPath().sameAs(this.path))
						{
							++this.ticksStuck;
							if (this.ticksStuck > 60)
							{
								this.reset();
								this.ticksStuck = 0;
							}
						}
						else
						{
							this.path = SprenFlameEntity.this.navigation.getPath();
						}

					}
				}
			}
		}

		private boolean startMovingToFar(BlockPos pos)
		{
			SprenFlameEntity.this.navigation.setMaxVisitedNodesMultiplier(10.0F);
			SprenFlameEntity.this.navigation.moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
			return SprenFlameEntity.this.navigation.getPath() != null && SprenFlameEntity.this.navigation.getPath().canReach();
		}

		private boolean isPossibleFire(BlockPos pos)
		{
			return this.possibleFires.contains(pos);
		}

		private void addPossibleFires(BlockPos pos)
		{
			this.possibleFires.add(pos);

			while (this.possibleFires.size() > 3)
			{
				this.possibleFires.remove(0);
			}

		}

		private void clearPossibleFires()
		{
			this.possibleFires.clear();
		}

		private void makeChosenFirePossibleFire()
		{
			if (SprenFlameEntity.this.firePos != null)
			{
				this.addPossibleFires(SprenFlameEntity.this.firePos);
			}

			this.reset();
		}

		private void reset()
		{
			SprenFlameEntity.this.firePos = null;
			SprenFlameEntity.this.remainingCooldownBeforeLocatingNewFire = 200;
		}

		private boolean isCloseEnough(BlockPos pos)
		{
			if (SprenFlameEntity.this.isWithinDistance(pos, 2))
			{
				return true;
			}
			else
			{
				Path path = SprenFlameEntity.this.navigation.getPath();
				return path != null && path.getTarget().equals(pos) && path.canReach() && path.isDone();
			}
		}
	}

	class UpdateFireGoal extends SprenFlameEntity.PassiveGoal
	{
		private UpdateFireGoal()
		{
		}

		public boolean canSprenStart()
		{
			return SprenFlameEntity.this.remainingCooldownBeforeLocatingNewFire == 0 && !SprenFlameEntity.this.hasFire() && SprenFlameEntity.this.canEnterFire();
		}

		public boolean canSprenContinue()
		{
			return false;
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start()
		{
			SprenFlameEntity.this.remainingCooldownBeforeLocatingNewFire = 200;
			List<BlockPos> list = this.getNearbyFreeFires();
			if (!list.isEmpty())
			{
				for (BlockPos blockpos : list)
				{
					if (!SprenFlameEntity.this.moveToFireGoal.isPossibleFire(blockpos))
					{
						SprenFlameEntity.this.firePos = blockpos;
						return;
					}
				}

				SprenFlameEntity.this.moveToFireGoal.clearPossibleFires();
				SprenFlameEntity.this.firePos = list.get(0);
			}
		}

		private List<BlockPos> getNearbyFreeFires()
		{
			List<BlockPos> found = new ArrayList<>();
			BlockPos.withinManhattanStream(SprenFlameEntity.this.blockPosition(), 15, 15, 15)
					.filter(blockPos ->
					{
						Block block = SprenFlameEntity.this.level.getBlockState(blockPos).getBlock();

						if (block instanceof FireBlock)
						{
							return true;
						}

						return false;
					})
					.forEach(found::add);
			return found;
		}
	}

}

