package leaf.cosmere.entities.spren;

import leaf.cosmere.registry.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.*;
import net.minecraft.world.*;

import javax.annotation.*;
import java.util.*;

public abstract class ASprenEntity extends AnimalEntity implements IFlyingAnimal
{
	protected ASprenEntity(EntityType<? extends AnimalEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	public CreatureAttribute getCreatureAttribute()
	{
		return AttributesRegistry.SPREN;
	}


	public class WanderGoal extends Goal
	{
		WanderGoal()
		{
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean shouldExecute()
		{
			return ASprenEntity.this.navigator.noPath() && ASprenEntity.this.rand.nextInt(10) == 0;
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean shouldContinueExecuting()
		{
			return ASprenEntity.this.navigator.hasPath();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting()
		{
			Vector3d vector3d = this.getRandomLocation();
			if (vector3d != null)
			{
				ASprenEntity.this.navigator.setPath(ASprenEntity.this.navigator.getPathToPos(new BlockPos(vector3d), 1), 1.0D);
			}

		}

		@Nullable
		private Vector3d getRandomLocation()
		{
			Vector3d vector3d = ASprenEntity.this.getLook(0.0F);

			Vector3d vector3d2 = RandomPositionGenerator.findAirTarget(ASprenEntity.this, 8, 7, vector3d, ((float) Math.PI / 2F), 2, 1);
			return vector3d2 != null ? vector3d2 : RandomPositionGenerator.findGroundTarget(ASprenEntity.this, 8, 4, -2, vector3d, (double) ((float) Math.PI / 2F));
		}
	}

	protected abstract class PassiveGoal extends Goal
	{
		public PassiveGoal()
		{

		}

		public abstract boolean canSprenStart();

		public abstract boolean canSprenContinue();

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean shouldExecute()
		{
			return this.canSprenStart();
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean shouldContinueExecuting()
		{
			return this.canSprenContinue();
		}
	}
}
