/*
 * File updated ~ 14 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.entity.spren;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Honorspren extends Allay
{
	public Honorspren(EntityType<? extends Allay> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}

	@Override
	public boolean canBeAffected(MobEffectInstance pEffectInstance)
	{
		return false;
	}

	@Override
	public boolean canBeHitByProjectile()
	{
		return false;
	}

	@Override
	public boolean canBeSeenByAnyone()
	{
		//TODO: make this only visible to the player it is bond to?
		return super.canBeSeenByAnyone();
	}

	@Override
	public boolean canCollideWith(Entity pEntity)
	{
		return false;
	}

	@Override
	public boolean canBeSeenAsEnemy()
	{
		return false;
	}

	@Override
	public boolean canBeLeashed(Player pPlayer)
	{
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		//todo: make this only collide with the player it is bond to?
		return false;
	}

	@Override
	public boolean canTakeItem(ItemStack pItemstack)
	{
		//return super.canTakeItem(pItemstack);
		//TODO: make this only take items from the player it is bond to?
		//todo make it only able to take things once bond has progressed?
		return false;
	}

	@Override
	public boolean hurt(DamageSource pSource, float pAmount)
	{
		//todo make this only damage from a very specific type of damage type, you know the one.
		return false;
	}
}
