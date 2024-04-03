/*
 * File updated ~ 23 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common.items;

import leaf.cosmere.tools.common.CosmereTools;
import leaf.cosmere.tools.common.registries.ToolsItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ToolsItemGroups
{
	public static final Supplier<Item.Properties> TOOL = () -> new Item.Properties().tab(ToolsItemGroups.TOOLS_TAB).stacksTo(1);

	public static CreativeModeTab TOOLS_TAB = new CreativeModeTab(CosmereTools.MODID + ".tools")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ToolsItems.METAL_PICKAXES.entrySet().stream().findAny().get().getValue().get());
		}
	};

}
