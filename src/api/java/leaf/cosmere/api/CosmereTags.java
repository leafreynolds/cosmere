/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 * File update ~ 12 - 7 - 2025 ~ Soar
 */

package leaf.cosmere.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CosmereTags
{

	public static class Items
	{
		public static final TagKey<Item> CURIO_ANY = makeItem(CuriosApi.MODID, "curio");
		public static final TagKey<Item> CURIO_BACK = makeItem(CuriosApi.MODID, "back");
		public static final TagKey<Item> CURIO_BELT = makeItem(CuriosApi.MODID, "belt");
		public static final TagKey<Item> CURIO_BODY = makeItem(CuriosApi.MODID, "body");
		public static final TagKey<Item> CURIO_BRACELET = makeItem(CuriosApi.MODID, "bracelet");
		public static final TagKey<Item> CURIO_CHARM = makeItem(CuriosApi.MODID, "charm");
		public static final TagKey<Item> CURIO_HEAD = makeItem(CuriosApi.MODID, "head");
		public static final TagKey<Item> CURIO_HANDS = makeItem(CuriosApi.MODID, "hands");
		public static final TagKey<Item> CURIO_NECKLACE = makeItem(CuriosApi.MODID, "necklace");
		public static final TagKey<Item> CURIO_RING = makeItem(CuriosApi.MODID, "ring");
		public static final TagKey<Item> CURIO_LEGS = makeItem(CuriosApi.MODID, "legs");
		public static final TagKey<Item> CURIO_FEET = makeItem(CuriosApi.MODID, "feet");
		public static final TagKey<Item> CURIO_SHARDPLATE = makeItem(CuriosApi.MODID, "shardplate");


		public static final TagKey<Item> CURIO_EYES = makeItem(CuriosApi.MODID, "eyes");
		public static final TagKey<Item> CURIO_LINCHPIN = makeItem(CuriosApi.MODID, "linchpin");
		public static final TagKey<Item> CURIO_PHYSICAL = makeItem(CuriosApi.MODID, "physical");
		public static final TagKey<Item> CURIO_MENTAL = makeItem(CuriosApi.MODID, "mental");
		public static final TagKey<Item> CURIO_TEMPORAL = makeItem(CuriosApi.MODID, "temporal");
		public static final TagKey<Item> CURIO_SPIRITUAL = makeItem(CuriosApi.MODID, "spiritual");

		public static final TagKey<Item> CONTAINS_METAL = makeItem("cosmere", "contains_metal");

		public static final TagKey<Item> METAL_SPIKE = makeItem("cosmere", "spike");

		public static final Map<Metals.MetalType, TagKey<Item>> METAL_RAW_TAGS =
				Arrays.stream(EnumUtils.METAL_TYPES)
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("raw_materials/" + type.getName())));

		public static final Map<Metals.MetalType, TagKey<Item>> METAL_ORE_TAGS =
				Arrays.stream(EnumUtils.METAL_TYPES)
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("ores/" + type.getName())));

		public static final Map<Metals.MetalType, TagKey<Item>> METAL_DUST_TAGS =
				Arrays.stream(EnumUtils.METAL_TYPES)
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("dusts/" + type.getName())));

		public static final Map<Metals.MetalType, TagKey<Item>> METAL_NUGGET_TAGS =
				Arrays.stream(EnumUtils.METAL_TYPES)
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("nuggets/" + type.getName())));

		public static final Map<Metals.MetalType, Map<Metals.MetalType, TagKey<Item>>> GOD_METAL_ALLOY_NUGGET_TAGS =
				Arrays.stream(new Metals.MetalType[]{Metals.MetalType.LERASIUM, Metals.MetalType.LERASATIUM})
						.collect(Collectors.toMap(
								Function.identity(), // key: god metal
								god -> Arrays.stream(EnumUtils.METAL_TYPES)
										.filter(base -> !base.isGodMetal())
										.collect(Collectors.toMap(
												Function.identity(), // key: base metal
												base -> forgeItemTag("nuggets/" + god.getName() + "_" + base.getName() + "_alloy")
										))
						));

		public static final Map<Metals.MetalType, TagKey<Item>> METAL_INGOT_TAGS =
				Arrays.stream(EnumUtils.METAL_TYPES)
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("ingots/" + type.getName())));

		public static final Map<Metals.MetalType, TagKey<Item>> METAL_BLOCK_ITEM_TAGS =
				Arrays.stream(EnumUtils.METAL_TYPES)
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("storage_blocks/" + type.getName())));

		public static final Map<Roshar.Gemstone, TagKey<Item>> GEM_BLOCK_ITEM_TAGS =
				Arrays.stream(EnumUtils.GEMSTONE_TYPES)
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("storage_blocks/" + type.getName())));


		public static final Map<Roshar.Gemstone, TagKey<Item>> GEM_TAGS =
				Arrays.stream(EnumUtils.GEMSTONE_TYPES)
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

		public static final TagKey<Block> DRAGON_PROOF = makeBlock("minecraft", "dragon_immune");
		public static final TagKey<Block> WITHER_PROOF = makeBlock("minecraft", "wither_immune");

		public static final TagKey<Block> CONTAINS_METAL = makeBlock("cosmere", "contains_metal");

		public static final Map<Metals.MetalType, TagKey<Block>> METAL_ORE_BLOCK_TAGS =
				Arrays.stream(EnumUtils.METAL_TYPES)
						//.filter(Metals.MetalType::hasOre)
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeTag("ores/" + type.getName())));

		public static final Map<Metals.MetalType, TagKey<Block>> METAL_BLOCK_TAGS =
				Arrays.stream(EnumUtils.METAL_TYPES)
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeTag("storage_blocks/" + type.getName())));

		public static final Map<Roshar.Gemstone, TagKey<Block>> GEM_ORE_BLOCK_TAGS =
				Arrays.stream(EnumUtils.GEMSTONE_TYPES)
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeTag("ores/" + type.getName())));

		public static final Map<Roshar.Gemstone, TagKey<Block>> GEM_BLOCK_TAGS =
				Arrays.stream(EnumUtils.GEMSTONE_TYPES)
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


	public static class Biomes
	{
		//todo move to surgebinding module
		public static final TagKey<Biome> IS_ROSHAR = create(new ResourceLocation("surgebinding", "is_roshar"));
		public static final TagKey<Biome> IS_SHADESMAR = create(new ResourceLocation(CosmereAPI.COSMERE_MODID, "is_shadesmar"));

		public static final TagKey<Biome> SPAWN_ORES = create(new ResourceLocation(CosmereAPI.COSMERE_MODID, "spawn_ores"));

		private static TagKey<Biome> create(ResourceLocation resourceLocation)
		{
			return TagKey.create(ForgeRegistries.BIOMES.getRegistryKey(), resourceLocation);
		}
	}

	public static class EntityTypes
	{
		public static final TagKey<EntityType<?>> CONTAINS_METAL = create("cosmere", "contains_metal");

		private static TagKey<EntityType<?>> create(String namespace, String path)
		{
			return create(new ResourceLocation(namespace, path));
		}

		private static TagKey<EntityType<?>> create(ResourceLocation resourceLocation)
		{
			return TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), resourceLocation);
		}
	}
}
