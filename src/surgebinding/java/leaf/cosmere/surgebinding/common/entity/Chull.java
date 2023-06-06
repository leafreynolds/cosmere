/*
 * File updated ~ 7 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

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
		return pOtherAnimal != this && pOtherAnimal instanceof Chull;
	}

	@Override
	public int getMaxSpawnClusterSize()
	{
		return 4;
	}
}
