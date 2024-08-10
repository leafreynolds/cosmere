/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.items;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.common.items.BaseItem;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.blocks.TaldainBlackSandLayerBlock;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class JarItem extends BaseItem
{
	public JarItem()
	{
		super(PropTypes.Items.ONE.get());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		ItemStack usedItem = pPlayer.getItemInHand(pUsedHand);
		if (pLevel.isClientSide())
		{
			return InteractionResultHolder.pass(usedItem);
		}
		BlockPos pos = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.NONE).getBlockPos();
		BlockState state = pLevel.getBlockState(pos);
		Inventory inv = pPlayer.getInventory();
		boolean fill = false;
		boolean charged = false;
		if (state.is(SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.getBlock()))
		{
			int layers = state.getValue(TaldainBlackSandLayerBlock.LAYERS);
			if (layers > 1)
			{
				pLevel.setBlockAndUpdate(pos, state.setValue(TaldainBlackSandLayerBlock.LAYERS, layers - 1));
			}
			else
			{
				pLevel.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}

			charged = false;
			fill = true;
		}
		else if (state.is(SandmasteryBlocks.TALDAIN_BLACK_SAND.getBlock()))
		{
			pLevel.setBlockAndUpdate(pos,
					SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.getBlock()
							.defaultBlockState()
							.setValue(TaldainBlackSandLayerBlock.LAYERS, 7)
			);
			charged = false;
			fill = true;
		}
		if (state.is(SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.getBlock()))
		{
			int layers = state.getValue(TaldainBlackSandLayerBlock.LAYERS);
			if (layers > 1)
			{
				pLevel.setBlockAndUpdate(pos, state.setValue(TaldainBlackSandLayerBlock.LAYERS, layers - 1));
			}
			else
			{
				pLevel.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}

			charged = true;
			fill = true;
		}
		else if (state.is(SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.getBlock()))
		{
			pLevel.setBlockAndUpdate(pos,
					SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.getBlock()
							.defaultBlockState()
							.setValue(TaldainBlackSandLayerBlock.LAYERS, 7)
			);
			charged = true;
			fill = true;
		}

		ItemStack item = new ItemStack(SandmasteryItems.SAND_JAR_ITEM);
		if (charged)
		{
			StackNBTHelper.setInt(item, Constants.NBT.CHARGE_LEVEL, 100);
		}
		else
		{
			StackNBTHelper.setInt(item, Constants.NBT.CHARGE_LEVEL, 0);
		}

		if (fill)
		{
			inv.setItem(pUsedHand == InteractionHand.MAIN_HAND ? inv.selected : -106, item);
		}

		return InteractionResultHolder.consume(usedItem);
	}
}
