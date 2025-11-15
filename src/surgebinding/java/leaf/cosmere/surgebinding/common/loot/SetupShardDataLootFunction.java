package leaf.cosmere.surgebinding.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import leaf.cosmere.surgebinding.common.items.HonorbladeItem;
import leaf.cosmere.surgebinding.common.items.IShardItem;
import leaf.cosmere.surgebinding.common.items.NightbloodItem;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import leaf.cosmere.surgebinding.common.registries.SurgebindingLootFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Random;

public class SetupShardDataLootFunction extends LootItemConditionalFunction
{
	protected SetupShardDataLootFunction(LootItemCondition[] pPredicates)
	{
		super(pPredicates);
	}

	@Override
	protected ItemStack run(ItemStack pStack, LootContext pContext)
	{
		if(pStack.getItem() instanceof IShardItem shard)
		{
			Random rand = new Random();
			int chance = rand.nextInt(0, 40);

			if(chance <= 3)
			{
				pStack = new ItemStack(SurgebindingItems.SHARDBLADE);
				shard = (IShardItem) pStack.getItem();
				shard.randomizedLootData(pStack);
				return pStack;
			}
			else if(chance <= 5)
			{
				pStack = new ItemStack(SurgebindingItems.SHARDPLATE);
				shard = (IShardItem) pStack.getItem();
				shard.randomizedLootData(pStack);
				return pStack;
			}
			else if(chance == 39)
			{
				pStack = new ItemStack(SurgebindingItems.NIGHTBLOOD);
				return pStack;
			}
			else
			{
				pStack = ItemStack.EMPTY;
				return pStack;
			}


		}
		else
		{
			return pStack;
		}
	}

	@Override
	public LootItemFunctionType getType()
	{
		return SurgebindingLootFunctions.SETUP_DATA.get();
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<SetupShardDataLootFunction>
	{
		@Override
		public SetupShardDataLootFunction deserialize(JsonObject jsonObject, JsonDeserializationContext
				deserializationContext, LootItemCondition[] lootConditions)
		{
			return new SetupShardDataLootFunction(lootConditions);
		}
	}
}
