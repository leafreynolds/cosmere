/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.common.charge.IChargeable;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;
import leaf.cosmere.feruchemy.common.registries.FeruchemyLootFunctions;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class InvestMetalmindLootFunction extends LootItemConditionalFunction
{
	public static final MapCodec<InvestMetalmindLootFunction> CODEC = RecordCodecBuilder.mapCodec(
			inst -> commonFields(inst).apply(inst, InvestMetalmindLootFunction::new));

	protected InvestMetalmindLootFunction(List<LootItemCondition> conditionsIn)
	{
		super(conditionsIn);
	}

	@Override
	public LootItemFunctionType<InvestMetalmindLootFunction> getType()
	{
		return FeruchemyLootFunctions.INVEST_METALMIND.get();
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext lootContext)
	{
		if (!(stack.getItem() instanceof IChargeable))
		{
			return stack;
		}

		ChargeableMetalCurioItem item = (ChargeableMetalCurioItem) stack.getItem();
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
}
