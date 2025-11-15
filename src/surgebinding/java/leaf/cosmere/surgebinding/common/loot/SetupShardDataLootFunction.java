package leaf.cosmere.surgebinding.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import leaf.cosmere.surgebinding.common.items.HonorbladeItem;
import leaf.cosmere.surgebinding.common.items.IShardItem;
import leaf.cosmere.surgebinding.common.items.NightbloodItem;
import leaf.cosmere.surgebinding.common.registries.SurgebindingLootFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetupShardDataLootFunction extends LootItemConditionalFunction
{
	protected SetupShardDataLootFunction(LootItemCondition[] pPredicates)
	{
		super(pPredicates);
	}

	@Override
	protected ItemStack run(ItemStack pStack, LootContext pContext)
	{
		if(pStack.getItem() instanceof HonorbladeItem shard)
		{
			shard.buildData(pStack, shard.getOrder(pStack), false, null);
			return pStack;
		}
		else if(pStack.getItem() instanceof NightbloodItem nightblood)
		{
			return pStack;
		}
		else if(pStack.getItem() instanceof IShardItem shard)
		{
			shard.randomizedLootData(pStack);
			return pStack;
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
