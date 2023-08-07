/*
 * File updated ~ 13 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.items;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.items.ChargeableItemBase;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import leaf.cosmere.sandmastery.common.itemgroups.SandmasteryItemGroups;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class QidoItem extends ChargeableItemBase
{
	public QidoItem()
	{
		super(PropTypes.Items.ONE.get().tab(SandmasteryItemGroups.ITEMS));
	}

	@Override
	public int getMaxCharge(ItemStack itemStack)
	{
		return Mth.floor(1000 * getMaxChargeModifier());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
		if (pLevel.isClientSide)
		{
			return InteractionResultHolder.pass(itemStack);
		}

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
			SpiritwebCapability.get(pPlayer).ifPresent(spiritweb ->
			{
				SpiritwebCapability data = (SpiritwebCapability) spiritweb;

				boolean isMaster = SandmasteryManifestations.SANDMASTERY_POWERS.values().stream().anyMatch((manifestation -> spiritweb.hasManifestation(manifestation.getManifestation())));

				if (!isMaster)
				{
					return;
				}

				final int liquid = getCharge(itemStack);

				SandmasterySpiritwebSubmodule sb = (SandmasterySpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);

				int playerHydration = sb.getHydrationLevel();
				final int maxPlayerHydration = SandmasteryConfigs.SERVER.MAX_HYDRATION.get();

				if (liquid + playerHydration > maxPlayerHydration)
				{
					sb.adjustHydration((maxPlayerHydration - playerHydration), true);
					setCharge(itemStack, ((liquid + playerHydration) - maxPlayerHydration));
				}
				else
				{
					sb.adjustHydration(liquid, true);
					setCharge(itemStack, 0);
				}
			});
		}

		return InteractionResultHolder.consume(itemStack);
	}

	// TODO figure out how to get this to work
//    @Override
//    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
//        int availableWater = getCharge(pStack);
//        if (availableWater == 0) return;
//        SpiritwebCapability.get(pLivingEntity).ifPresent(spiritweb ->
//        {
//            SpiritwebCapability data = (SpiritwebCapability) spiritweb;
//            SandmasterySpiritwebSubmodule sb = (SandmasterySpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);
//
//            int increasePerTick = Math.min(63, availableWater);
//            int playerHydration = sb.getHydrationLevel();
//            final int maxPlayerHydration = sb.MAX_HYDRATION;
//
//            if(playerHydration == maxPlayerHydration) return;
//            if ((increasePerTick + playerHydration) > maxPlayerHydration) increasePerTick = maxPlayerHydration-playerHydration;
//
//            sb.adjustHydration(increasePerTick, true);
//            setCharge(pStack, availableWater - increasePerTick);
//
//            CosmereAPI.logger.info(increasePerTick);
//        });
//        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
//    }

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
		return 16;
	}

}
