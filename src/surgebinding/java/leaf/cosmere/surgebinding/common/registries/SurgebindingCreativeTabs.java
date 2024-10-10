/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

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
					SurgebindingItems.HONORBLADES.get(Roshar.Gemstone.SMOKESTONE),
					builder ->
							builder.withSearchBar()//Allow our tabs to be searchable for convenience purposes
									.displayItems((displayParameters, output) ->
									{
										CreativeTabDeferredRegister.addToDisplay(SurgebindingItems.ITEMS, output);
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
			for (var gemstone : Roshar.Gemstone.values())
			{
				CreativeTabDeferredRegister.addToDisplay(event, SurgebindingItems.HONORBLADES.get(gemstone));
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
			for (var gemstone : Roshar.Gemstone.values())
			{
				CreativeTabDeferredRegister.addToDisplay(event, SurgebindingItems.GEMSTONE_CHIPS.get(gemstone));
				CreativeTabDeferredRegister.addToDisplay(event, SurgebindingItems.GEMSTONE_MARKS.get(gemstone));
				CreativeTabDeferredRegister.addToDisplay(event, SurgebindingItems.GEMSTONE_BROAMS.get(gemstone));
			}
		}
	}

}
