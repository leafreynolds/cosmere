/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class MetalNuggetItem extends MetalItem
{
    public MetalNuggetItem(Metals.MetalType metalType)
    {
        super(metalType);
    }


    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);

        //todo convert to shavings

        SpiritwebCapability.get(playerIn).ifPresent(iSpiritweb ->
        {
            SpiritwebCapability spiritweb = (SpiritwebCapability) iSpiritweb;

            //add to metal stored
            Integer metalIngested = spiritweb.METALS_INGESTED.get(getMetalType());
            spiritweb.METALS_INGESTED.put(getMetalType(),metalIngested + 9); // todo decide what value should be used for ingestion

            itemstack.shrink(1);

        });



        return ActionResult.resultConsume(itemstack);
    }
}
