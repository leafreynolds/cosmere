package leaf.cosmere.surgebinding.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import leaf.cosmere.surgebinding.common.items.IRadiantShardItem;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import leaf.cosmere.surgebinding.common.registries.SurgebindingLootFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class SetupShardDataLootFunction extends LootItemConditionalFunction
{
	public static final MapCodec<SetupShardDataLootFunction> CODEC = RecordCodecBuilder.mapCodec(
			inst -> commonFields(inst).apply(inst, SetupShardDataLootFunction::new));

	protected SetupShardDataLootFunction(List<LootItemCondition> pPredicates)
	{
		super(pPredicates);
	}

	@Override
	protected ItemStack run(ItemStack pStack, LootContext pContext)
	{
		if (pStack.getItem() instanceof IRadiantShardItem shard)
		{
			int chance = pContext.getRandom().nextInt(0, 40);

			if (chance <= 3)
			{
				pStack = new ItemStack(SurgebindingItems.SHARDBLADE);
				shard.randomizedLootData(pStack);
				return pStack;
			}
			else if (chance <= 5)
			{
				pStack = new ItemStack(SurgebindingItems.SHARDPLATE);
				shard.randomizedLootData(pStack);
				return pStack;
			}
			else if (chance == 39)
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
	public LootItemFunctionType<SetupShardDataLootFunction> getType()
	{
		return SurgebindingLootFunctions.SETUP_DATA.get();
	}
}
