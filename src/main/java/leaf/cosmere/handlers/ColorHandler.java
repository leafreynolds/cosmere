/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to Vazkii and Botania!
 * for the easier to understand example of hooking into color tints
 */

package leaf.cosmere.handlers;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.IHasMetalType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;

public final class ColorHandler
{

    public static void init()
    {
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        ItemColors itemColors = Minecraft.getInstance().getItemColors();

        IItemColor metalColorHandler =
                (itemStack, tintIndex) -> tintIndex == 0
                                      ? ((IHasMetalType) itemStack.getItem()).getMetalType().getColorValue()
                                      : -1;

        IItemColor blockItemColorHandler =
                (itemStack, tintIndex) -> tintIndex == 0
                                      ? Minecraft.getInstance().getBlockColors().getColor(((BlockItem) itemStack.getItem()).getBlock().getDefaultState(), null, null, tintIndex)
                                      : -1;

        IBlockColor metalBlockColorHandler =
                (blockState, world, pos, tintIndex) -> tintIndex == 0
                                                  ? ((IHasMetalType) blockState.getBlock()).getMetalType().getColorValue()
                                                  : -1;

        for (Metals.MetalType metalType : Metals.MetalType.values())
        {

            if (metalType.hasOre())
            {
                //blocks in world
                blockColors.register(metalBlockColorHandler, metalType.getOreBlock());
                itemColors.register(blockItemColorHandler, metalType.getOreBlock());
                itemColors.register(metalColorHandler, metalType.getRawMetalItem());
            }

            if (metalType.isAlloy())
            {
                itemColors.register(metalColorHandler, metalType.getRawMetalItem());
            }

            if (metalType.hasMaterialItem())
            {
                //ingots
                itemColors.register(metalColorHandler, metalType.getNuggetItem());
                itemColors.register(metalColorHandler, metalType.getIngotItem());
                //blocks in inventory
                itemColors.register(blockItemColorHandler, metalType.getBlock());
                //blocks in world
                blockColors.register(metalBlockColorHandler, metalType.getBlock());
            }

            if (metalType.hasFeruchemicalEffect())
            {
                itemColors.register(metalColorHandler, metalType.getRingItem());
                itemColors.register(metalColorHandler, metalType.getBraceletItem());
                itemColors.register(metalColorHandler, metalType.getNecklaceItem());
            }

            if (metalType.hasHemalurgicEffect())
            {
                itemColors.register(metalColorHandler, metalType.getSpikeItem());
            }
        }
    }
}
