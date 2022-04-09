/*
 * File created ~ 9 - 4 - 2022 ~ Leaf
 * The Gemstones of Roshar
 * https://coppermind.net/wiki/Polestone
 */

package leaf.cosmere.items.gems;

import leaf.cosmere.constants.Roshar;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.items.ChargeableItemBase;
import leaf.cosmere.properties.PropTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PolestoneItem extends ChargeableItemBase implements IHasPolestoneType
{
	private final Roshar.Polestone polestone;
	private final Roshar.GemSize gemSize;

	public PolestoneItem(Roshar.Polestone polestone, Roshar.GemSize gemSize)
	{
		super(PropTypes.Items.SIXTY_FOUR.get().tab(CosmereItemGroups.ITEMS).rarity(Rarity.UNCOMMON));
		this.polestone = polestone;
		this.gemSize = gemSize;
	}

	@Override
	public int getMaxCharge(ItemStack itemStack)
	{
		return MathHelper.floor(10000 * getMaxChargeModifier());
	}

	@Override
	public float getMaxChargeModifier()
	{
		return gemSize.getChargeModifier();
	}

	@Override
	public Roshar.Polestone getPolestoneType()
	{
		return polestone;
	}

	public Roshar.GemSize getSize()
	{
		return this.gemSize;
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world)
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entityItem)
	{
		if (entityItem.level.isRainingAt(entityItem.blockPosition()) && entityItem.level.isThundering())
		{
			if (getCharge(stack) < getMaxCharge(stack))
			{
				//gemstones charge faster in the world
				this.increaseCurrentCharge(stack, 5);
			}
		}
		return super.onEntityItemUpdate(stack,entityItem);
	}

	@Override
	public void inventoryTick(ItemStack pStack, World pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected)
	{
		if (pLevel.isRainingAt(pEntity.blockPosition()) && pLevel.isThundering())
		{
			if (pStack.getItem() instanceof PolestoneItem)
			{
				PolestoneItem polestoneItem = (PolestoneItem) pStack.getItem();
				polestoneItem.increaseCurrentCharge(pStack);
			}
		}

		super.inventoryTick(pStack, pLevel, pEntity, pItemSlot, pIsSelected);
	}
}
