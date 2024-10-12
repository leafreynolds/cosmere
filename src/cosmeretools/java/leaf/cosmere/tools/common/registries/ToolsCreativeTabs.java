/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common.registries;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.CreativeTabDeferredRegister;
import leaf.cosmere.common.registration.impl.CreativeTabRegistryObject;
import leaf.cosmere.tools.common.CosmereTools;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

public class ToolsCreativeTabs
{
	public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(CosmereTools.MODID, ToolsCreativeTabs::addToExistingTabs);


	public static final CreativeTabRegistryObject ITEMS =
			CREATIVE_TABS.registerMain(
					Component.translatable("tabs." + CosmereTools.MODID + ".items"),
					ToolsItems.METAL_PICKAXES.entrySet().stream().findAny().get().getValue(),
					builder ->
							builder.withSearchBar()//Allow our tabs to be searchable for convenience purposes
									.displayItems((displayParameters, output) ->
									{
										CreativeTabDeferredRegister.addToDisplay(ToolsItems.ITEMS, output);
										//CreativeTabDeferredRegister.addToDisplay(AllomancyBlocks.BLOCKS, output);
									})
			);


	private static void addToExistingTabs(BuildCreativeModeTabContentsEvent event)
	{
		ResourceKey<CreativeModeTab> tabKey = event.getTabKey();
		if (tabKey == CreativeModeTabs.BUILDING_BLOCKS)
		{
			//CreativeTabDeferredRegister.addToDisplay(event, BlocksRegistry.METALWORKING_TABLE);
		}
		else if (tabKey == CreativeModeTabs.NATURAL_BLOCKS)
		{
			/*for (var ore : BlocksRegistry.METAL_ORE.values())
			{
				CreativeTabDeferredRegister.addToDisplay(event, ore);
			}
			for (var ore : BlocksRegistry.METAL_ORE_DEEPSLATE.values())
			{
				CreativeTabDeferredRegister.addToDisplay(event, ore);
			}*/
		}
		else if (tabKey == CreativeModeTabs.FUNCTIONAL_BLOCKS)
		{

		}
		else if (tabKey == CreativeModeTabs.REDSTONE_BLOCKS)
		{

		}
		else if (tabKey == CreativeModeTabs.TOOLS_AND_UTILITIES)
		{
			for (var metalType : Metals.MetalType.values())
			{
				if (!metalType.hasMaterialItem())
				{
					continue;
				}

				CreativeTabDeferredRegister.addToDisplay(event, ToolsItems.METAL_PICKAXES.get(metalType));
				CreativeTabDeferredRegister.addToDisplay(event, ToolsItems.METAL_SHOVEL.get(metalType));
				CreativeTabDeferredRegister.addToDisplay(event, ToolsItems.METAL_AXES.get(metalType));
				CreativeTabDeferredRegister.addToDisplay(event, ToolsItems.METAL_HOE.get(metalType));
			}
		}
		else if (tabKey == CreativeModeTabs.COMBAT)
		{
			for (var metalType : Metals.MetalType.values())
			{
				if (!metalType.hasMaterialItem())
				{
					continue;
				}

				CreativeTabDeferredRegister.addToDisplay(event, ToolsItems.METAL_SWORDS.get(metalType));
				CreativeTabDeferredRegister.addToDisplay(event, ToolsItems.METAL_HELMETS.get(metalType));
				CreativeTabDeferredRegister.addToDisplay(event, ToolsItems.METAL_CHESTPLATES.get(metalType));
				CreativeTabDeferredRegister.addToDisplay(event, ToolsItems.METAL_LEGGINGS.get(metalType));
				CreativeTabDeferredRegister.addToDisplay(event, ToolsItems.METAL_BOOTS.get(metalType));
			}
		}
		else if (tabKey == CreativeModeTabs.FOOD_AND_DRINKS)
		{
			//ItemsRegistry.SOME_FOOD.get().addItems(event);
		}
		else if (tabKey == CreativeModeTabs.SPAWN_EGGS)
		{

		}
		else if (tabKey == CreativeModeTabs.INGREDIENTS)
		{

		}
	}

}
