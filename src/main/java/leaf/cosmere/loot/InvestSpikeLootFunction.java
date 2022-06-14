/*
 * File created ~ 26 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.curio.HemalurgicSpikeItem;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.LootFunctionRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
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
		return LootFunctionRegistry.INVEST_SPIKE;
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
						.filter(metalType -> metalType.hasAssociatedManifestation())
						.skip(lootContext.getRandom().nextInt(hemalurgyStealWhitelist.size()))
						.findFirst();


		final float strengthLevel = Mth.clamp(5 + lootContext.getLuck(), 1, 10);

		switch (spikeMetalType)
		{
			case IRON:
				// add strength
				item.Invest(stack, spikeMetalType, strengthLevel, UUID.randomUUID());

				break;
			case TIN:
			case COPPER:
			case CHROMIUM:
			{
				item.Invest(stack,spikeMetalType, strengthLevel / 10,UUID.randomUUID() );
			}
			break;
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

				AManifestation allomancyMani = ManifestationRegistry.ALLOMANCY_POWERS.get(stealType.get()).get();
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

				AManifestation feruchemyMani = ManifestationRegistry.FERUCHEMY_POWERS.get(stealType.get()).get();

				item.Invest(stack, feruchemyMani, strengthLevel, UUID.randomUUID());
			}
			break;
			case ATIUM:

				if (!stealType.isPresent())
				{
					return stack;
				}

				boolean isAllomancy = lootContext.getRandom().nextBoolean();
				AManifestation manifestation;
				if (isAllomancy)
				{
					manifestation = ManifestationRegistry.ALLOMANCY_POWERS.get(stealType.get()).get();
				}
				else
				{
					manifestation = ManifestationRegistry.FERUCHEMY_POWERS.get(stealType.get()).get();
				}

				item.Invest(stack, manifestation, strengthLevel, UUID.randomUUID());

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