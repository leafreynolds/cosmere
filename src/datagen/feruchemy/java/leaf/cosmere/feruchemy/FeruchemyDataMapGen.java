/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

/*
 * File created ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.feruchemy;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.datamaps.CosmereDataMaps;
import leaf.cosmere.common.datamaps.MetalmindProperties;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

//feruchemy:metalmind entries. datapack shape:
//{ "values": { "minecraft:iron_chestplate": { "metal": "iron", "charge_modifier": 0.89 } } }
//worn metal only. armour at ingots/9, chainmail half, tools are spikes instead
public class FeruchemyDataMapGen extends DataMapProvider
{
	public FeruchemyDataMapGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(HolderLookup.Provider provider)
	{
		var metalminds = builder(CosmereDataMaps.METALMIND);

		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			if (!metalType.hasFeruchemicalEffect() || metalType == Metals.MetalType.NICROSIL)
			{
				continue;
			}

			metalminds.add(
					FeruchemyItems.METAL_RINGS.get(metalType).get().builtInRegistryHolder(),
					new MetalmindProperties(metalType, MetalmindProperties.RING_CHARGE_MODIFIER),
					false);

			metalminds.add(
					FeruchemyItems.METAL_BRACELETS.get(metalType).get().builtInRegistryHolder(),
					new MetalmindProperties(metalType, MetalmindProperties.BRACELET_CHARGE_MODIFIER),
					false);
		}

		//vanilla metal armour as metalminds
		//capacity = crafting ingots / 9
		addArmourSet(metalminds, Metals.MetalType.IRON, 1f,
				Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS);
		//chainmail is iron, but far less of it
		addArmourSet(metalminds, Metals.MetalType.IRON, 0.5f,
				Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS);
		addArmourSet(metalminds, Metals.MetalType.GOLD, 1f,
				Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS);
	}

	private void addArmourSet(Builder<MetalmindProperties, Item> metalminds, Metals.MetalType metal, float metalAmountScale, Item helmet, Item chestplate, Item leggings, Item boots)
	{
		metalminds.add(helmet.builtInRegistryHolder(), new MetalmindProperties(metal, metalAmountScale * 5f / 9f), false);
		metalminds.add(chestplate.builtInRegistryHolder(), new MetalmindProperties(metal, metalAmountScale * 8f / 9f), false);
		metalminds.add(leggings.builtInRegistryHolder(), new MetalmindProperties(metal, metalAmountScale * 7f / 9f), false);
		metalminds.add(boots.builtInRegistryHolder(), new MetalmindProperties(metal, metalAmountScale * 4f / 9f), false);
	}
}
