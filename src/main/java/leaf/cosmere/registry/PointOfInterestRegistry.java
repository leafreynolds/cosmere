/*
 * File created ~ 20 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.registry;

import com.google.common.collect.ImmutableSet;
import leaf.cosmere.Cosmere;
import net.minecraft.block.Block;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class PointOfInterestRegistry
{
    public static DeferredRegister<PointOfInterestType> POINT_OF_INTERESTS = DeferredRegister.create(ForgeRegistries.POI_TYPES, Cosmere.MODID);

    public static RegistryObject<PointOfInterestType> METAL_TRADER_POI = POINT_OF_INTERESTS.register("metal_trader", () -> registerPOI("metal_trader", BlocksRegistry.METALWORKING_TABLE::get));


    private static PointOfInterestType registerPOI(String name, Supplier<Block> block)
    {
        return new PointOfInterestType(
                "cosmere:" + name,//name
                ImmutableSet.copyOf(block.get().getStateDefinition().getPossibleStates()),//states
                1, //tickets(?)
                1);//range
    }
}
