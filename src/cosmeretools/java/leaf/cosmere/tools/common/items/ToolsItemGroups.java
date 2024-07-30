/*
 * File updated ~ 23 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common.items;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.tools.common.CosmereTools;
import leaf.cosmere.tools.common.registries.ToolsItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ToolsItemGroups
{
	public static final Supplier<Item.Properties> TOOL = () -> new Item.Properties();

	public static CreativeModeTab TOOLS_TAB = CreativeModeTab.builder()
			.icon(() -> new ItemStack(ToolsItems.METAL_PICKAXES.entrySet().stream().findAny().get().getValue().get()))
			// todo: populate with .displayItems()
			.build();

}
