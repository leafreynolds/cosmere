/*
 * File updated ~ 13 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.items;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.items.ChargeableItemBase;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import leaf.cosmere.sandmastery.common.entities.SandProjectile;
import leaf.cosmere.sandmastery.common.itemgroups.SandmasteryItemGroups;
import leaf.cosmere.sandmastery.common.items.sandpouch.ISandPouchItemHandler;
import leaf.cosmere.sandmastery.common.items.sandpouch.SandpouchItemHandler;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import leaf.cosmere.sandmastery.common.items.sandpouch.SandPouchContainerMenu;
import leaf.cosmere.sandmastery.common.items.sandpouch.SandPouchInventory;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class SandPouchItem extends ChargeableItemBase
{
	public static Capability<ISandPouchItemHandler> POUCH_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});

	public SandPouchItem()
	{
		super(PropTypes.Items.ONE.get().tab(SandmasteryItemGroups.ITEMS));
	}

	private SandPouchInventory sandPouchInventory;

	public static final Predicate<ItemStack> SUPPORTED_ITEMS = (itemStack) ->
	{
		if (itemStack.getItem() == SandmasteryBlocksRegistry.TALDAIN_BLACK_SAND.asItem())
		{
			return true;
		}
		return itemStack.getItem() == SandmasteryBlocksRegistry.TALDAIN_BLACK_SAND_LAYER.asItem();
	};

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected)
	{
		MiscHelper.chargeItemFromInvestiture(pStack, pLevel, pEntity, getMaxCharge(pStack));
	}

	@Override
	public int getCharge(ItemStack itemStack)
	{
		int itemCharge = StackNBTHelper.getInt(itemStack, Constants.NBT.CHARGE_LEVEL, 0);
		return itemCharge * itemStack.getCount();
	}

	@Override
	public int getMaxCharge(ItemStack itemStack)
	{
		int res = 0;
		ISandPouchItemHandler inv = getPouchInv(itemStack);
		if (inv == null)
		{
			return res;
		}
		return inv.getLayers() * SandmasteryConfigs.SERVER.SAND_LAYER_CHARGE_CAPACITY.get();
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> stacks)
	{
		if (allowedIn(tab))
		{
			stacks.add(new ItemStack(this));
		}
	}

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
			if (!player.level.isClientSide() && player instanceof ServerPlayer)
			{
				MenuProvider container = new SimpleMenuProvider((windowID, playerInv, plyer) ->
						new SandPouchContainerMenu(windowID, playerInv, pouchStack), pouchStack.getHoverName());
				NetworkHooks.openScreen((ServerPlayer) player, container, buf -> buf.writeBoolean(true));
			}
		}
		return InteractionResultHolder.consume(pouchStack);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag oldCapNbt)
	{
		this.sandPouchInventory = new SandPouchInventory();
		if (oldCapNbt != null)
		{
			sandPouchInventory.deserializeNBT(oldCapNbt); // todo check if this breaks things?
		}
		return this.sandPouchInventory;
	}

	public static ISandPouchItemHandler getPouchInv(ItemStack pouchStack)
	{
		return pouchStack.getCapability(POUCH_HANDLER).orElse(null);
	}

	public void shoot(ItemStack pouch, Player player)
	{
		SpiritwebCapability.get(player).ifPresent((data) ->
		{
			int mode = data.getMode(SandmasteryManifestations.SANDMASTERY_POWERS.get(Taldain.Mastery.PROJECTILE).get());
			ISandPouchItemHandler inv = getPouchInv(pouch);
			ItemStack ammo = inv.getStackInSlot(2);
			if (ammo.getCount() > 0)
			{
				final ItemStack stackToShoot = ammo.copy().split(1);
				ammo.shrink(1);
				//shoot?

				if (!player.level.isClientSide)
				{
					AbstractArrow sandProjectile = new SandProjectile(player.level, player, stackToShoot);
					sandProjectile.setCritArrow(true);
					sandProjectile.shootFromRotation(
							player,
							player.getXRot(),
							player.getYRot(),
							0.0F,
							mode * 0.5F + 3F,
							1.0F);

					sandProjectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

					player.level.addFreshEntity(sandProjectile);
				}

				player.level.playSound(
						null,
						player.getX(),
						player.getY(),
						player.getZ(),
						SoundEvents.ARROW_SHOOT,
						SoundSource.PLAYERS,
						1.0F,
						1.0F / (player.level.getRandom().nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);

			}
		});
	}
}
