package leaf.cosmere.surgebinding.common.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ISummonableItem
{
	boolean canSummonDismiss(LivingEntity player, ItemStack stack);
}
