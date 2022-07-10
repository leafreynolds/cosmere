/*
 * File created ~ 10 - 7 - 2022 ~ Leaf
 */

package leaf.cosmere.items;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class ShardbladeItemTier implements Tier
{
	float attackDamage;

	public ShardbladeItemTier(float attackDamage) {
		this.attackDamage = attackDamage;
	}

	@Override
	public int getUses() {
		return -1;
	}

	@Override
	public float getSpeed() {
		return 8.0F;
	}

	@Override
	public float getAttackDamageBonus() {
		return attackDamage;
	}

	@Override
	public int getLevel() {
		return 3;
	}

	@Override
	public int getEnchantmentValue() {
		return 30;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.EMPTY;
	}
}
