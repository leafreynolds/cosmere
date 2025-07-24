/*
 * File updated ~ 14 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.registration.impl.CreativeTabDeferredRegister;
import leaf.cosmere.common.registration.impl.CreativeTabRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

public class SurgebindingCreativeTabs
{
	public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(Surgebinding.MODID, SurgebindingCreativeTabs::addToExistingTabs);


	public static final CreativeTabRegistryObject ITEMS =
			CREATIVE_TABS.registerMain(
					Component.translatable("tabs." + Surgebinding.MODID + ".items"),
					SurgebindingItems.HONORBLADES.get(Roshar.RadiantOrder.SKYBREAKER),
					builder ->
							builder.withSearchBar()//Allow our tabs to be searchable for convenience purposes
									.displayItems((displayParameters, output) ->
									{
										CreativeTabDeferredRegister.addToDisplay(SurgebindingItems.ITEMS, output);
										//CreativeTabDeferredRegister.addToDisplay(AllomancyBlocks.BLOCKS, output);
										addFilled(output);
									})
			);

	private static void addFilled(CreativeModeTab.Output output)
	{
		for (var gemstone : EnumUtils.GEMSTONE_TYPES)
		{
			SurgebindingItems.GEMSTONE_CHIPS.get(gemstone).get().addFilled(output);
			SurgebindingItems.GEMSTONE_MARKS.get(gemstone).get().addFilled(output);
			SurgebindingItems.GEMSTONE_BROAMS.get(gemstone).get().addFilled(output);
		}
		for(var order : EnumUtils.RADIANT_ORDERS)
		{
			SurgebindingItems.SHARDPLATE_SUITS.get(order).get().addFilled(output);
		}
	}

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
			for (var gemstone : EnumUtils.RADIANT_ORDERS)
			{
				CreativeTabDeferredRegister.addToDisplay(event, SurgebindingItems.HONORBLADES.get(gemstone));
				CreativeTabDeferredRegister.addToDisplay(event, SurgebindingItems.SHARDPLATE_SUITS.get(gemstone));
			}
		}
		else if (tabKey == CreativeModeTabs.FOOD_AND_DRINKS)
		{
			//ItemsRegistry.SOME_FOOD.get().addItems(event);
		}
		else if (tabKey == CreativeModeTabs.SPAWN_EGGS)
		{
			CreativeTabDeferredRegister.addToDisplay(event, SurgebindingItems.CHULL_EGG, SurgebindingItems.CRYPTIC_EGG);
		}
		else if (tabKey == CreativeModeTabs.INGREDIENTS)
		{
			for (var gemstone : EnumUtils.GEMSTONE_TYPES)
			{
				CreativeTabDeferredRegister.addToDisplay(event, SurgebindingItems.GEMSTONE_CHIPS.get(gemstone));
				CreativeTabDeferredRegister.addToDisplay(event, SurgebindingItems.GEMSTONE_MARKS.get(gemstone));
				CreativeTabDeferredRegister.addToDisplay(event, SurgebindingItems.GEMSTONE_BROAMS.get(gemstone));
			}
		}
	}

}
