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
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.CapWrapper;
import leaf.cosmere.items.Metalmind;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChargeItemHandler
{

    public static List<ItemStack> getChargeItems(PlayerEntity player)
    {
        if (player == null)
        {
            return Collections.emptyList();
        }

        IInventory acc = new CapWrapper(new PlayerInvWrapper(player.inventory));

        List<ItemStack> toReturn = getChargeableItemStacks(acc);

        return toReturn;
    }

    public static List<ItemStack> getChargeCurios(PlayerEntity player)
    {
        if (player == null || !CuriosCompat.CuriosIsPresent())
        {
            return Collections.emptyList();
        }

        IInventory acc = new CapWrapper(CuriosApi.getCuriosHelper().getEquippedCurios(player).orElseGet(EmptyHandler::new));

        List<ItemStack> toReturn = getChargeableItemStacks(acc);

        return toReturn;
    }

    private static List<ItemStack> getChargeableItemStacks(IInventory acc)
    {
        List<ItemStack> toReturn = new ArrayList<>(acc.getSizeInventory());

        for (int slot = 0; slot < acc.getSizeInventory(); slot++)
        {
            ItemStack stackInSlot = acc.getStackInSlot(slot);

            if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IChargeable)
            {
                toReturn.add(stackInSlot);
            }
        }
        return toReturn;
    }

    public static int requestCharge(ItemStack stack, PlayerEntity player, int chargeToGet, boolean remove)
    {
        return requestCharge(stack, player, chargeToGet, remove, false);
    }

    public static int requestCharge(ItemStack stack, PlayerEntity player, int chargeToGet, boolean remove, boolean checkPlayerID)
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


    public static boolean spendChargeExact(PlayerEntity player, int chargeToGet, boolean remove)
    {
        return spendChargeExact(player, chargeToGet, remove, false);
    }


    public static boolean spendMetalmindChargeExact(PlayerEntity player, Metals.MetalType metalType, int chargeToGet, boolean remove, boolean checkPlayer)
    {
        List<ItemStack> items = getChargeItems(player);
        List<ItemStack> acc = getChargeCurios(player);

        //remove items that don't match the metal type we are looking for
        items.removeIf(obj ->
                {
                    boolean objectIsNotMetalmind = !(obj.getItem() instanceof Metalmind);
                    boolean metalMindIsNotCorrectType = ((Metalmind) obj.getItem()).getMetalType() != metalType;

                    return (objectIsNotMetalmind || metalMindIsNotCorrectType);
                }
        );
        acc.removeIf(obj ->
                {
                    boolean objectIsNotMetalmind = !(obj.getItem() instanceof Metalmind);
                    boolean metalMindIsNotCorrectType = ((Metalmind) obj.getItem()).getMetalType() != metalType;

                    return (objectIsNotMetalmind || metalMindIsNotCorrectType);
                }
        );

        return spendChargeExact(player, chargeToGet, remove, checkPlayer, items, acc);
    }

    public static boolean spendChargeExact(PlayerEntity player, int chargeToGet, boolean remove, boolean checkPlayer)
    {
        List<ItemStack> items = getChargeItems(player);
        List<ItemStack> acc = getChargeCurios(player);

        return spendChargeExact(player, chargeToGet, remove, checkPlayer, items, acc);
    }

    public static boolean spendChargeExact(PlayerEntity player, int chargeToGet, boolean remove, boolean checkPlayer, List<ItemStack> items, List<ItemStack> acc)
    {
        for (ItemStack stackInSlot : Iterables.concat(items, acc))
        {
            IChargeable chargeItemSlot = (IChargeable) stackInSlot.getItem();

            if (checkPlayer && !chargeItemSlot.trySetAttunedPlayer(stackInSlot, player))
            {
                continue;
            }

            if (chargeItemSlot.getCharge(stackInSlot) > chargeToGet)
            {
                if (remove)
                {
                    chargeItemSlot.adjustCharge(stackInSlot, -chargeToGet);
                }

                return true;
            }
        }

        return false;
    }


    public static boolean requestChargeExact(ItemStack stack, PlayerEntity player, int chargeToGet, boolean remove)
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

    public static int dispatchCharge(PlayerEntity player, int chargeToSend, boolean add)
    {
        List<ItemStack> items = getChargeItems(player);
        List<ItemStack> acc = getChargeCurios(player);
        for (ItemStack stackInSlot : Iterables.concat(items, acc))
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

    public static int dispatchCharge(ItemStack stack, PlayerEntity player, int chargeToSend, boolean add)
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


    public static boolean dispatchChargeExact(ItemStack stack, PlayerEntity player, int chargeToSend, boolean add)
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

    private static int discountChargeForTool(ItemStack stack, PlayerEntity player, int inCost)
    {
        float multiplier = Math.max(0F, 1F - getFullDiscountForTools(player, stack));
        return (int) (inCost * multiplier);
    }


    public static int requestChargeForTool(ItemStack stack, PlayerEntity player, int chargeToGet, boolean remove)
    {
        int cost = discountChargeForTool(stack, player, chargeToGet);
        return requestCharge(stack, player, cost, remove);
    }


    public static boolean requestChargeExactForTool(ItemStack stack, PlayerEntity player, int chargeToGet, boolean remove)
    {
        int cost = discountChargeForTool(stack, player, chargeToGet);
        return requestChargeExact(stack, player, cost, remove);
    }


    public static int getInvocationCountForTool(ItemStack stack, PlayerEntity player, int chargeToGet)
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


    public static float getFullDiscountForTools(PlayerEntity player, ItemStack tool)
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
