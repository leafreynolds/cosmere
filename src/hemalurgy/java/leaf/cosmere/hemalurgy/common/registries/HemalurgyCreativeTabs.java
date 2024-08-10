/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.registries;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.registration.impl.CreativeTabDeferredRegister;
import leaf.cosmere.common.registration.impl.CreativeTabRegistryObject;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.items.HemalurgicSpikeItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import java.util.Collection;
import java.util.UUID;

public class HemalurgyCreativeTabs
{
	public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(Hemalurgy.MODID, HemalurgyCreativeTabs::addToExistingTabs);


	public static final CreativeTabRegistryObject ITEMS =
			CREATIVE_TABS.registerMain(
					Component.translatable("itemGroups." + Hemalurgy.MODID + ".items"),
					HemalurgyItems.METAL_SPIKE.entrySet().stream().findAny().get().getValue(),
					builder ->
							builder.withSearchBar()//Allow our tabs to be searchable for convenience purposes
									.displayItems((displayParameters, output) ->
									{
										CreativeTabDeferredRegister.addToDisplay(HemalurgyItems.ITEMS, output);
										addFilledSpikes(output);
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

	private static void addFilledSpikes(CreativeModeTab.Output output)
	{
		for (ItemRegistryObject<HemalurgicSpikeItem> spike : HemalurgyItems.METAL_SPIKE.values())
		{
			filledSpike(spike.asItem(), output);
		}
	}

	private static void filledSpike(HemalurgicSpikeItem item, CreativeModeTab.Output output)
	{
		final Metals.MetalType metalType = item.getMetalType();
		if (metalType.hasFeruchemicalEffect())
		{
			//what powers can this metal type contain
			if (metalType == Metals.MetalType.IRON)
			{
				ItemStack filledIronSpike = new ItemStack(item);
				//steals physical strength
				//don't steal modified values, only base value
				//todo decide how much strength is reasonable to steal and how much goes to waste
				//currently will try 70%
				double strengthToAdd = 15 * 0.7D;// Iron golems have the most base attack damage of normal mods (giants have 50??). Ravagers have


				item.Invest(filledIronSpike, metalType, strengthToAdd, UUID.randomUUID());

				output.accept(filledIronSpike);
			}
			else if (metalType == Metals.MetalType.TIN)
			{
				ItemStack filledIronSpike = new ItemStack(item);
				item.Invest(filledIronSpike, metalType, 0.25f, UUID.randomUUID());
				output.accept(filledIronSpike);
			}


			Collection<Metals.MetalType> hemalurgyStealWhitelist = metalType.getHemalurgyStealWhitelist();
			if (hemalurgyStealWhitelist != null)
			{
				for (Metals.MetalType stealType : hemalurgyStealWhitelist)
				{
					if (!stealType.hasAssociatedManifestation())
					{
						continue;
					}
					try
					{

						//then we've found something to steal!
						switch (metalType)
						{
							//steals allomantic abilities
							case STEEL, BRONZE, CADMIUM, ELECTRUM ->
							{
								ItemStack allomancySpike = new ItemStack(item);
								Manifestation manifestation = CosmereAPI.manifestationRegistry().getValue(new ResourceLocation("allomancy", stealType.getName()));
								item.Invest(allomancySpike, manifestation, 7, UUID.randomUUID());
								output.accept(allomancySpike);
							}
							//steals feruchemical abilities
							case PEWTER, BRASS, BENDALLOY, GOLD ->
							{
								ItemStack feruchemySpike = new ItemStack(item);
								Manifestation manifestation = CosmereAPI.manifestationRegistry().getValue(new ResourceLocation("feruchemy", stealType.getName()));
								item.Invest(feruchemySpike, manifestation, 7, UUID.randomUUID());
								output.accept(feruchemySpike);
							}
						}


					}
					catch (Exception e)
					{
						CosmereAPI.logger.info(String.format("remove %s from whitelist for %s spikes", stealType, metalType));
					}
				}
			}
		}

		if (metalType == Metals.MetalType.LERASATIUM)
		{
			ItemStack bound = new ItemStack(item);
			final UUID identity = UUID.randomUUID();
			for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
			{
				//lerasatium creative mode hide surge powers for now
				if (manifestation.getManifestationType() == Manifestations.ManifestationTypes.SURGEBINDING)
				{
					continue;
				}
				switch (manifestation.getManifestationType())
				{
					case ALLOMANCY, FERUCHEMY ->
					{
						item.Invest(bound, manifestation, 5, identity);
					}
					default ->
					{
						//skip everything else for now
					}
				}

			}

			output.accept(bound);
		}
	}
}
