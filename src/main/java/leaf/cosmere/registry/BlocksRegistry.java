/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to SizableShrimp from the Forge Project discord!
 * Java isn't my first programming language, so I didn't know you could collect and set up items like this!
 * Makes setting up items for metals a breeze~
 */

package leaf.cosmere.registry;

import leaf.cosmere.blocks.MetalOreBlock;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.properties.PropTypes;
import leaf.cosmere.Cosmere;
import leaf.cosmere.blocks.BaseBlock;
import leaf.cosmere.blocks.MetalBlock;
import leaf.cosmere.constants.Metals;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BlocksRegistry
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Cosmere.MODID);
    public static final RegistryObject<Block> GEM_BLOCK = register("gem_block", () -> (new BaseBlock(PropTypes.Blocks.EXAMPLE.get(), SoundType.STONE, 1F, 2F)), Rarity.COMMON);


    public static final Map<Metals.MetalType, RegistryObject<MetalBlock>> METAL_BLOCKS =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasMaterialItem)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            metalType -> register(
                                    metalType.name().toLowerCase() + Constants.RegNameStubs.BLOCK,
                                    () -> new MetalBlock(metalType), metalType.getRarity())));

    public static final Map<Metals.MetalType, RegistryObject<MetalOreBlock>> METAL_ORE =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasOre)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            metalType -> register(
                                    metalType.name().toLowerCase() + Constants.RegNameStubs.ORE,
                                    () -> new MetalOreBlock(metalType),
                                    Rarity.COMMON)));



    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, Rarity itemRarity)
    {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        ItemsRegistry.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties().group(CosmereItemGroups.BLOCKS).rarity(itemRarity)));


        return registryObject;
    }

}
