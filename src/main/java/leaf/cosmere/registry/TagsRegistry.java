/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.constants.Roshar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TagsRegistry
{

	public static class Items
	{
		public static final TagKey<Item> CURIO_ANY = makeItem("curios", "curio");
		public static final TagKey<Item> CURIO_BACK = makeItem("curios", "back");
		public static final TagKey<Item> CURIO_BELT = makeItem("curios", "belt");
		public static final TagKey<Item> CURIO_BODY = makeItem("curios", "body");
		public static final TagKey<Item> CURIO_BRACELET = makeItem("curios", "bracelet");
		public static final TagKey<Item> CURIO_CHARM = makeItem("curios", "charm");
		public static final TagKey<Item> CURIO_HEAD = makeItem("curios", "head");
		public static final TagKey<Item> CURIO_HANDS = makeItem("curios", "hands");
		public static final TagKey<Item> CURIO_NECKLACE = makeItem("curios", "necklace");
		public static final TagKey<Item> CURIO_RING = makeItem("curios", "ring");

		public static TagKey<Item> METAL_SPIKE = makeItem("cosmere", "spike");

		public static final Map<Metals.MetalType, TagKey<Item>> METAL_RAW_TAGS =
				Arrays.stream(Metals.MetalType.values())
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("raw_materials/" + type.getName())));

		public static final Map<Metals.MetalType, TagKey<Item>> METAL_DUST_TAGS =
				Arrays.stream(Metals.MetalType.values())
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("dust/" + type.getName())));

		public static final Map<Metals.MetalType, TagKey<Item>> METAL_NUGGET_TAGS =
				Arrays.stream(Metals.MetalType.values())
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("nuggets/" + type.getName())));

		public static final Map<Metals.MetalType, TagKey<Item>> METAL_INGOT_TAGS =
				Arrays.stream(Metals.MetalType.values())
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("ingots/" + type.getName())));

		public static final Map<Metals.MetalType, TagKey<Item>> METAL_BLOCK_ITEM_TAGS =
				Arrays.stream(Metals.MetalType.values())
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("storage_blocks/" + type.getName())));


		public static final Map<Roshar.Polestone, TagKey<Item>> GEM_TAGS =
				Arrays.stream(Roshar.Polestone.values())
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("gems/" + type.getName())));


		public static TagKey<Item> makeItem(String domain, String path)
		{
			return ItemTags.create(new ResourceLocation(domain, path));
		}

		public static TagKey<Item> makeItem(ResourceLocation resourceLocation)
		{
			return ItemTags.create(resourceLocation);
		}

		private static TagKey<Item> forgeItemTag(String name)
		{
			final ResourceLocation forgeTagToMake = new ResourceLocation("forge", name);
			return makeItem(forgeTagToMake);
		}
	}

	public static class Blocks
	{

		public static TagKey<Block> DRAGON_PROOF = makeBlock("minecraft", "dragon_immune");
		public static TagKey<Block> WITHER_PROOF = makeBlock("minecraft", "wither_immune");

		public static final Map<Metals.MetalType, TagKey<Block>> METAL_ORE_BLOCK_TAGS =
				Arrays.stream(Metals.MetalType.values())
						.filter(Metals.MetalType::hasOre)
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeTag("ores/" + type.getName())));

		public static final Map<Metals.MetalType, TagKey<Block>> METAL_BLOCK_TAGS =
				Arrays.stream(Metals.MetalType.values())
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeTag("storage_blocks/" + type.getName())));

		public static TagKey<Block> makeBlock(String domain, String path)
		{
			return BlockTags.create(new ResourceLocation(domain, path));
		}

		public static TagKey<Block> makeBlock(ResourceLocation resourceLocation)
		{
			return BlockTags.create(resourceLocation);
		}

		private static TagKey<Block> forgeTag(String name)
		{
			final ResourceLocation forgeTagToMake = new ResourceLocation("forge", name);
			return makeBlock(forgeTagToMake);
		}

	}

}
