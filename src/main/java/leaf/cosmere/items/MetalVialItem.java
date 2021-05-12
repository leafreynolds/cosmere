/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.CompoundNBTHelper;
import leaf.cosmere.utils.helpers.TextHelper;
import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static leaf.cosmere.constants.Constants.Strings.CONTAINED_METALS;

public class MetalVialItem extends BaseItem implements IContainsMetal
{
    final String metals_contained = "metals_contained";
    final String metal_ids = "metalIDs";
    final String metal_amounts = "metalAmounts";
    private final double MAX_METALS_COUNT = 8;

    private CompoundNBT getContainedMetalsTag(ItemStack stack)
    {
        return stack.getOrCreateChildTag(metals_contained);
    }

    @Override
    public boolean containsMetal(ItemStack stack)
    {
        return getContainedMetalsTag(stack).contains(metal_ids);
    }

    public boolean isFull(ItemStack stack)
    {
        return containedMetalCount(stack) >= MAX_METALS_COUNT;
    }

    @Nonnull
    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        //same drink time as normal potions
        return 32;
    }


    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (player.canEat(true) && containedMetalCount(player.getHeldItem(hand)) > 0)
        {
            player.setActiveHand(hand);
            return ActionResult.resultConsume(stack);
        }
        return ActionResult.resultPass(stack);
    }


    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {

        PlayerEntity playerentity = entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null;

        if (!worldIn.isRemote)
        {
            SpiritwebCapability.get(entityLiving).ifPresent(iSpiritweb ->
            {
                SpiritwebCapability spiritweb = (SpiritwebCapability) iSpiritweb;

                //for each metal in the vial

                Map<Integer, Integer> metalsInVial = getStoredMetalsMap(getContainedMetalsTag(stack));

                metalsInVial.entrySet().stream().forEach(metalInfo ->
                {
                    // MetalName x Value
                    Metals.MetalType metalType = Metals.MetalType.valueOf(metalInfo.getKey()).get();

                    //add to metal stored
                    spiritweb.METALS_INGESTED.put(metalType, spiritweb.METALS_INGESTED.get(metalType) + 9); // todo decide what value should be used for ingestion
                });

                iSpiritweb.syncToClients(null);
            });
        }


        if (playerentity == null || !playerentity.abilities.isCreativeMode)
        {
            if (stack.isEmpty())
            {
                return new ItemStack(ItemsRegistry.METAL_VIAL.get());
            }

            if (playerentity != null)
            {
                if (stack.getCount() > 1)
                {
                    //split 1 off the stack, if more than one
                    //drain that new stack
                    ItemStack splitStack = stack.split(1);
                    emptyMetals(splitStack);

                    playerentity.addItemStackToInventory(splitStack);
                }
                else
                {
                    emptyMetals(stack);
                }
            }
        }

        return stack;
    }

    public int containedMetalCount(ItemStack stack)
    {
        int count = 0;
        int[] metalAmounts = CompoundNBTHelper.getIntArray(getContainedMetalsTag(stack), metal_amounts);

        for (int metalCount : metalAmounts)
            count += metalCount;

        return count;
    }

    public void addMetals(ItemStack stack, int metalID, int count)
    {
        //todo refactor this? seems so convoluted compared to what I'm used to

        //get and add
        CompoundNBT nbt = getContainedMetalsTag(stack);

        Map<Integer, Integer> sorted = getStoredMetalsMap(nbt);

        if (sorted.containsKey(metalID))
        {
            count += sorted.get(metalID);
        }

        sorted.put(metalID, count);
        List<Integer> keys = new ArrayList<>(sorted.keySet());
        List<Integer> values = new ArrayList<>(sorted.values());

        CompoundNBTHelper.setIntArray(nbt, metal_ids, keys);
        CompoundNBTHelper.setIntArray(nbt, metal_amounts, values);
    }

    private Map<Integer, Integer> getStoredMetalsMap(CompoundNBT nbt)
    {
        int[] metalIDs = CompoundNBTHelper.getIntArray(nbt, metal_ids);
        int[] metalAmounts = CompoundNBTHelper.getIntArray(nbt, metal_amounts);

        Map<Integer, Integer> sorted = IntStream.range(0, metalIDs.length).boxed()
                .collect(Collectors.toMap(
                        i -> metalIDs[i], i -> metalAmounts[i],
                        (i, j) -> i, LinkedHashMap::new));
        return sorted;
    }

    public void emptyMetals(ItemStack stack)
    {
        CompoundNBT nbt = getContainedMetalsTag(stack);
        nbt.remove(metal_ids);
        nbt.remove(metal_amounts);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return 1 - (containedMetalCount(stack) / MAX_METALS_COUNT);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        Map<Integer, Integer> sorted = getStoredMetalsMap(getContainedMetalsTag(stack));
        tooltip.add(TextHelper.createTranslatedText(CONTAINED_METALS));
        sorted.entrySet().stream().forEach(metalInfo ->
        {
            // MetalName x Value
            String metalName = Metals.MetalType.valueOf(metalInfo.getKey()).get().name();

            String metalTranslation = String.format("item.cosmere.%s_nugget", metalName.toLowerCase());
            tooltip.add(TextHelper.createTranslatedText(metalTranslation).appendSibling(TextHelper.createText(String.format(": x%s", metalInfo.getValue()))));

        });

        tooltip.add(TextHelper.createText(String.format("%s / %s", containedMetalCount(stack), MAX_METALS_COUNT)));

    }
}
