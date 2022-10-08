/*
 * File updated ~ 26 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.common.registry.LootFunctionRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomiseMetalTypeLootFunction extends LootItemConditionalFunction
{

	protected RandomiseMetalTypeLootFunction(LootItemCondition[] conditionsIn)
	{
		super(conditionsIn);
	}


	@Override
	public LootItemFunctionType getType()
	{
		return LootFunctionRegistry.RANDOMISE_METALTYPE.get();
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext lootContext)
	{
		final Item item = stack.getItem();
		if (!(item instanceof IHasMetalType))
		{
			return stack;
		}

		List<Item> itemsOfClass = new ArrayList<>();
		for (Item value : ForgeRegistries.ITEMS.getValues())
		{
			if (value.getClass().equals(item.getClass()))
			{
				IHasMetalType iHasMetalType = (IHasMetalType) value;
				switch (iHasMetalType.getMetalType())
				{
					case LERASIUM:
					case HARMONIUM:
					case LERASATIUM:
						break;
					default:
						itemsOfClass.add(value);
						break;
				}
			}
		}

		Collections.shuffle(itemsOfClass);
		var random = itemsOfClass.stream().findFirst();
		if (random.isPresent())
		{
			CompoundTag nbt = stack.getOrCreateTag().copy();
			stack = new ItemStack(random.get(), stack.getCount());
			stack.setTag(nbt);
		}

		return stack;
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<RandomiseMetalTypeLootFunction>
	{
		@Override
		public RandomiseMetalTypeLootFunction deserialize(JsonObject jsonObject, JsonDeserializationContext deserializationContext, LootItemCondition[] lootConditions)
		{
			return new RandomiseMetalTypeLootFunction(lootConditions);
		}
	}
}