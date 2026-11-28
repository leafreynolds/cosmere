/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.items;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.items.ChargeableItemBase;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.diem.trial.DiemTrials;
import leaf.cosmere.sandmastery.common.entities.SandProjectile;
import leaf.cosmere.sandmastery.common.items.sandpouch.SandPouchContainerMenu;
import leaf.cosmere.sandmastery.common.items.sandpouch.SandpouchItemHandler;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.registries.SandmasteryDataComponents;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import leaf.cosmere.sandmastery.common.utils.SandmasteryConstants;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SandPouchItem extends ChargeableItemBase
{
	public SandPouchItem()
	{
		super(PropTypes.Items.ONE.get());
	}

	public static final Predicate<ItemStack> SUPPORTED_ITEMS = (itemStack) ->
	{
		return itemStack.getItem() == SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.asItem() ||
				itemStack.getItem() == SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.asItem() ||
				itemStack.getItem() == SandmasteryBlocks.TALDAIN_BLACK_SAND.asItem() ||
				itemStack.getItem() == SandmasteryBlocks.TALDAIN_WHITE_SAND.asItem();
	};

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected)
	{
		if (pLevel.isClientSide() || !MiscHelper.checkIfNearbyInvestiture((ServerLevel) pLevel, pEntity.blockPosition(), false))
		{
			return;
		}

		int charged = pStack.getOrDefault(SandmasteryDataComponents.CHARGED_SAND_LAYERS, 0);
		if (charged < totalLayers(pStack))
		{
			pStack.set(SandmasteryDataComponents.CHARGED_SAND_LAYERS, charged + 1);
		}
	}

	private static final int CHARGE_PER_LAYER = SandmasteryConstants.CHARGE_PER_SAND_LAYER;

	private static int totalLayers(ItemStack itemStack)
	{
		return itemStack.getOrDefault(SandmasteryDataComponents.SAND_LAYERS, 0);
	}

	@Override
	public int getCharge(ItemStack itemStack)
	{
		return Math.min(itemStack.getOrDefault(SandmasteryDataComponents.CHARGED_SAND_LAYERS, 0), totalLayers(itemStack)) * CHARGE_PER_LAYER;
	}

	@Override 
	public void setCharge(ItemStack itemStack, int chargeLevel)
	{
		int layers = Mth.clamp(chargeLevel / CHARGE_PER_LAYER, 0, totalLayers(itemStack));
		itemStack.set(SandmasteryDataComponents.CHARGED_SAND_LAYERS, layers);
	}

	@Override
	public int getMaxCharge(ItemStack itemStack)
	{
		return totalLayers(itemStack) * CHARGE_PER_LAYER;
	}

	/*@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> stacks)
	{
		if (allowedIn(tab))
		{
			stacks.add(new ItemStack(this));
		}
	}*/

	@Override
	public boolean isFoil(@NotNull ItemStack stack)
	{
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand)
	{
		ItemStack pouchStack = player.getItemInHand(interactionHand);
		if (interactionHand == InteractionHand.MAIN_HAND)
		{
			if (!player.level().isClientSide() && player instanceof ServerPlayer)
			{
				MenuProvider container = new SimpleMenuProvider((windowID, playerInv, plyer) ->
						new SandPouchContainerMenu(windowID, playerInv, pouchStack), pouchStack.getHoverName());
				((ServerPlayer) player).openMenu(container, buf -> buf.writeBoolean(true));
			}
		}
		return InteractionResultHolder.consume(pouchStack);
	}

	public static IItemHandlerModifiable getPouchInv(ItemStack pouchStack)
	{
		return (IItemHandlerModifiable) pouchStack.getCapability(Capabilities.ItemHandler.ITEM);
	}

	public void shoot(ItemStack pouch, Player player)
	{
		SpiritwebCapability.get(player).ifPresent((data) ->
		{
			IItemHandlerModifiable inv = getPouchInv(pouch);
			if (inv == null)
			{
				return;
			}
			final ItemStack stackToShoot = inv.extractItem(SandpouchItemHandler.SLOT_LAYERS, 1, false);
			if (!stackToShoot.isEmpty())
			{
				//shoot?

				if (!player.level().isClientSide)
				{
					AbstractArrow sandProjectile = new SandProjectile(player.level(), player, stackToShoot, pouch);
					sandProjectile.setCritArrow(true);
					sandProjectile.shootFromRotation(
							player,
							player.getXRot(),
							player.getYRot(),
							0.0F,
							3F,
							1.0F);

					sandProjectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

					player.level().addFreshEntity(sandProjectile);

					if (player instanceof ServerPlayer serverPlayer)
					{
						DiemTrials.onShot(serverPlayer);
					}
				}

				player.level().playSound(
						null,
						player.getX(),
						player.getY(),
						player.getZ(),
						SoundEvents.ARROW_SHOOT,
						SoundSource.PLAYERS,
						1.0F,
						1.0F / (player.level().getRandom().nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);

			}
		});
	}
}
