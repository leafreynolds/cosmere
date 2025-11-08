/*
 * File updated ~ 8 - 11 - 2025 ~ Leaf
 */

package leaf.cosmere.common.eventHandlers;

import leaf.cosmere.api.IHasColour;
import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.common.config.CosmereConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public final class ColorHandler
{

	public static void init()
	{
		if (CosmereConfigs.CLIENT_CONFIG.disableItemTinting.get())
		{
			return;
		}

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
			final ResourceLocation registryName = RegistryHelper.get(item);

			final String itemNamespace = registryName.getNamespace();

			if (item instanceof BlockItem blockItem)
			{
				if (!itemNamespace.equals("surgebinding")
						&& !itemNamespace.equals("cosmeretools"))
				{
					continue;
				}

				final Block block = blockItem.getBlock();
				if (block instanceof IHasColour)
				{
					itemColors.register(blockItemColorHandler, block);
					blockColors.register(cosmereBlockColorHandler, block);
				}
			}
			else
			{
				//todo temp?
				if (//!itemNamespace.equals("surgebinding") &&
						!itemNamespace.equals("cosmeretools")
					//&& !itemNamespace.equals("cosmere")
				)
				{
					continue;
				}

				if (item instanceof IHasColour)
				{
					itemColors.register(itemColorHandler, item);
				}
			}
		}
	}
}
