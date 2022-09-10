/*
 * File created ~ 9 - 4 - 2022 ~ Leaf
 * The Gemstones of Roshar
 * https://coppermind.net/wiki/Gemstone
 */

package leaf.cosmere.items.gems;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Roshar;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.items.ChargeableItemBase;
import leaf.cosmere.properties.PropTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class GemstoneItem extends ChargeableItemBase implements IHasGemstoneType
{
	private final Roshar.Gemstone gemstone;
	private final Roshar.GemSize gemSize;

	public GemstoneItem(Roshar.Gemstone gemstone, Roshar.GemSize gemSize)
	{
		super(PropTypes.Items.SIXTY_FOUR.get().tab(CosmereItemGroups.ITEMS).rarity(Rarity.UNCOMMON));
		this.gemstone = gemstone;
		this.gemSize = gemSize;
	}

	@Override
	public int getMaxCharge(ItemStack itemStack)
	{
		return Mth.floor(10000 * getMaxChargeModifier());
	}

	@Override
	public float getMaxChargeModifier()
	{
		return gemSize.getChargeModifier();
	}

	@Override
	public Roshar.Gemstone getType()
	{
		return gemstone;
	}

	public Roshar.GemSize getSize()
	{
		return this.gemSize;
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
		return super.onEntityItemUpdate(stack, entityItem);
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected)
	{
		if (pLevel.isRainingAt(pEntity.blockPosition()) && pLevel.isThundering())
		{
			if (pStack.getItem() instanceof GemstoneItem gemstoneItem)
			{
				gemstoneItem.increaseCurrentCharge(pStack);
			}
		}

		super.inventoryTick(pStack, pLevel, pEntity, pItemSlot, pIsSelected);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

		if (pLevel.isClientSide)
		{
			return InteractionResultHolder.pass(itemStack);
		}

		SpiritwebCapability.get(pPlayer).ifPresent(spiritweb ->
		{
			SpiritwebCapability data = (SpiritwebCapability) spiritweb;

			final int charge = getCharge(itemStack);
			int playerStormlight = data.getStormlight();
			final int maxPlayerStormlight = 1000;

			//Get stormlight from gems
			if (!pPlayer.isCrouching())
			{
				if (charge + playerStormlight > maxPlayerStormlight)
				{
					data.adjustStormlight((maxPlayerStormlight - playerStormlight), true);
					setCharge(itemStack, ((charge + playerStormlight) - maxPlayerStormlight));
				}
				else
				{
					data.adjustStormlight(charge, true);
					setCharge(itemStack, 0);
				}
			}
			//put remaining stormlight into gem.
			else
			{
				if (playerStormlight > 0)
				{
					if ((charge + playerStormlight) > getMaxCharge(itemStack))
					{
						data.adjustStormlight(-(getMaxCharge(itemStack) - charge), true);
						setCharge(itemStack, getMaxCharge(itemStack));
					}
					else
					{
						data.adjustStormlight(-playerStormlight, true);
						setCharge(itemStack, (short) (playerStormlight + charge));
					}
				}
			}
		});


		return InteractionResultHolder.consume(itemStack);
	}
}
