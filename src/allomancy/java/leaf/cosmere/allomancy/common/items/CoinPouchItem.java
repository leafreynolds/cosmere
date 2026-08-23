/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.allomancy.common.items;

import leaf.cosmere.allomancy.client.AllomancyKeybindings;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.coinpouch.CoinPouchContainerMenu;
import leaf.cosmere.allomancy.common.entities.CoinProjectile;
import leaf.cosmere.allomancy.common.manifestation.AllomancyIronSteel;
import leaf.cosmere.allomancy.common.network.packets.PlayerShootProjectileMessage;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import java.util.function.Predicate;

public class CoinPouchItem extends ProjectileWeaponItem
{
	public static final Predicate<ItemStack> SUPPORTED_PROJECTILES = (itemStack) ->
	{
		// todo: fix copper nugget tagging
		final boolean isNugget = itemStack.is(Tags.Items.NUGGETS) || itemStack.getItem() == ItemsRegistry.METAL_NUGGETS.get(Metals.MetalType.COPPER).asItem();  // don't know why copper nuggets aren't tagged
		final boolean containsMetal = itemStack.is(CosmereTags.Items.CONTAINS_METAL);
		final boolean isUncommonMetal = (itemStack.getItem() instanceof IHasMetalType metalType) && metalType.getMetalType().getRarity() != Rarity.COMMON;
		return isNugget && containsMetal && !isUncommonMetal;
	};

	public CoinPouchItem(Properties properties)
	{
		super(properties);
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
	public void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @javax.annotation.Nullable LivingEntity target)
	{
		// we have our own, do not use this one (it doesn't have the source item stack D:)
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity)
	{
		return 600;
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand)
	{
		ItemStack coinPouchStack = player.getItemInHand(interactionHand);

		//only allow opening pouch when it's in the main hand.
		if (interactionHand == InteractionHand.MAIN_HAND && player.isCrouching())
		{
			//open inventory
			if (!player.level().isClientSide && player instanceof ServerPlayer)
			{
				MenuProvider container = new SimpleMenuProvider((windowID, playerInv, plyr) -> new CoinPouchContainerMenu(windowID, playerInv, coinPouchStack), Component.translatable("item.allomancy.coin_pouch"));
				((ServerPlayer) player).openMenu(container, buf -> buf.writeBoolean(true));
			}
		}
		else if (player.level().isClientSide && AllomancyKeybindings.ALLOMANCY_STEEL_PUSH.isDown())
		{
			//assume they wanna shoot a projectile
			//so let them tell the server
			Allomancy.packetHandler().sendToServer(new PlayerShootProjectileMessage());
		}
		return InteractionResultHolder.consume(coinPouchStack);
	}

	public void shoot(Player player, ItemStack coinPouchStack)
	{
		SpiritwebCapability.get(player).ifPresent((data) ->
		{
			final AllomancyIronSteel steelManifestation = (AllomancyIronSteel) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.STEEL).get();

			if (steelManifestation.isActive(data) && steelManifestation.getMode(data) > 0)
			{
				final boolean playerCreativeMode = player.getAbilities().instabuild;
				final Holder<Enchantment> infinityEnch = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.INFINITY);
				final boolean infiniteAmmo = playerCreativeMode || EnchantmentHelper.getItemEnchantmentLevel(infinityEnch, coinPouchStack) > 0;

				final IItemHandlerModifiable bagInv = getBagInv(coinPouchStack);
				final int ammoSlot = findProjectileSlot(bagInv, coinPouchStack);
				ItemStack ammo = ammoSlot >= 0
				                 ? bagInv.getStackInSlot(ammoSlot)
				                 : (player.getAbilities().instabuild
				                    ? new ItemStack(ItemsRegistry.METAL_NUGGETS.get(Metals.MetalType.COPPER))
				                    : ItemStack.EMPTY);

				if (!ammo.isEmpty() || infiniteAmmo)
				{
					final ItemStack stackToShoot = ammo.copy().split(1);
					if (!infiniteAmmo && ammoSlot >= 0)
					{
						//stacks from a component handler are copies,
						//so take the coin out through the handler
						bagInv.extractItem(ammoSlot, 1, false);
					}
					//shoot?

					if (!player.level().isClientSide)
					{
						AbstractArrow coinProjectile = new CoinProjectile(player.level(), player, stackToShoot, coinPouchStack);
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

						player.level().addFreshEntity(coinProjectile);

						steelManifestation.trackValidEntity(data, coinProjectile);
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
			}
		});
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
			int slot = findProjectileSlot(bagInv, coinPouchStack);
			if (slot >= 0)
			{
				return bagInv.getStackInSlot(slot);
			}

			return player.getAbilities().instabuild
			       ? new ItemStack(ItemsRegistry.METAL_NUGGETS.get(Metals.MetalType.COPPER))
			       : ItemStack.EMPTY;
		}
	}

	/**
	 * @return the first slot holding a usable projectile, or -1 if there is none.
	 */
	private static int findProjectileSlot(@javax.annotation.Nullable IItemHandlerModifiable bagInv, ItemStack coinPouchStack)
	{
		if (bagInv == null || !(coinPouchStack.getItem() instanceof ProjectileWeaponItem weapon))
		{
			return -1;
		}
		Predicate<ItemStack> predicate = weapon.getSupportedHeldProjectiles();
		for (int i = 0; i < bagInv.getSlots(); ++i)
		{
			if (predicate.test(bagInv.getStackInSlot(i)))
			{
				return i;
			}
		}
		return -1;
	}

	private static IItemHandlerModifiable getBagInv(ItemStack coinPouchStack)
	{
		return (IItemHandlerModifiable) coinPouchStack.getCapability(Capabilities.ItemHandler.ITEM);
	}

	public static boolean onPickupItem(Entity entity, Player player)
	{
		//icky
		ItemEntity itemEntity = entity instanceof ItemEntity ? (ItemEntity) entity : null;
		CoinProjectile coinProjectile = entity instanceof CoinProjectile ? (CoinProjectile) entity : null;

		final boolean isItemEntity = itemEntity != null;
		final boolean isCoinProjectile = coinProjectile != null;

		if (!isItemEntity && !isCoinProjectile)
		{
			return false;
		}

		ItemStack entityStack;

		if (isItemEntity)
		{
			entityStack = itemEntity.getItem();
		}
		else //ick, why do these classes not share an item provider type
		{
			entityStack = coinProjectile.getItem();
		}

		int originalCount = entityStack.getCount();

		if (CoinPouchItem.SUPPORTED_PROJECTILES.test(entityStack))
		{
			for (int i = 0; i < player.getInventory().getContainerSize(); i++)
			{
				ItemStack bag = player.getInventory().getItem(i);
				if (!bag.isEmpty() && bag.is(AllomancyItems.COIN_POUCH.get()))
				{
					IItemHandlerModifiable bagInv = getBagInv(bag);
					if (bagInv == null)
					{
						continue;
					}

					for (int j = 0; j < bagInv.getSlots(); j++)
					{
						entityStack = bagInv.insertItem(j, entityStack, false);

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
						if (isItemEntity)
						{
							//here's what we couldn't fit (if any)
							itemEntity.setItem(entityStack);
							//do the take animation where the entity flies into the player
							player.take(itemEntity, amountTaken);
						}
						else// if (isCoinProjectile)
						{
							//coin projectiles are always stack size 1 (?) so should be fine to delete if we get here.
							coinProjectile.discard();
						}
						return true;
					}
				}
			}
		}
		return false;
	}
}
