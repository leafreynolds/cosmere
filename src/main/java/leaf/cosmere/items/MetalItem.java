/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.properties.PropTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class MetalItem extends BaseItem implements IHasMetalType
{
	private final Metals.MetalType metalType;

	public MetalItem(Metals.MetalType metalType)
	{
		super(PropTypes.Items.SIXTY_FOUR.get().tab(CosmereItemGroups.ITEMS).rarity(metalType.getRarity()));

		this.metalType = metalType;
	}


	@Override
	public Color getColour()
	{
		return metalType.getColor();
	}

	/**
	 * Returns true if the item can be used on the given entity, e.g. shears on sheep.
	 */
	public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand)
	{
		return InteractionResult.PASS;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}
}
