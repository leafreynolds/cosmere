/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to SizableShrimp from the Forge Project discord!
 * Java isn't my first programming language, so I didn't know you could collect and set up items like this!
 * Makes setting up items for metals a breeze~
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Constants.RegNameStubs;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.items.*;
import leaf.cosmere.items.curio.BraceletMetalmind;
import leaf.cosmere.items.curio.HemalurgicSpikeItem;
import leaf.cosmere.items.curio.NecklaceMetalmind;
import leaf.cosmere.items.curio.RingMetalmind;
import leaf.cosmere.properties.PropTypes;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemsRegistry
{
    public static final DeferredRegister<net.minecraft.item.Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Cosmere.MODID);

    //other items

    public static final RegistryObject<net.minecraft.item.Item> GUIDE = ITEMS.register("guide", () -> createItem(new GuideItem()));
    public static final RegistryObject<net.minecraft.item.Item> INVESTITURE = ITEMS.register("investiture", () -> createItem(new BaseItem()));

    public static final RegistryObject<net.minecraft.item.Item> MIST_CLOAK = ITEMS.register("mist_cloak", () -> createItem(new ElytraItem(PropTypes.Items.ONE.get())));
    public static final RegistryObject<net.minecraft.item.Item> OBSIDIAN_DAGGER = ITEMS.register("obsidian_dagger", () -> createItem(new SwordItem(ItemTier.DIAMOND, 2, -1.4F, PropTypes.Items.ONE.get().rarity(Rarity.UNCOMMON))));
    public static final RegistryObject<net.minecraft.item.Item> NIGHT_BLOOD = ITEMS.register("night_blood", () -> createItem(new ShardbladeItem(ItemTier.NETHERITE, 10, -1.4F, PropTypes.Items.ONE.get().setNoRepair().rarity(Rarity.EPIC))));

    public static final RegistryObject<net.minecraft.item.Item> COPPER_CLIP = ITEMS.register("copper_clip", () -> createItem(new MetalNuggetItem(Metals.MetalType.COPPER)));
    public static final RegistryObject<net.minecraft.item.Item> GOLD_BOXING = ITEMS.register("gold_boxing", () -> createItem(new MetalNuggetItem(Metals.MetalType.GOLD)));

    public static final RegistryObject<net.minecraft.item.Item> METAL_FILE = ITEMS.register("metal_file", () -> createItem(new MetalFileItem()));


    public static final RegistryObject<net.minecraft.item.Item> JAR_EMPTY = ITEMS.register("jar_empty", () -> createItem(new BaseItem(PropTypes.Items.SIXTEEN.get().group(CosmereItemGroups.ITEMS))));
    public static final RegistryObject<net.minecraft.item.Item> JAR_OF_BLOOD = ITEMS.register("jar_of_blood", () -> createItem(new BaseItem(PropTypes.Items.ONE.get().group(CosmereItemGroups.ITEMS))));


    //Mass items gen

    public static final Map<Metals.MetalType, RegistryObject<net.minecraft.item.Item>> METAL_NUGGETS =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasMaterialItem)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            type -> ITEMS.register(
                                    type.name().toLowerCase() + RegNameStubs.NUGGET,
                                    () -> createItem(new MetalNuggetItem(type))
                            )));

    /*public static final Map<Metals.MetalType, RegistryObject<net.minecraft.item.Item>> METAL_SHAVINGS =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasMaterialItem)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            type -> ITEMS.register(
                                    type.name().toLowerCase() + Item.SHAVINGS,
                                    () -> createItem(new MetalShavingsItem(type))
                            )));*/

    public static final Map<Metals.MetalType, RegistryObject<net.minecraft.item.Item>> METAL_INGOTS =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasMaterialItem)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            type -> ITEMS.register(
                                    type.name().toLowerCase() + RegNameStubs.INGOT,
                                    () -> createItem(new MetalIngotItem(type))
                            )));

    public static final Map<Metals.MetalType, RegistryObject<net.minecraft.item.Item>> METAL_RINGS =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasFeruchemicalEffect)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            type -> ITEMS.register(
                                    type.name().toLowerCase() + RegNameStubs.RING + RegNameStubs.METALMIND,
                                    () -> createItem(new RingMetalmind(type))
                            )));

    public static final Map<Metals.MetalType, RegistryObject<net.minecraft.item.Item>> METAL_BRACELETS =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasFeruchemicalEffect)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            type -> ITEMS.register(
                                    type.name().toLowerCase() + RegNameStubs.BRACELET + RegNameStubs.METALMIND,
                                    () -> createItem(new BraceletMetalmind(type))
                            )));

    public static final Map<Metals.MetalType, RegistryObject<net.minecraft.item.Item>> METAL_NECKLACES =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasFeruchemicalEffect)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            type -> ITEMS.register(
                                    type.name().toLowerCase() + RegNameStubs.NECKLACE + RegNameStubs.METALMIND,
                                    () -> createItem(new NecklaceMetalmind(type))
                            )));

    public static final Map<Metals.MetalType, RegistryObject<net.minecraft.item.Item>> METAL_SPIKE =
            Arrays.stream(Metals.MetalType.values())
                    .filter(Metals.MetalType::hasHemalurgicEffect)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            type -> ITEMS.register(
                                    type.name().toLowerCase() + RegNameStubs.SPIKE,
                                    () -> createItem(new HemalurgicSpikeItem(type))
                            )));


    private static <T extends net.minecraft.item.Item> T createItem(T item)
    {
        return item;
    }

}
