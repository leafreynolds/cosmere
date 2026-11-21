/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.tools;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.tag.BaseTagProvider;
import leaf.cosmere.tools.common.CosmereTools;
import leaf.cosmere.tools.common.registries.ToolsBlocks;
import leaf.cosmere.tools.common.registries.ToolsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ToolsTagProvider extends BaseTagProvider
{
	public ToolsTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(packOutput, lookupProvider, CosmereTools.MODID, existingFileHelper);
	}


	//make the tool-spikes wearable in their spike category slots. no eye slots
	private void addSpikeToolCurioTags()
	{
		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			if (!metalType.hasMaterialItem() || !metalType.hasHemalurgicEffect())
			{
				continue;
			}

			List<Item> toolSpikes = List.of(
					ToolsItems.METAL_SWORDS.get(metalType).get(),
					ToolsItems.METAL_AXES.get(metalType).get(),
					ToolsItems.METAL_PICKAXES.get(metalType).get(),
					ToolsItems.METAL_SHOVEL.get(metalType).get(),
					ToolsItems.METAL_HOE.get(metalType).get());

			for (Item toolSpike : toolSpikes)
			{
				if (metalType.isPhysicalSpike())
				{
					getItemBuilder(CosmereTags.Items.CURIO_PHYSICAL).add(toolSpike);
					//any spike can be a linchpin?
					getItemBuilder(CosmereTags.Items.CURIO_LINCHPIN).add(toolSpike);
				}
				if (metalType.isMentalSpike())
				{
					getItemBuilder(CosmereTags.Items.CURIO_MENTAL).add(toolSpike);
				}
				if (metalType.isSpiritualSpike())
				{
					getItemBuilder(CosmereTags.Items.CURIO_SPIRITUAL).add(toolSpike);
				}
				if (metalType.isTemporalSpike())
				{
					getItemBuilder(CosmereTags.Items.CURIO_TEMPORAL).add(toolSpike);
				}
			}
		}
	}

	@Override
	protected List<IBlockProvider> getAllBlocks()
	{
		return ToolsBlocks.BLOCKS.getAllBlocks();
	}


	@Override
	protected void registerTags(HolderLookup.Provider registries)
	{
		//getItemBuilder(CosmereTags.Items.CURIO_HEAD).add(Tools.Item.asItem());

		addItems();
		//todo - decide if tools should legit be allowed to be spikes lol
		//addSpikeToolCurioTags();
		addBlocks();
		addStorageBlocks();
		addEntityTypes();
		addGameEvents();

		addContainsMetal();
	}


	private void addItems()
	{
		for (var item : ToolsItems.METAL_SWORDS.values())
			getItemBuilder(ItemTags.SWORDS).add(item);

		for (var item : ToolsItems.METAL_AXES.values())
			getItemBuilder(ItemTags.AXES).add(item);

		for (var item : ToolsItems.METAL_SHOVEL.values())
			getItemBuilder(ItemTags.SHOVELS).add(item);

		for (var item : ToolsItems.METAL_PICKAXES.values())
			getItemBuilder(ItemTags.PICKAXES).add(item);

		for (var item : ToolsItems.METAL_HOE.values())
			getItemBuilder(ItemTags.HOES).add(item);

	}

	private void addBlocks()
	{
		//addToTag(BlockTags.NEEDS_STONE_TOOL, BlocksRegistry.METALWORKING_TABLE);
		//addToHarvestTag(BlockTags.MINEABLE_WITH_AXE, BlocksRegistry.METALWORKING_TABLE);

	}

	private void addEntityTypes()
	{
		//getEntityTypeBuilder(CosmereTags.EntityTypes.CONTAINS_METAL).add(EntityType.IRON_GOLEM);
	}

	private void addGameEvents()
	{

	}

	private void addStorageBlocks()
	{

	}

	private void addContainsMetal()
	{

	}
}