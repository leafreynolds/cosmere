/*
 * File updated ~ 28 - 3 - 2026 ~ Leaf
 */

package leaf.cosmere.tools.common.items;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;

public class THoeItem extends HoeItem implements IHasMetalType
{
	Metals.MetalType metalType;

	public THoeItem(Metals.MetalType metalType, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties)
	{
		super(metalType, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
		this.metalType = metalType;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return metalType;
	}

	@Override
	public int getEnchantmentValue(ItemStack stack)
	{
		return getEnchantmentValue();
	}

	@Override
	public int getEnchantmentValue()
	{
		return metalType.getEnchantmentValue();
	}

	@Override
	public boolean isEnchantable(ItemStack pStack)
	{
		return true;
	}

	@Override
	public boolean isValidRepairItem(ItemStack pStack, ItemStack pRepairCandidate)
	{
		return metalType.getRepairIngredient().test(pRepairCandidate);
	}
}
