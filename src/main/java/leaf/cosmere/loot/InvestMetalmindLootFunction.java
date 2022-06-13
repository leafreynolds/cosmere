/*
 * File created ~ 26 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import leaf.cosmere.charge.IChargeable;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.MetalmindItem;
import leaf.cosmere.registry.LootFunctionRegistry;
import leaf.cosmere.utils.helpers.StackNBTHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class InvestMetalmindLootFunction extends LootItemConditionalFunction
{

	protected InvestMetalmindLootFunction(LootItemCondition[] conditionsIn)
	{
		super(conditionsIn);
	}

	@Override
	public LootItemFunctionType getType()
	{
		return LootFunctionRegistry.INVEST_METALMIND;
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext lootContext)
	{
		if (!(stack.getItem() instanceof IChargeable))
		{
			return stack;
		}

		MetalmindItem item = (MetalmindItem) stack.getItem();
		final Metals.MetalType metalType = item.getMetalType();

		if (!metalType.hasFeruchemicalEffect() || metalType == Metals.MetalType.NICROSIL)
		{
			return stack;
		}

		int maxCharge = item.getMaxCharge(stack);

		final int strengthBeforeLuck = 2 + lootContext.getRandom().nextInt(8);

		final float strengthLevel = Mth.clamp(strengthBeforeLuck + lootContext.getLuck(), 1, 10);

		item.setCharge(stack, (int) Mth.lerp(strengthLevel / 10, 1, maxCharge));

		StackNBTHelper.setUuid(stack, Constants.NBT.ATTUNED_PLAYER, Constants.NBT.UNKEYED_UUID);
		StackNBTHelper.setString(stack, Constants.NBT.ATTUNED_PLAYER_NAME, "Unkeyed"); // todo translation

		return stack;
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<InvestMetalmindLootFunction>
	{
		@Override
		public InvestMetalmindLootFunction deserialize(JsonObject jsonObject, JsonDeserializationContext deserializationContext, LootItemCondition[] lootConditions)
		{
			return new InvestMetalmindLootFunction(lootConditions);
		}
	}
}