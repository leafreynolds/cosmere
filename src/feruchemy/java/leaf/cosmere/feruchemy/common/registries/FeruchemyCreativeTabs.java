/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.registries;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.CreativeTabDeferredRegister;
import leaf.cosmere.common.registration.impl.CreativeTabRegistryObject;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.items.BraceletMetalmindItem;
import leaf.cosmere.feruchemy.common.items.NecklaceMetalmindItem;
import leaf.cosmere.feruchemy.common.items.RingMetalmindItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

public class FeruchemyCreativeTabs
{
	public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(Feruchemy.MODID, FeruchemyCreativeTabs::addToExistingTabs);

	public static final CreativeTabRegistryObject ITEMS =
			CREATIVE_TABS.registerMain(
					Component.translatable("tabs." + Feruchemy.MODID + ".items"),
					FeruchemyItems.BANDS_OF_MOURNING,
					builder ->
							builder.withSearchBar()//Allow our tabs to be searchable for convenience purposes
									.displayItems((displayParameters, output) ->
									{
										CreativeTabDeferredRegister.addToDisplay(FeruchemyItems.ITEMS, output);
										addFilledMetalminds(output);
									})
			);

	private static void addFilledMetalminds(CreativeModeTab.Output output)
	{
		FeruchemyItems.BANDS_OF_MOURNING.get().addFilled(output);

		//I don't like the multiple for loops,
		// but this is how we have them ordered nicely in the creative menu
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasFeruchemicalEffect())
			{
				final RingMetalmindItem item = FeruchemyItems.METAL_RINGS.get(metalType).get();
				item.addFilled(output);
			}
		}
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasFeruchemicalEffect())
			{
				final BraceletMetalmindItem item = FeruchemyItems.METAL_BRACELETS.get(metalType).get();
				item.addFilled(output);
			}
		}
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasFeruchemicalEffect())
			{
				final NecklaceMetalmindItem item = FeruchemyItems.METAL_NECKLACES.get(metalType).get();
				item.addFilled(output);
			}
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
