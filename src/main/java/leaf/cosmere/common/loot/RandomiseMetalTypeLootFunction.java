/*
 * File updated ~ 26 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.common.registry.LootFunctionRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomiseMetalTypeLootFunction extends LootItemConditionalFunction
{
	public static final MapCodec<RandomiseMetalTypeLootFunction> CODEC = RecordCodecBuilder.mapCodec(
			inst -> commonFields(inst).apply(inst, RandomiseMetalTypeLootFunction::new));

	protected RandomiseMetalTypeLootFunction(List<LootItemCondition> conditionsIn)
	{
		super(conditionsIn);
	}


	@Override
	public LootItemFunctionType<RandomiseMetalTypeLootFunction> getType()
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
		for (Item value : BuiltInRegistries.ITEM)
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
			CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
			stack = new ItemStack(random.get(), stack.getCount());
			if (!customData.isEmpty())
			{
				stack.set(DataComponents.CUSTOM_DATA, customData);
			}
		}

		return stack;
	}
}
