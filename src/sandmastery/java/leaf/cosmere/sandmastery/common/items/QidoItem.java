/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.items;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.items.ChargeableItemBase;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class QidoItem extends ChargeableItemBase
{
	public QidoItem()
	{
		super(PropTypes.Items.ONE.get());
	}

	/**
	 * @param fillPercent ranges from 0 to 1
	 * @return ItemStack of a qido with specified fill percentage
	 */
	public ItemStack getChargedQido(float fillPercent)
	{
		ItemStack qido = new ItemStack(this);
		setCharge(qido, Math.round(fillPercent * SandmasteryConfigs.SERVER.QIDO_MAX_FILL.get()));
		return qido;
	}

	@Override
	public int getMaxCharge(ItemStack itemStack)
	{
		return SandmasteryConfigs.SERVER.QIDO_MAX_FILL.get();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

		if (pPlayer.isCrouching())
		{
			BlockPos pos = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.WATER).getBlockPos();
			BlockState state = pLevel.getBlockState(pos);
			if (state.is(Blocks.WATER))
			{
				pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
				setCharge(itemStack, getMaxCharge(itemStack));
			}
		}
		else
		{
			final int liquid = getCharge(itemStack);
			if (liquid <= 0)
			{
				InteractionResultHolder.pass(itemStack);
			}
			SpiritwebCapability.get(pPlayer).ifPresent(spiritweb ->
			{
				SpiritwebCapability data = (SpiritwebCapability) spiritweb;

				if (data.getLiving().getAttribute(SandmasteryAttributes.RIBBONS.get()).getBaseValue() < 1)
				{
					return;
				}

				SandmasterySpiritwebSubmodule sb = SandmasterySpiritwebSubmodule.get(data);

				int playerHydration = sb.getHydrationLevel();
				final int maxPlayerHydration = SandmasteryConfigs.SERVER.MAX_HYDRATION.get();

				pPlayer.startUsingItem(pUsedHand);
			});
		}

		return InteractionResultHolder.consume(itemStack);
	}

	@Override
	public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration)
	{
		if (!pLivingEntity.isCrouching())
		{
			int availableWater = getCharge(pStack);
			if (availableWater == 0)
			{
				pLivingEntity.stopUsingItem();
				return;
			}
			SpiritwebCapability.get(pLivingEntity).ifPresent(spiritweb ->
			{
				SpiritwebCapability data = (SpiritwebCapability) spiritweb;
				SandmasterySpiritwebSubmodule sb = (SandmasterySpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);

				int increasePerTick = Math.min(50, availableWater);
				int playerHydration = sb.getHydrationLevel();
				final int maxPlayerHydration = SandmasteryConfigs.SERVER.MAX_HYDRATION.get();

				if (playerHydration == maxPlayerHydration)
				{
					return;
				}
				if ((increasePerTick + playerHydration) > maxPlayerHydration)
				{
					increasePerTick = maxPlayerHydration - playerHydration;
				}

				sb.adjustHydration(increasePerTick);
				setCharge(pStack, availableWater - increasePerTick);
			});
		}
		super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
	}

	@Nonnull
	@Override
	public UseAnim getUseAnimation(ItemStack pStack)
	{
		return UseAnim.DRINK;
	}

	@Override
	public boolean isFoil(@NotNull ItemStack stack)
	{
		return stack.isEnchanted();
	}

	@Override
	public int getUseDuration(ItemStack pStack)
	{
		return 16000;
	}

}
