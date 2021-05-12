/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.constants.Metals;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static leaf.cosmere.utils.helpers.ResourceLocationHelper.prefix;

public class TagsRegistry
{
    private static <T> ITag.INamedTag<T> getOrRegister(List<? extends ITag.INamedTag<T>> list,
                                                       Function<ResourceLocation, ITag.INamedTag<T>> register,
                                                       ResourceLocation loc)
    {
        for (ITag.INamedTag<T> existing : list)
        {
            if (existing.getName().equals(loc))
            {
                return existing;
            }
        }

        return register.apply(loc);
    }

    public static class Items
    {


        public static ITag.INamedTag<Item> CURIO_ANY = makeItem("curios", "curio");
        public static ITag.INamedTag<Item> CURIO_BACK = makeItem("curios", "back");
        public static ITag.INamedTag<Item> CURIO_BELT = makeItem("curios", "belt");
        public static ITag.INamedTag<Item> CURIO_BODY = makeItem("curios", "body");
        public static ITag.INamedTag<Item> CURIO_BRACELET = makeItem("curios", "bracelet");
        public static ITag.INamedTag<Item> CURIO_CHARM = makeItem("curios", "charm");
        public static ITag.INamedTag<Item> CURIO_HEAD = makeItem("curios", "head");
        public static ITag.INamedTag<Item> CURIO_HANDS = makeItem("curios", "hands");
        public static ITag.INamedTag<Item> CURIO_NECKLACE = makeItem("curios", "necklace");
        public static ITag.INamedTag<Item> CURIO_RING = makeItem("curios", "ring");


        public static ITag.INamedTag<Item> NUGGET = forgeItemTag("nugget");


        public static final Map<Metals.MetalType, ITag.INamedTag<Item>> METAL_RAW_TAGS =
                Arrays.stream(Metals.MetalType.values())
                        .collect(Collectors.toMap(
                                Function.identity(),
                                type -> forgeItemTag("raw/" + type.name().toLowerCase())));

        public static final Map<Metals.MetalType, ITag.INamedTag<Item>> METAL_DUST_TAGS =
                Arrays.stream(Metals.MetalType.values())
                        .collect(Collectors.toMap(
                                Function.identity(),
                                type -> forgeItemTag("dust/" + type.name().toLowerCase())));

        public static final Map<Metals.MetalType, ITag.INamedTag<Item>> METAL_NUGGET_TAGS =
                Arrays.stream(Metals.MetalType.values())
                        .collect(Collectors.toMap(
                                Function.identity(),
                                type -> forgeItemTag("nuggets/" + type.name().toLowerCase())));

        public static final Map<Metals.MetalType, ITag.INamedTag<Item>> METAL_INGOT_TAGS =
                Arrays.stream(Metals.MetalType.values())
                        .collect(Collectors.toMap(
                                Function.identity(),
                                type -> forgeItemTag("ingots/" + type.name().toLowerCase())));

        public static final Map<Metals.MetalType, ITag.INamedTag<Item>> METAL_BLOCK_ITEM_TAGS =
                Arrays.stream(Metals.MetalType.values())
                        .collect(Collectors.toMap(
                                Function.identity(),
                                type -> forgeItemTag("storage_blocks/" + type.name().toLowerCase())));


        private static ITag.INamedTag<Item> itemTag(String name)
        {
            return ItemTags.makeWrapperTag(prefix(name).toString());
        }

        private static ITag.INamedTag<Item> forgeItemTag(String name)
        {
            return getOrRegister(ItemTags.getAllTags(), loc -> ItemTags.makeWrapperTag(loc.toString()), new ResourceLocation("forge", name));
        }


        public static ITag.INamedTag<Item> makeItem(String domain, String path)
        {
            return ItemTags.makeWrapperTag(new ResourceLocation(domain, path).toString());
        }
    }

    public static class Blocks
    {


        public static ITag.INamedTag<Block> makeBlock(String domain, String path)
        {
            return BlockTags.makeWrapperTag(new ResourceLocation(domain, path).toString());
        }

        public static ITag.INamedTag<Block> DRAGON_PROOF = makeBlock("minecraft", "dragon_immune");
        public static ITag.INamedTag<Block> WITHER_PROOF = makeBlock("minecraft", "wither_immune");

        public static final Map<Metals.MetalType, ITag.INamedTag<Block>> METAL_BLOCK_TAGS =
                Arrays.stream(Metals.MetalType.values())
                        .collect(Collectors.toMap(
                                Function.identity(),
                                type -> forgeTag("storage_blocks/" + type.name().toLowerCase())));

        private static ITag.INamedTag<Block> forgeTag(String name)
        {
            return getOrRegister(BlockTags.getAllTags(), loc -> BlockTags.makeWrapperTag(loc.toString()), new ResourceLocation("forge", name));
        }

    }

}
