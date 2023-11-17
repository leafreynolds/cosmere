/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.hemalurgy.common.items.HemalurgicSpikeItem;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyLootFunctions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class InvestSpikeLootFunction extends LootItemConditionalFunction
{

	protected InvestSpikeLootFunction(LootItemCondition[] conditionsIn)
	{
		super(conditionsIn);
	}


	@Override
	public LootItemFunctionType getType()
	{
		return HemalurgyLootFunctions.INVEST_SPIKE.get();
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext lootContext)
	{
		if (!(stack.getItem() instanceof HemalurgicSpikeItem item))
		{
			return stack;
		}

		final Metals.MetalType spikeMetalType = item.getMetalType();

		if (!spikeMetalType.hasHemalurgicEffect())
		{
			return stack;
		}

		Collection<Metals.MetalType> hemalurgyStealWhitelist = spikeMetalType.getHemalurgyStealWhitelist();

		Optional<Metals.MetalType> stealType =
				hemalurgyStealWhitelist == null
				? Optional.empty()
				: hemalurgyStealWhitelist
						.stream()
						.filter(Metals.MetalType::hasAssociatedManifestation)
						.skip(lootContext.getRandom().nextInt(hemalurgyStealWhitelist.size()))
						.findFirst();

		//todo metal types that aren't able to store powers.
		if (stealType.isEmpty())
		{
			CosmereAPI.logger.error(spikeMetalType + " has empty Stealtype on trying to invest spike loot");
			return stack;
		}

		final float strengthLevel = Mth.clamp(5 + lootContext.getLuck(), 1, 10);
		Manifestation allomancyMani = CosmereAPI.manifestationRegistry().getValue(new ResourceLocation("allomancy", stealType.get().getName()));
		Manifestation feruchemyMani = CosmereAPI.manifestationRegistry().getValue(new ResourceLocation("feruchemy", stealType.get().getName()));

		switch (spikeMetalType)
		{
			//todo metal types that aren't able to store powers.
			/*
			case IRON:
				// add strength
				item.Invest(stack, spikeMetalType, strengthLevel, UUID.randomUUID());

				break;
			case TIN:
			case COPPER:
			case CHROMIUM:
			{
				item.Invest(stack, spikeMetalType, strengthLevel / 10, UUID.randomUUID());
			}
			break;*/
			//steals allomantic abilities
			case STEEL:
			case BRONZE:
			case CADMIUM:
			case ELECTRUM:
			{
				if (!stealType.isPresent())
				{
					return stack;
				}
				item.Invest(stack, allomancyMani, strengthLevel, UUID.randomUUID());
			}
			break;
			//steals feruchemical abilities
			case PEWTER:
			case BRASS:
			case BENDALLOY:
			case GOLD:
			{
				if (!stealType.isPresent())
				{
					return stack;
				}

				item.Invest(stack, feruchemyMani, strengthLevel, UUID.randomUUID());
			}
			break;
			case ATIUM:

				if (!stealType.isPresent())
				{
					return stack;
				}

				boolean isAllomancy = lootContext.getRandom().nextBoolean();
				Manifestation manifestation;
				if (isAllomancy)
				{
					manifestation = allomancyMani;
				}
				else
				{
					manifestation = feruchemyMani;
				}

				if (manifestation != null)
				{
					item.Invest(stack, manifestation, strengthLevel, UUID.randomUUID());
				}

				break;
		}

		return stack;
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<InvestSpikeLootFunction>
	{
		@Override
		public InvestSpikeLootFunction deserialize(JsonObject jsonObject, JsonDeserializationContext deserializationContext, LootItemCondition[] lootConditions)
		{
			return new InvestSpikeLootFunction(lootConditions);
		}
	}
}