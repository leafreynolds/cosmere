/*
 * File updated ~ 13 - 5 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.entity;

import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

public class Cryptic extends TamableAnimal
{
	private static final Set<Item> TAMING_INGREDIENTS;
	private SitWhenOrderedToGoal sitGoal;

	public Cryptic(EntityType<? extends TamableAnimal> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
		this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
	}

	public static AttributeSupplier.Builder createAttributes()
	{
		return Monster.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, (double) 0.20000000298023224D)
				.add(Attributes.FOLLOW_RANGE, 12.0D)
				.add(Attributes.MAX_HEALTH, 24.0D)
				.add(Attributes.JUMP_STRENGTH, 0.1D)
				.add(Attributes.ATTACK_DAMAGE, 5.0D);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag)
	{
		if (pSpawnData == null)
		{
			pSpawnData = new AgeableMob.AgeableMobGroupData(false);
		}
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

	@Override
	protected void registerGoals()
	{
		this.sitGoal = new SitWhenOrderedToGoal(this);
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(2, this.sitGoal);
		this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
//        this.goalSelector.addGoal(2, new FlyOntoTreeGoal(this, 1.0D)); // This goal seems to be missing from 1.19
//        this.goalSelector.addGoal(3, new LandOnOwnersShoulderGoal(this));
		this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));
	}

	@Override
	protected PathNavigation createNavigation(Level pLevel)
	{
		return new WallClimberNavigation(this, pLevel);
	}

	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand)
	{
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
//        System.out.println("Cryptic#mobInteract start itemStack:" + itemstack + " isTamed:" + this.isTame());
		if (!this.isTame() && TAMING_INGREDIENTS.contains(itemstack.getItem()))
		{
			if (!pPlayer.getAbilities().instabuild)
			{
				itemstack.shrink(1);
			}

			if (!this.isSilent())
			{
				this.level().playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.PARROT_EAT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
			}

			if (!this.level().isClientSide)
			{
				if (this.random.nextInt(10) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, pPlayer))
				{
//                    System.out.println("Cryptic#mobInteract isClient, setOwner:" + pPlayer);
					this.tame(pPlayer);
					this.level().broadcastEntityEvent(this, (byte) 7);
				}
				else
				{
//                    System.out.println("Cryptic#mobInteract isClient, tame FAIL");
					this.level().broadcastEntityEvent(this, (byte) 6);
				}
			}

			return InteractionResult.sidedSuccess(this.level().isClientSide);
		}
		else if (this.isTame() && this.isOwnedBy(pPlayer))
		{
			if (!this.level().isClientSide)
			{
//                System.out.println("Cryptic#mobInteract setOrderedToSit");
				this.setOrderedToSit(!this.isOrderedToSit());
			}

			return InteractionResult.sidedSuccess(this.level().isClientSide);
		}
		else
		{
			return super.mobInteract(pPlayer, pHand);
		}
	}

	@Override
	public boolean isFood(ItemStack pStack)
	{
		return false;
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent)
	{
		return null;
	}

	@Override
	public boolean canMate(@NotNull Animal pOtherAnimal)
	{
		return false;
	}

	static
	{
		TAMING_INGREDIENTS = SurgebindingItems.GEMSTONE_BROAMS.values().stream()
				.map(registryItem -> registryItem.asItem()).collect(Collectors.toSet());
	}
}
