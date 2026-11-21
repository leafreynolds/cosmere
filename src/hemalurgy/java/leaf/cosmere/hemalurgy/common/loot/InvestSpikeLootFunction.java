/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.hemalurgy.common.capabilities.HemalurgyItemCapabilities;
import leaf.cosmere.hemalurgy.common.items.IHemalurgicInfo;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyLootFunctions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InvestSpikeLootFunction extends LootItemConditionalFunction
{
	public static final MapCodec<InvestSpikeLootFunction> CODEC = RecordCodecBuilder.mapCodec(
			inst -> commonFields(inst).apply(inst, InvestSpikeLootFunction::new));

	protected InvestSpikeLootFunction(List<LootItemCondition> conditionsIn)
	{
		super(conditionsIn);
	}


	@Override
	public LootItemFunctionType<InvestSpikeLootFunction> getType()
	{
		return HemalurgyLootFunctions.INVEST_SPIKE.get();
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext lootContext)
	{
		final IHemalurgicInfo item = HemalurgyItemCapabilities.getSpike(stack);
		if (item == null)
		{
			return stack;
		}

		final Metals.MetalType spikeMetalType = item.getSpikeMetalType(stack);

		if (spikeMetalType == null || !spikeMetalType.hasHemalurgicEffect())
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
		Manifestation allomancyMani = CosmereAPI.manifestationRegistry().get(ResourceLocation.fromNamespaceAndPath("allomancy", stealType.get().getName()));
		Manifestation feruchemyMani = CosmereAPI.manifestationRegistry().get(ResourceLocation.fromNamespaceAndPath("feruchemy", stealType.get().getName()));

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
}
