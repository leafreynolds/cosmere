/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.registration.impl.BlockDeferredRegister;
import leaf.cosmere.common.registration.impl.BlockRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.blocks.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Rarity;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SurgebindingBlocks
{
	public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Surgebinding.MODID);

	public static final Map<Roshar.Gemstone, BlockRegistryObject<GemBlock, BlockItem>> GEM_BLOCKS =
			Arrays.stream(Roshar.Gemstone.values())
					.collect(Collectors.toMap(
							Function.identity(),
							gemstone -> BLOCKS.registerWithRarity(
									gemstone.getName() + Constants.RegNameStubs.BLOCK,
									() -> new GemBlock(gemstone),
									Rarity.UNCOMMON)));

	public static final Map<Roshar.Gemstone, BlockRegistryObject<GemOreBlock, BlockItem>> GEM_ORE =
			Arrays.stream(Roshar.Gemstone.values())
					.collect(Collectors.toMap(
							Function.identity(),
							gemstone -> BLOCKS.registerWithRarity(
									gemstone.getName() + Constants.RegNameStubs.ORE,
									() -> new GemOreBlock(gemstone),
									Rarity.UNCOMMON)));

	public static final Map<Roshar.Gemstone, BlockRegistryObject<GemOreBlock, BlockItem>> GEM_ORE_DEEPSLATE =
			Arrays.stream(Roshar.Gemstone.values())
					.collect(Collectors.toMap(
							Function.identity(),
							gemstone -> BLOCKS.registerWithRarity(
									Constants.RegNameStubs.DEEPSLATE + gemstone.getName() + Constants.RegNameStubs.ORE,
									() -> new GemOreBlock(gemstone),
									Rarity.UNCOMMON)));

	public static final BlockRegistryObject<LavisPolypBlock, BlockItem> LAVIS_POLYP_BLOCK = BLOCKS.register("lavis_polyp", LavisPolypBlock::new);
	public static final BlockRegistryObject<PrickletacBlock, BlockItem> PRICKLETAC_BLOCK = BLOCKS.register("prickletac", PrickletacBlock::new);
	public static final BlockRegistryObject<RockbudVariantBlock, BlockItem> ROCKBUD_VARIANT_BLOCK = BLOCKS.register("rockbud_variant", RockbudVariantBlock::new);
	public static final BlockRegistryObject<VinebudBlock, BlockItem> VINEBUD_BLOCK = BLOCKS.register("vinebud", VinebudBlock::new);

}
