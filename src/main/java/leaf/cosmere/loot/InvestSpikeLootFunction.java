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
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.math.MathHelper;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class InvestSpikeLootFunction extends LootFunction
{

    protected InvestSpikeLootFunction(ILootCondition[] conditionsIn)
    {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext lootContext)
    {
        if (!(stack.getItem() instanceof HemalurgicSpikeItem))
        {
            return stack;
        }

        HemalurgicSpikeItem item = (HemalurgicSpikeItem) stack.getItem();
        final Metals.MetalType spikeMetalType = item.getMetalType();

        if (!spikeMetalType.hasHemalurgicEffect())
        {
            return stack;
        }

        Collection<Metals.MetalType> hemalurgyStealWhitelist = spikeMetalType.getHemalurgyStealWhitelist();

        Optional<Metals.MetalType> stealType = hemalurgyStealWhitelist
                .stream()
                .filter(metalType -> metalType.hasAssociatedManifestation())
                .skip(lootContext.getRandom().nextInt(hemalurgyStealWhitelist.size()))
                .findFirst();


        final float strengthLevel = MathHelper.clamp(5 + lootContext.getLuck(), 1, 10);

        switch (spikeMetalType)
        {
            case IRON:
                // add strength
                item.Invest(stack,spikeMetalType, strengthLevel, UUID.randomUUID());

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

    @Override
    public LootFunctionType getType()
    {
        return LootFunctionRegistry.INVEST_SPIKE;
    }

    public static class Serializer extends LootFunction.Serializer<InvestSpikeLootFunction>
    {
        @Override
        public InvestSpikeLootFunction deserialize(JsonObject jsonObject, JsonDeserializationContext deserializationContext, ILootCondition[] lootConditions)
        {
            return new InvestSpikeLootFunction(lootConditions);
        }
    }
}