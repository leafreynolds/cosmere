/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.items;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.properties.PropTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MetalItem extends BaseItem implements IHasMetalType
{
	private final Metals.MetalType metalType;

	public MetalItem(Metals.MetalType metalType)
	{
		super(PropTypes.Items.SIXTY_FOUR.get().rarity(metalType.getRarity()));

		this.metalType = metalType;
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
