/*
 * File updated ~ 23 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common.items;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import net.minecraft.world.item.SwordItem;

public class TSwordItem extends SwordItem implements IHasMetalType
{
	Metals.MetalType metalType;

	public TSwordItem(Metals.MetalType metalType, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties)
	{
		super(metalType, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
		this.metalType = metalType;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return metalType;
	}
}
