package leaf.cosmere.sandmastery.common.items;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.common.items.ChargeableItemBase;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.blocks.SandJarBlock;
import leaf.cosmere.sandmastery.common.blocks.TaldainSandLayerBlock;
import leaf.cosmere.sandmastery.common.itemgroups.SandmasteryItemGroups;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import leaf.cosmere.sandmastery.common.utils.SandmasteryConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class SandJarItem extends ChargeableItemBase
{
	public SandJarItem()
	{
		super(PropTypes.Items.ONE.get().tab(SandmasteryItemGroups.ITEMS));
	}

	@Override
	public int getMaxCharge(ItemStack itemStack)
	{
		return Mth.floor(SandmasteryConstants.CHARGE_PER_SAND_LAYER);
	}

	@Override
	public boolean isBarVisible(@NotNull ItemStack stack)
	{
		return true;
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected)
	{
		MiscHelper.chargeItemFromInvestiture(pStack, pLevel, pEntity, getMaxCharge(pStack));
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> stacks)
	{
		if (allowedIn(tab))
		{
			stacks.add(new ItemStack(this));

			ItemStack halfPower = new ItemStack(this);
			setCharge(halfPower, getMaxCharge(halfPower) / 2);
			stacks.add(halfPower);

			ItemStack fullPower = new ItemStack(this);
			setCharge(fullPower, getMaxCharge(fullPower));
			stacks.add(fullPower);
		}
	}

	@Override
	public boolean isFoil(@NotNull ItemStack stack)
	{
		return false;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack itemStack)
	{
		ItemStack stack = new ItemStack(SandmasteryItems.JAR_ITEM);
		stack.setCount(1);
		return stack;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		ItemStack usedItem = pPlayer.getItemInHand(pUsedHand);
		if (pLevel.isClientSide()) return InteractionResultHolder.pass(usedItem);
		BlockHitResult res = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.NONE);
		BlockPos pos = res.getBlockPos();
		Direction dir = res.getDirection();
		BlockState state = pLevel.getBlockState(pos);
		Inventory inv = pPlayer.getInventory();

		boolean wasCharged = StackNBTHelper.getInt(usedItem, Constants.NBT.CHARGE_LEVEL, 0) > 0;
		if (pPlayer.isCrouching())
		{
			inv.setItem(pUsedHand == InteractionHand.MAIN_HAND ? inv.selected : -106, ItemStack.EMPTY);
			BlockPos pos2 = pos.offset(dir.getNormal());
			pLevel.setBlockAndUpdate(pos2,
					SandmasteryBlocksRegistry.SAND_JAR_BLOCK.getBlock()
							.defaultBlockState()
							.setValue(SandJarBlock.INVESTITURE, StackNBTHelper.getInt(usedItem, Constants.NBT.CHARGE_LEVEL, 0))
			);
		}
		else
		{
			inv.setItem(pUsedHand == InteractionHand.MAIN_HAND ? inv.selected : -106, new ItemStack(SandmasteryItems.JAR_ITEM));
			if (state.is(SandmasteryBlocksRegistry.TALDAIN_SAND_LAYER.getBlock()) && state.getValue(TaldainSandLayerBlock.LAYERS) < 8)
			{
				int layers = state.getValue(TaldainSandLayerBlock.LAYERS);
				pLevel.setBlockAndUpdate(pos,
						state
								.setValue(TaldainSandLayerBlock.LAYERS, layers + 1)
								.setValue(TaldainSandLayerBlock.INVESTED, wasCharged)
				);
			}
			else
			{
				BlockPos pos2 = pos.offset(dir.getNormal());
				pLevel.setBlockAndUpdate(pos2,
						SandmasteryBlocksRegistry.TALDAIN_SAND_LAYER.getBlock()
								.defaultBlockState()
								.setValue(TaldainSandLayerBlock.LAYERS, 1)
								.setValue(TaldainSandLayerBlock.INVESTED, wasCharged)
				);
			}
		}
		return InteractionResultHolder.consume(usedItem);
	}

}
