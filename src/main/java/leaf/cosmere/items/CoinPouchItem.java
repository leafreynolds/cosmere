/*
 * File created ~ 21 - 5 - 2022 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.containers.coinpouch.CoinPouchContainer;
import leaf.cosmere.containers.coinpouch.CoinPouchInventory;
import leaf.cosmere.entities.CoinProjectile;
import leaf.cosmere.manifestation.allomancy.AllomancyIronSteel;
import leaf.cosmere.network.Network;
import leaf.cosmere.network.packets.PlayerShootProjectileMessage;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.KeybindingRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class CoinPouchItem extends ProjectileWeaponItem
{
	public static final Predicate<ItemStack> SUPPORTED_PROJECTILES = (itemStack) -> itemStack.is(Tags.Items.NUGGETS) && AllomancyIronSteel.containsMetal(itemStack.getItem().getRegistryName().getPath());

	public CoinPouchItem(Item.Properties p_40660_)
	{
		super(p_40660_);
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles()
	{
		return SUPPORTED_PROJECTILES;
	}

	@Override
	public int getDefaultProjectileRange()
	{
		return 8;
	}

	@Override
	public int getUseDuration(ItemStack itemStack)
	{
		return 600;
	}


	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand)
	{
		ItemStack coinPouchStack = player.getItemInHand(interactionHand);

		if (player.isCrouching())
		{
			//open inventory
			if (!player.level.isClientSide && player instanceof ServerPlayer)
			{
				MenuProvider container = new SimpleMenuProvider((windowID, playerInv, plyr) -> new CoinPouchContainer(windowID, playerInv, coinPouchStack), coinPouchStack.getHoverName());
				NetworkHooks.openGui((ServerPlayer) player, container, buf -> buf.writeBoolean(interactionHand == InteractionHand.MAIN_HAND));
			}
		}
		else if (player.level.isClientSide && KeybindingRegistry.ALLOMANCY_PUSH.isDown())
		{
			//assume they wanna shoot a projectile
			//so let them tell the server
			Network.sendToServer(new PlayerShootProjectileMessage());
		}
		return InteractionResultHolder.consume(coinPouchStack);
	}

	public void shoot(Player player, ItemStack coinPouchStack)
	{
		SpiritwebCapability.get(player).ifPresent((data) ->
		{
			final AllomancyIronSteel steelManifestation = (AllomancyIronSteel) ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.STEEL).get();

			if (steelManifestation.isActive(data))
			{
				final boolean playerCreativeMode = player.getAbilities().instabuild;
				final boolean infiniteAmmo = playerCreativeMode || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, coinPouchStack) > 0;

				ItemStack ammo = getProjectile(player, coinPouchStack);

				if (!ammo.isEmpty() || infiniteAmmo)
				{
					final ItemStack stackToShoot = ammo.copy().split(1);
					if (!infiniteAmmo)
					{
						ammo.shrink(1);
					}
					//shoot?

					if (!player.level.isClientSide)
					{
						AbstractArrow coinProjectile = new CoinProjectile(player.level, player, stackToShoot);
						coinProjectile.setCritArrow(true);
						coinProjectile.shootFromRotation(
								player,
								player.getXRot(),
								player.getYRot(),
								0.0F,
								3.0F,
								1.0F);

						coinProjectile.pickup = infiniteAmmo
						                       ? AbstractArrow.Pickup.DISALLOWED
						                       : AbstractArrow.Pickup.ALLOWED;

						player.level.addFreshEntity(coinProjectile);

						steelManifestation.trackValidEntity(data, coinProjectile);
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
			}
		});
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag oldCapNbt)
	{
		return new CoinPouchInventory();
	}


	public ItemStack getProjectile(Player player, ItemStack coinPouchStack)
	{
		if (!(coinPouchStack.getItem() instanceof ProjectileWeaponItem))
		{
			return ItemStack.EMPTY;
		}
		else
		{
			IItemHandlerModifiable bagInv = getBagInv(coinPouchStack);
			Predicate<ItemStack> predicate = ((ProjectileWeaponItem) coinPouchStack.getItem()).getSupportedHeldProjectiles();

			for (int i = 0; i < bagInv.getSlots(); ++i)
			{
				ItemStack stackInSlot = bagInv.getStackInSlot(i);
				if (predicate.test(stackInSlot))
				{
					return net.minecraftforge.common.ForgeHooks.getProjectile(player, coinPouchStack, stackInSlot);
				}
			}

			return net.minecraftforge.common.ForgeHooks.getProjectile(
					player,
					coinPouchStack,
					player.getAbilities().instabuild
					? new ItemStack(Metals.MetalType.COPPER.getNuggetItem())
					: ItemStack.EMPTY);
		}
	}

	@NotNull
	private static IItemHandlerModifiable getBagInv(ItemStack coinPouchStack)
	{
		return (IItemHandlerModifiable) coinPouchStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
	}

	public static boolean onPickupItem(ItemEntity entity, Player player)
	{
		ItemStack entityStack = entity.getItem();
		int originalCount = entityStack.getCount();

		if (CoinPouchItem.SUPPORTED_PROJECTILES.test(entityStack))
		{
			for (int i = 0; i < player.getInventory().getContainerSize(); i++)
			{
				ItemStack bag = player.getInventory().getItem(i);
				if (!bag.isEmpty() && bag.is(ItemsRegistry.COIN_POUCH.get()))
				{
					IItemHandlerModifiable bagInv = getBagInv(bag);

					for (int j = 0; j < bagInv.getSlots(); j++)
					{
						entityStack = bagInv.insertItem(j, entityStack,false);

						if (entityStack.isEmpty())
						{
							break;
						}
					}

					if (player.getInventory().add(-1, entityStack))
					{
						//great, we fit it all.
					}

					final int amountTaken = originalCount - entityStack.getCount();
					if (amountTaken > 0)
					{
						//here's what we couldn't fit (if any)
						entity.setItem(entityStack);
						//do the take animation where the entity flies into the player
						player.take(entity, amountTaken);
						return true;
					}
				}
			}
		}
		return false;
	}
}
