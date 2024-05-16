/*
 * File updated ~ 18 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.common.items;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.CosmereDamageTypesRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class MetalNuggetItem extends MetalItem
{
	public MetalNuggetItem(Metals.MetalType metalType)
	{
		super(metalType);
	}


	@Nonnull
	@Override
	public UseAnim getUseAnimation(ItemStack stack)
	{
		return UseAnim.EAT;
	}


	@Override
	public int getUseDuration(ItemStack stack)
	{
		//be annoying enough that people prefer metal vials
		return 16;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (player.canEat(true))
		{
			player.startUsingItem(hand);
			return InteractionResultHolder.consume(stack);
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level pLevel, LivingEntity pLivingEntity)
	{
		if (pLevel.isClientSide)
		{
			return itemstack;
		}

		if (pLivingEntity instanceof Player player && !player.isCreative())
		{
			itemstack.shrink(1);
		}

		pLivingEntity.hurt(CosmereDamageTypesRegistry.damageSource(pLevel, CosmereDamageTypesRegistry.EAT_METAL),  1);

		return itemstack;
	}

}
