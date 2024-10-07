/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.items;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import leaf.cosmere.tag.BaseTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class SandmasteryTagsProvider extends BaseTagProvider
{
	public SandmasteryTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(packOutput, lookupProvider, Sandmastery.MODID, existingFileHelper);
	}

	@Override
	protected void registerTags(HolderLookup.Provider registries)
	{
		getItemBuilder(CosmereTags.Items.CURIO_BELT).add(SandmasteryItems.SAND_POUCH_ITEM.asItem());

		getBlockBuilder(BlockTags.MINEABLE_WITH_SHOVEL).add(SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_SAND.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SAND.getBlock());
		getBlockBuilder(BlockTags.SAND).add(SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_SAND.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SAND.getBlock());
		getBlockBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(SandmasteryBlocks.SAND_JAR_BLOCK.getBlock());
		getBlockBuilder(BlockTags.MINEABLE_WITH_AXE).add(SandmasteryBlocks.SAND_SPREADING_TUB_BLOCK.getBlock());
	}

}