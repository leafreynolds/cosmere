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
import leaf.cosmere.items.curio.HemalurgicSpikeItem;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.LootFunctionRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.StackNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.math.MathHelper;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class InvestMetalmindLootFunction extends LootFunction
{

    protected InvestMetalmindLootFunction(ILootCondition[] conditionsIn)
    {
        super(conditionsIn);
    }

    @Override
    public LootFunctionType getType()
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

        if (!metalType.hasFeruchemicalEffect())
        {
            return stack;
        }

        int maxCharge = item.getMaxCharge(stack);

        final int strengthBeforeLuck = 2 + lootContext.getRandom().nextInt(8);

        final float strengthLevel = MathHelper.clamp(strengthBeforeLuck + lootContext.getLuck(), 1, 10);

        item.setCharge(stack,(int) MathHelper.lerp(strengthLevel / 10,1,maxCharge));

        StackNBTHelper.setUuid(stack, Constants.NBT.ATTUNED_PLAYER, Constants.NBT.UNSEALED_UUID);
        StackNBTHelper.setString(stack, Constants.NBT.ATTUNED_PLAYER_NAME, "Unsealed"); // todo translation

        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<InvestMetalmindLootFunction>
    {
        @Override
        public InvestMetalmindLootFunction deserialize(JsonObject jsonObject, JsonDeserializationContext deserializationContext, ILootCondition[] lootConditions)
        {
            return new InvestMetalmindLootFunction(lootConditions);
        }
    }
}