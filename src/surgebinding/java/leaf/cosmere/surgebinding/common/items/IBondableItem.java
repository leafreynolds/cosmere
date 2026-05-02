/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * `stack.getCapability(BondData.BOND_DATA)` removed in 1.21.1 — `BondData` now binds directly
 * to the stack via `BondData.of(stack)` and reads/writes through DataComponents.CUSTOM_DATA.
 */

package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.Constants;
import leaf.cosmere.surgebinding.common.capabilities.BondData;
import leaf.cosmere.surgebinding.common.capabilities.IBondData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public interface IBondableItem
{
	default IBondData getBondData(ItemStack stack)
	{
		return BondData.of(stack);
	}

	default boolean isBondedTo(ItemStack stack, LivingEntity player)
	{
		IBondData data = getBondData(stack);
		UUID id = data.getBondedEntity();
		if (id == null || id.equals(Constants.NBT.UNKEYED_UUID))
		{
			return false;
		}
		return id.equals(player.getUUID());
	}

	void bond(ItemStack stack, Player entity);

	void releaseBond(ItemStack stack);

	int bondTime();
}
