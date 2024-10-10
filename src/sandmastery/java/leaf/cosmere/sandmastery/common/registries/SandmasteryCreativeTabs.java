/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.common.registration.impl.CreativeTabDeferredRegister;
import leaf.cosmere.common.registration.impl.CreativeTabRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

public class SandmasteryCreativeTabs
{
	public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(Sandmastery.MODID, SandmasteryCreativeTabs::addToExistingTabs);


	public static final CreativeTabRegistryObject ITEMS =
			CREATIVE_TABS.registerMain(
					Component.translatable("tabs." + Sandmastery.MODID + ".items"),
					SandmasteryItems.QIDO_ITEM,
					builder ->
							builder.withSearchBar()//Allow our tabs to be searchable for convenience purposes
									.displayItems((displayParameters, output) ->
									{
										CreativeTabDeferredRegister.addToDisplay(SandmasteryItems.ITEMS, output);
										SandmasteryItems.QIDO_ITEM.get().addFilled(output);
										CreativeTabDeferredRegister.addToDisplay(SandmasteryBlocks.BLOCKS, output);
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
			CreativeTabDeferredRegister.addToDisplay(event, SandmasteryBlocks.SAND_JAR_BLOCK);
		}
		else if (tabKey == CreativeModeTabs.REDSTONE_BLOCKS)
		{
			CreativeTabDeferredRegister.addToDisplay(event, SandmasteryBlocks.SAND_JAR_BLOCK);
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
