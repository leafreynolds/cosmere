/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to Vazkii and Botania!
 * for the easier to understand example of hooking into color tints
 */

package leaf.cosmere.common.eventHandlers;

import leaf.cosmere.api.IHasColour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public final class ColorHandler
{

	public static void init()
	{
		//todo check client side config for if it's wanted


		BlockColors blockColors = Minecraft.getInstance().getBlockColors();
		ItemColors itemColors = Minecraft.getInstance().getItemColors();

		ItemColor itemColorHandler =
				(itemStack, tintIndex) -> tintIndex == 0
				                          ? ((IHasColour) itemStack.getItem()).getColourValue()
				                          : -1;

		ItemColor blockItemColorHandler =
				(itemStack, tintIndex) -> tintIndex == 0
				                          ? Minecraft.getInstance().getBlockColors().getColor(((BlockItem) itemStack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex)
				                          : -1;

		BlockColor cosmereBlockColorHandler =
				(blockState, world, pos, tintIndex) -> tintIndex == 0
				                                       ? ((IHasColour) blockState.getBlock()).getColourValue()
				                                       : -1;


		for (Item item : ForgeRegistries.ITEMS)
		{
			if (item instanceof BlockItem blockItem)
			{
				if (item instanceof IHasColour)
				{
					final Block block = blockItem.getBlock();
					itemColors.register(blockItemColorHandler, block);
					blockColors.register(cosmereBlockColorHandler, block);
				}
			}
			else
			{
				if (item instanceof IHasColour)
				{
					itemColors.register(itemColorHandler, item);
				}
			}
		}
	}
}
