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
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class LootFunctionRegistry
{
	public static final LootItemFunctionType INVEST_SPIKE = register("invest_spike", new InvestSpikeLootFunction.Serializer());
	public static final LootItemFunctionType INVEST_METALMIND = register("invest_metalmind", new InvestMetalmindLootFunction.Serializer());
	public static final LootItemFunctionType RANDOMISE_METALTYPE = register("randomise_metaltype", new RandomiseMetalTypeLootFunction.Serializer());

	private static LootItemFunctionType register(String name, Serializer<? extends LootItemFunction> serializer)
	{
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Cosmere.MODID, name), new LootItemFunctionType(serializer));
	}

	public static void Register()
	{
		//static finals get initialized on class being referenced
		LogHelper.info("Registering Loot Functions");
	}
}
