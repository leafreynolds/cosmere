/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.common.cap.item.CosmereItemCapabilities;
import leaf.cosmere.common.charge.IChargeable;
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
		final IChargeable chargeable = CosmereItemCapabilities.getChargeable(stack);

		if (chargeable == null)
		{
			return stack;
		}

		final Metals.MetalType metalType = chargeable.getChargeMetalType(stack);

		//chargeables that aren't made of a metal at all (gemstones, sand jars) never get invested
		if (metalType == null || !metalType.hasFeruchemicalEffect() || metalType == Metals.MetalType.NICROSIL)
		{
			return stack;
		}

		int maxCharge = chargeable.getMaxCharge(stack);

		final int strengthBeforeLuck = 2 + lootContext.getRandom().nextInt(8);

		final float strengthLevel = Mth.clamp(strengthBeforeLuck + lootContext.getLuck(), 1, 10);

		chargeable.setCharge(stack, (int) Mth.lerp(strengthLevel / 10, 1, maxCharge));

		StackNBTHelper.setUuid(stack, Constants.NBT.ATTUNED_PLAYER, Constants.NBT.UNKEYED_UUID);
		StackNBTHelper.setString(stack, Constants.NBT.ATTUNED_PLAYER_NAME, "Unkeyed"); // todo translation

		return stack;
	}
}
