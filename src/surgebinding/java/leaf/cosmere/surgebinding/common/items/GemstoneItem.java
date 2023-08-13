/*
 * File updated ~ 27 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.IHasGemType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.charge.ItemChargeHelper;
import leaf.cosmere.common.itemgroups.CosmereItemGroups;
import leaf.cosmere.common.items.ChargeableItemBase;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.registries.SurgebindingDimensions;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class GemstoneItem extends ChargeableItemBase implements IHasGemType
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
	public Roshar.Gemstone getGemType()
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
		if (entityItem.level.dimension().equals(SurgebindingDimensions.ROSHAR_DIM_KEY))
		{
			if (entityItem.level.isRainingAt(entityItem.blockPosition()) && entityItem.level.isThundering())
			{
				if (getCharge(stack) < getMaxCharge(stack))
				{
					//gemstones charge faster in the world
					this.increaseCurrentCharge(stack, 5);
				}
			}
		}
		return super.onEntityItemUpdate(stack, entityItem);
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected)
	{
		if (!pLevel.dimension().equals(SurgebindingDimensions.ROSHAR_DIM_KEY))
		{
			return;
		}

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

			boolean hasAnySurgebinding = SurgebindingManifestations.SURGEBINDING_POWERS.values().stream().anyMatch((manifestation -> spiritweb.hasManifestation(manifestation.getManifestation())));

			if (!hasAnySurgebinding)
			{
				return;
			}

			final int charge = getCharge(itemStack);

			SurgebindingSpiritwebSubmodule sb = (SurgebindingSpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);

			int playerStormlight = sb.getStormlight();

			final int maxPlayerStormlight = SurgebindingConfigs.SERVER.PLAYER_MAX_STORMLIGHT.get();

			//Get stormlight from gems
			if (!pPlayer.isCrouching())
			{
				if (charge + playerStormlight > maxPlayerStormlight)
				{
					sb.adjustStormlight((maxPlayerStormlight - playerStormlight), true);
					ItemChargeHelper.requestChargeExact(itemStack, pPlayer, ((charge + playerStormlight) - maxPlayerStormlight), true);
				}
				else
				{
					sb.adjustStormlight(charge, true);
					ItemChargeHelper.requestChargeExact(itemStack, pPlayer, 0, true);
				}
			}
			//put remaining stormlight into gem.
			else
			{
				if (playerStormlight > 0)
				{
					if ((charge + playerStormlight) > getMaxCharge(itemStack))
					{
						sb.adjustStormlight(-(getMaxCharge(itemStack) - charge), true);
						setCharge(itemStack, getMaxCharge(itemStack));
					}
					else
					{
						sb.adjustStormlight(-playerStormlight, true);
						setCharge(itemStack, (short) (playerStormlight + charge));
					}
				}
			}
		});


		return InteractionResultHolder.consume(itemStack);
	}
}
