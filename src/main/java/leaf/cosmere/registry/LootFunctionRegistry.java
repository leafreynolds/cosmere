/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to the curio mod for providing the example of how to set up fortune bonus for non-tool related things.
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.loot.InvestMetalmindLootFunction;
import leaf.cosmere.loot.InvestSpikeLootFunction;
import leaf.cosmere.loot.RandomiseMetalTypeLootFunction;
import leaf.cosmere.utils.helpers.LogHelper;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.common.Mod;

public class LootFunctionRegistry
{
    public static final LootFunctionType INVEST_SPIKE = register("invest_spike", new InvestSpikeLootFunction.Serializer());
    public static final LootFunctionType INVEST_METALMIND = register("invest_metalmind", new InvestMetalmindLootFunction.Serializer());
    public static final LootFunctionType RANDOMISE_METALTYPE = register("randomise_metaltype", new RandomiseMetalTypeLootFunction.Serializer());

    private static LootFunctionType register(String name, ILootSerializer<? extends ILootFunction> serializer)
    {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Cosmere.MODID, name), new LootFunctionType(serializer));
    }

    public static void Register()
    {
        //static finals get initialized on class being referenced
        LogHelper.info("Registering Loot Functions");
    }
}
