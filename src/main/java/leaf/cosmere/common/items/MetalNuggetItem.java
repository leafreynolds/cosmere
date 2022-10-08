/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.items;

import leaf.cosmere.api.Metals;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public class MetalNuggetItem extends MetalItem
{
	public static final DamageSource EAT_METAL = (new DamageSource("eat_metal")).bypassArmor().bypassMagic();


	public MetalNuggetItem(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public boolean isEdible()
	{
		return true;
	}

	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
	{
		return ItemUtils.startUsingInstantly(worldIn, playerIn, handIn);
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		//be annoying enough that people prefer metal vials
		return 16;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level pLevel, LivingEntity pLivingEntity)
	{
		if (pLivingEntity instanceof Player player && !player.isCreative())
		{
			itemstack.shrink(1);
		}

		pLivingEntity.hurt(EAT_METAL, 1);

		return itemstack;
	}

}
