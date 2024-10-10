/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.common.registration.impl.CreativeTabDeferredRegister;
import leaf.cosmere.common.registration.impl.CreativeTabRegistryObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

public class AllomancyCreativeTabs
{
	public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(Allomancy.MODID, AllomancyCreativeTabs::addToExistingTabs);


	public static final CreativeTabRegistryObject ITEMS =
			CREATIVE_TABS.registerMain(
					Component.translatable("tabs." + Allomancy.MODID + ".items"),
					AllomancyItems.COIN_POUCH,
					builder ->
							builder.withSearchBar()//Allow our tabs to be searchable for convenience purposes
									.displayItems((displayParameters, output) ->
									{
										CreativeTabDeferredRegister.addToDisplay(AllomancyItems.ITEMS, output);
										AllomancyItems.METAL_VIAL.get().addFilled(output);
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

		}
		else if (tabKey == CreativeModeTabs.COMBAT)
		{

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
