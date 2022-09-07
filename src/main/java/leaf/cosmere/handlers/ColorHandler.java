/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to Vazkii and Botania!
 * for the easier to understand example of hooking into color tints
 */

package leaf.cosmere.handlers;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.constants.Roshar;
import leaf.cosmere.items.IHasColour;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.BlockItem;

public final class ColorHandler
{

	public static void init()
	{
		BlockColors blockColors = Minecraft.getInstance().getBlockColors();
		ItemColors itemColors = Minecraft.getInstance().getItemColors();

		ItemColor cosmereColourHandler =
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

		itemColors.register(cosmereColourHandler, ItemsRegistry.BANDS_OF_MOURNING.get());
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasOre())
			{
				//blocks in world
				blockColors.register(cosmereBlockColorHandler, metalType.getOreBlock());
				blockColors.register(cosmereBlockColorHandler, metalType.getDeepslateOreBlock());

				itemColors.register(blockItemColorHandler, metalType.getOreBlock());
				itemColors.register(blockItemColorHandler, metalType.getDeepslateOreBlock());

				itemColors.register(cosmereColourHandler, metalType.getRawMetalItem());
			}

			if (metalType.isAlloy())
			{
				itemColors.register(cosmereColourHandler, metalType.getRawMetalItem());
			}

			if (metalType.hasMaterialItem())
			{
				//ingots
				itemColors.register(cosmereColourHandler, metalType.getNuggetItem());
				itemColors.register(cosmereColourHandler, metalType.getIngotItem());
				//blocks in inventory
				itemColors.register(blockItemColorHandler, metalType.getBlock());
				//blocks in world
				blockColors.register(cosmereBlockColorHandler, metalType.getBlock());
			}

			if (metalType.hasFeruchemicalEffect())
			{
				itemColors.register(cosmereColourHandler, metalType.getRingItem());
				itemColors.register(cosmereColourHandler, metalType.getBraceletItem());
				itemColors.register(cosmereColourHandler, metalType.getNecklaceItem());
			}

			if (metalType.hasHemalurgicEffect())
			{
				itemColors.register(cosmereColourHandler, metalType.getSpikeItem());
			}
		}

		for (Roshar.Polestone polestone : Roshar.Polestone.values())
		{
			//blockColors.register(cosmereBlockColorHandler, metalType.getOreBlock());
			for (Roshar.GemSize size : Roshar.GemSize.values())
			{
				itemColors.register(cosmereColourHandler, polestone.getPolestoneItem(size));
			}

			itemColors.register(blockItemColorHandler, BlocksRegistry.GEM_BLOCKS.get(polestone).get());
			itemColors.register(blockItemColorHandler, BlocksRegistry.GEM_ORE.get(polestone).get());
			itemColors.register(blockItemColorHandler, BlocksRegistry.GEM_ORE_DEEPSLATE.get(polestone).get());

			blockColors.register(cosmereBlockColorHandler, BlocksRegistry.GEM_BLOCKS.get(polestone).get());
			blockColors.register(cosmereBlockColorHandler, BlocksRegistry.GEM_ORE.get(polestone).get());
			blockColors.register(cosmereBlockColorHandler, BlocksRegistry.GEM_ORE_DEEPSLATE.get(polestone).get());
		}
	}
}
