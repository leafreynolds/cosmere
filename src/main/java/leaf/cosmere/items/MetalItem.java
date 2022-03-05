/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.properties.PropTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

public class MetalItem extends BaseItem implements IHasMetalType
{
    private final Metals.MetalType metalType;

    public MetalItem(Metals.MetalType metalType)
    {
        super(PropTypes.Items.SIXTY_FOUR.get().tab(CosmereItemGroups.ITEMS).rarity(metalType.getRarity()));

        this.metalType = metalType;
    }


    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand)
    {
        return ActionResultType.PASS;
    }

    @Override
    public Metals.MetalType getMetalType()
    {
        return this.metalType;
    }
}
