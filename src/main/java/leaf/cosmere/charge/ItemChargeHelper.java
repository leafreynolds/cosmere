/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to Vazkii and their Mod Botania for providing the itemstack NBT interaction example!
 * https://github.com/Vazkii/Botania
 * I've used their example of storing mana as the basis for storing different types of feruchemical attributes.
 * In future, will also be doing it for gems and stormlight.
 */

package leaf.cosmere.charge;

import com.google.common.collect.Iterables;
import leaf.cosmere.compat.curios.CuriosCompat;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.CapWrapper;
import leaf.cosmere.registry.EffectsRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ItemChargeHelper
{

	public static List<ItemStack> getChargeItems(Player player)
	{
		if (player == null)
		{
			return Collections.emptyList();
		}

		Container acc = new CapWrapper(new PlayerInvWrapper(player.getInventory()));

		List<ItemStack> toReturn = getChargeableItemStacks(acc);

		return toReturn;
	}

	public static List<ItemStack> getChargeCurios(Player player)
	{
		if (player == null || !CuriosCompat.CuriosIsPresent())
		{
			return Collections.emptyList();
		}

		Container acc = new CapWrapper(CuriosApi.getCuriosHelper().getEquippedCurios(player).orElseGet(EmptyHandler::new));

		List<ItemStack> toReturn = getChargeableItemStacks(acc);

		return toReturn;
	}

	private static List<ItemStack> getChargeableItemStacks(Container acc)
	{
		List<ItemStack> toReturn = new ArrayList<>(acc.getContainerSize());

		for (int slot = 0; slot < acc.getContainerSize(); slot++)
		{
			ItemStack stackInSlot = acc.getItem(slot);

			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IChargeable)
			{
				toReturn.add(stackInSlot);
			}
		}
		return toReturn;
	}

	public static int requestCharge(ItemStack stack, Player player, int chargeToGet, boolean remove)
	{
		return requestCharge(stack, player, chargeToGet, remove, false);
	}

	public static int requestCharge(ItemStack stack, Player player, int chargeToGet, boolean remove, boolean checkPlayerID)
	{
		if (stack.isEmpty())
		{
			return 0;
		}

		List<ItemStack> items = getChargeItems(player);
		List<ItemStack> acc = getChargeCurios(player);
		for (ItemStack stackInSlot : Iterables.concat(items, acc))
		{
			if (stackInSlot == stack)
			{
				continue;
			}
			IChargeable chargeItem = (IChargeable) stackInSlot.getItem();
			if (chargeItem.canGiveChargeToItem(stackInSlot, stack) && chargeItem.getCharge(stackInSlot) > 0)
			{
				if (stack.getItem() instanceof IChargeable && !((IChargeable) stack.getItem()).canReceiveChargeFromItem(stack, stackInSlot))
				{
					continue;
				}
				//don't let non attuned players use the charge if not allowed.
				if (checkPlayerID && !chargeItem.getPlayerIsAttuned(stackInSlot, player))
				{
					continue;
				}

				int charge = Math.min(chargeToGet, chargeItem.getCharge(stackInSlot));

				if (remove)
				{
					chargeItem.adjustCharge(stackInSlot, -charge);
				}

				return charge;
			}
		}

		return 0;
	}


	public static ItemStack adjustChargeExact(Player player, int chargeToGet, boolean remove)
	{
		return adjustChargeExact(player, chargeToGet, remove, false);
	}

	public static ItemStack adjustChargeExact(Player player, int chargeToGet, boolean remove, boolean checkPlayer)
	{
		List<ItemStack> items = getChargeItems(player);
		List<ItemStack> acc = getChargeCurios(player);

		return adjustChargeExact(player, chargeToGet, remove, checkPlayer, items, acc);
	}

	public static ItemStack adjustChargeExact(Player player, int adjustValue, boolean doAdjust, boolean checkPlayer, List<ItemStack> items, List<ItemStack> acc)
	{
		MobEffectInstance storingIdentity = player.getEffect(EffectsRegistry.STORING_EFFECTS.get(Metals.MetalType.ALUMINUM).get());
		boolean isStoringIdentity = (storingIdentity != null && storingIdentity.getDuration() > 0);

		for (ItemStack stackInSlot : Iterables.concat(items, acc))
		{
			IChargeable chargeItemSlot = (IChargeable) stackInSlot.getItem();
			boolean storing = adjustValue < 0;

			int slotCharge = chargeItemSlot.getCharge(stackInSlot);
			int slotMaxCharge = chargeItemSlot.getMaxCharge(stackInSlot);

			//if draining, skip empty.
			if (!storing && slotCharge <= 0 || storing && slotCharge >= slotMaxCharge)
			{
				continue;
			}

			boolean playerUnableToAccess = !chargeItemSlot.trySetAttunedPlayer(stackInSlot, player);
			final UUID attunedPlayer = chargeItemSlot.getAttunedPlayer(stackInSlot);
			if (checkPlayer && playerUnableToAccess //if we need to make sure the player has access and they do not
					|| //or if the player is trying to store in an unsealed metalmind but have identity
					storing && !isStoringIdentity && attunedPlayer != null && attunedPlayer.compareTo(Constants.NBT.UNKEYED_UUID) == 0)
			{
				continue;
			}


			if ((storing && (slotCharge + (-adjustValue)) <= slotMaxCharge)//storing and can fit in this item
					|| !storing && slotCharge >= adjustValue)
			{
				if (doAdjust)
				{
					chargeItemSlot.adjustCharge(stackInSlot, -adjustValue);
				}

				return stackInSlot;
			}
		}

		return null;
	}


	public static boolean requestChargeExact(ItemStack stack, Player player, int chargeToGet, boolean remove)
	{
		if (stack.isEmpty())
		{
			return false;
		}

		List<ItemStack> items = getChargeItems(player);
		List<ItemStack> acc = getChargeCurios(player);
		for (ItemStack stackInSlot : Iterables.concat(items, acc))
		{
			if (stackInSlot == stack)
			{
				continue;
			}
			IChargeable chargeItemSlot = (IChargeable) stackInSlot.getItem();
			if (chargeItemSlot.canGiveChargeToItem(stackInSlot, stack) && chargeItemSlot.getCharge(stackInSlot) > chargeToGet)
			{
				if (stack.getItem() instanceof IChargeable && !((IChargeable) stack.getItem()).canReceiveChargeFromItem(stack, stackInSlot))
				{
					continue;
				}

				if (remove)
				{
					chargeItemSlot.adjustCharge(stackInSlot, -chargeToGet);
				}

				return true;
			}
		}

		return false;
	}

	public static int dispatchCharge(Player player, int chargeToSend, boolean add)
	{
		List<ItemStack> items = getChargeItems(player);
		List<ItemStack> acc = getChargeCurios(player);

		Iterable<ItemStack> itemStacksIterable = Iterables.concat(items, acc);

		for (ItemStack stackInSlot : itemStacksIterable)
		{
			IChargeable chargeItemSlot = (IChargeable) stackInSlot.getItem();

			int received;
			if (chargeItemSlot.getCharge(stackInSlot) + chargeToSend <= chargeItemSlot.getMaxCharge(stackInSlot))
			{
				received = chargeToSend;
			}
			else
			{
				received = chargeToSend - (chargeItemSlot.getCharge(stackInSlot) + chargeToSend - chargeItemSlot.getMaxCharge(stackInSlot));
			}

			if (add)
			{
				chargeItemSlot.adjustCharge(stackInSlot, chargeToSend);
			}

			return received;
		}

		return 0;
	}

	public static int dispatchCharge(ItemStack stack, Player player, int chargeToSend, boolean add)
	{
		if (stack.isEmpty())
		{
			return 0;
		}

		List<ItemStack> items = getChargeItems(player);
		List<ItemStack> acc = getChargeCurios(player);
		for (ItemStack stackInSlot : Iterables.concat(items, acc))
		{
			if (stackInSlot == stack)
			{
				continue;
			}
			IChargeable chargeItemSlot = (IChargeable) stackInSlot.getItem();
			if (chargeItemSlot.canReceiveChargeFromItem(stackInSlot, stack))
			{
				if (stack.getItem() instanceof IChargeable && !((IChargeable) stack.getItem()).canGiveChargeToItem(stack, stackInSlot))
				{
					continue;
				}

				int received;
				if (chargeItemSlot.getCharge(stackInSlot) + chargeToSend <= chargeItemSlot.getMaxCharge(stackInSlot))
				{
					received = chargeToSend;
				}
				else
				{
					received = chargeToSend - (chargeItemSlot.getCharge(stackInSlot) + chargeToSend - chargeItemSlot.getMaxCharge(stackInSlot));
				}

				if (add)
				{
					chargeItemSlot.adjustCharge(stackInSlot, chargeToSend);
				}

				return received;
			}
		}

		return 0;
	}


	public static boolean dispatchChargeExact(ItemStack stack, Player player, int chargeToSend, boolean add)
	{
		if (stack.isEmpty())
		{
			return false;
		}

		List<ItemStack> items = getChargeItems(player);
		List<ItemStack> acc = getChargeCurios(player);
		for (ItemStack stackInSlot : Iterables.concat(items, acc))
		{
			if (stackInSlot == stack)
			{
				continue;
			}
			IChargeable chargeItemSlot = (IChargeable) stackInSlot.getItem();
			if (chargeItemSlot.getCharge(stackInSlot) + chargeToSend <= chargeItemSlot.getMaxCharge(stackInSlot) && chargeItemSlot.canReceiveChargeFromItem(stackInSlot, stack))
			{
				if (stack.getItem() instanceof IChargeable && !((IChargeable) stack.getItem()).canGiveChargeToItem(stack, stackInSlot))
				{
					continue;
				}

				if (add)
				{
					chargeItemSlot.adjustCharge(stackInSlot, chargeToSend);
				}

				return true;
			}
		}

		return false;
	}

	private static int discountChargeForTool(ItemStack stack, Player player, int inCost)
	{
		float multiplier = Math.max(0F, 1F - getFullDiscountForTools(player, stack));
		return (int) (inCost * multiplier);
	}


	public static int requestChargeForTool(ItemStack stack, Player player, int chargeToGet, boolean remove)
	{
		int cost = discountChargeForTool(stack, player, chargeToGet);
		return requestCharge(stack, player, cost, remove);
	}


	public static boolean requestChargeExactForTool(ItemStack stack, Player player, int chargeToGet, boolean remove)
	{
		int cost = discountChargeForTool(stack, player, chargeToGet);
		return requestChargeExact(stack, player, cost, remove);
	}


	public static int getInvocationCountForTool(ItemStack stack, Player player, int chargeToGet)
	{
		if (stack.isEmpty())
		{
			return 0;
		}

		int cost = discountChargeForTool(stack, player, chargeToGet);
		int invocations = 0;

		List<ItemStack> items = getChargeItems(player);
		List<ItemStack> acc = getChargeCurios(player);
		for (ItemStack stackInSlot : Iterables.concat(items, acc))
		{
			if (stackInSlot == stack)
			{
				continue;
			}
			IChargeable chargeItemSlot = (IChargeable) stackInSlot.getItem();
			int availableCharge = chargeItemSlot.getCharge(stackInSlot);
			if (chargeItemSlot.canGiveChargeToItem(stackInSlot, stack) && availableCharge > cost)
			{
				if (stack.getItem() instanceof IChargeable && !((IChargeable) stack.getItem()).canReceiveChargeFromItem(stack, stackInSlot))
				{
					continue;
				}

				invocations += availableCharge / cost;
			}
		}

		return invocations;
	}


	public static float getFullDiscountForTools(Player player, ItemStack tool)
	{
		float discount = 0F;
		//todo discount
		/*for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
			ItemStack armor = player.inventory.armorInventory.get(i);
			if (!armor.isEmpty() && armor.getItem() instanceof IChargeDiscountArmor) {
				discount += ((IChargeDiscountArmor) armor.getItem()).getDiscount(armor, i, player, tool);
			}
		}

		int unbreaking = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, tool);
		discount += unbreaking * 0.05F;

		ChargeDiscountEvent event = new ChargeDiscountEvent(player, discount, tool);
		MinecraftForge.EVENT_BUS.post(event);
		discount = event.getDiscount();*/

		return discount;
	}
}
