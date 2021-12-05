/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.properties.PropTypes;
import net.minecraft.item.ItemGroup;

public class ChargeableMetalItem extends ChargeableItemBase implements IHasMetalType
{
    private final Metals.MetalType metalType;

    public ChargeableMetalItem(Metals.MetalType metalType, ItemGroup group)
    {
        super(PropTypes.Items.ONE.get().rarity(metalType.getRarity()).group(group));

        this.metalType = metalType;
    }

    @Override
    public Metals.MetalType getMetalType()
    {
        return this.metalType;
    }
}
