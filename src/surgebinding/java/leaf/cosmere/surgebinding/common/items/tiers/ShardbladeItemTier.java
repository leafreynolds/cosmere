/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items.tiers;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.crafting.Ingredient;

public class ShardbladeItemTier implements Tier
{
	float attackDamage;

	public ShardbladeItemTier(float attackDamage)
	{
		this.attackDamage = attackDamage;
	}

	@Override
	public int getUses()
	{
		return -1;
	}

	@Override
	public float getSpeed()
	{
		return 8.0F;
	}

	@Override
	public float getAttackDamageBonus()
	{
		return attackDamage;
	}

	@Override
	public int getEnchantmentValue()
	{
		return 30;
	}

	@Override
	public TagKey<Block> getIncorrectBlocksForDrops()
	{
		return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
	}

	@Override
	public Ingredient getRepairIngredient()
	{
		return Ingredient.EMPTY;
	}
}
