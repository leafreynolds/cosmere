/*
 * File updated ~ 19 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.entity;

import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public class Koloss extends AbstractIllager
{
	public Koloss(EntityType<? extends Koloss> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}

	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(2, new AbstractIllager.RaiderOpenDoorGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
		this.goalSelector.addGoal(3, new Raider.HoldGroundAttackGoal(this, 10.0F));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
		this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
	}

	@Override
	public boolean isBaby()
	{
		return false;
	}

	@Override
	public void applyRaidBuffs(int pWave, boolean pUnusedFalse)
	{

	}

	@Override
	public SoundEvent getCelebrateSound()
	{
		return SoundEvents.RAVAGER_CELEBRATE;
	}


	public static AttributeSupplier.Builder createAttributes()
	{
		//todo, make different based on size of koloss
		return Monster.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, (double) 0.35F)
				.add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.MAX_HEALTH, 100.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.75d)
				.add(Attributes.ARMOR, 4.0d)
				.add(Attributes.ARMOR_TOUGHNESS, 1.0d)
				.add(Attributes.LUCK, -1.0d)
				.add(Attributes.ATTACK_DAMAGE, 12.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.5D)
				.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.1D);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag)
	{
		SpawnGroupData spawngroupdata = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
		((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
		RandomSource randomsource = pLevel.getRandom();
		this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
		this.populateDefaultEquipmentEnchantments(randomsource, pDifficulty);
		return spawngroupdata;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty)
	{
		final Item value = HemalurgyItems.KOLOSS_SWORD.get();
		if (value != null)
		{
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(value));
		}
	}
}
