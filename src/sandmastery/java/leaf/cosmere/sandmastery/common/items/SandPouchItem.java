/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.items;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.items.ChargeableItemBase;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.entities.SandProjectile;
import leaf.cosmere.sandmastery.common.items.sandpouch.SandPouchContainerMenu;
import leaf.cosmere.sandmastery.common.items.sandpouch.SandPouchInventory;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SandPouchItem extends ChargeableItemBase
{
	public SandPouchItem()
	{
		super(PropTypes.Items.ONE.get());
	}

	private SandPouchInventory sandPouchInventory;

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
		IItemHandler inv = getPouchInv(itemStack);
		if (inv == null)
		{
			return res;
		}
		for (int i = 0; i < SandPouchInventory.size; i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			res += MiscHelper.getChargeFromItemStack(stack);
		}
		return res;
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

	public static IItemHandlerModifiable getPouchInv(ItemStack pouchStack)
	{
		return (IItemHandlerModifiable) pouchStack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
	}

	public void shoot(ItemStack pouch, Player player)
	{
		SpiritwebCapability.get(player).ifPresent((data) ->
		{
			int mode = data.getMode(SandmasteryManifestations.SANDMASTERY_POWERS.get(Taldain.Mastery.PROJECTILE).get());
			IItemHandlerModifiable inv = getPouchInv(pouch);
			ItemStack ammo = inv.getStackInSlot(2);
			if (ammo.getCount() > 0)
			{
				final ItemStack stackToShoot = ammo.copy().split(1);
				ammo.shrink(1);
				//shoot?

				if (!player.level().isClientSide)
				{
					AbstractArrow sandProjectile = new SandProjectile(player.level(), player, stackToShoot);
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
