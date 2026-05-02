/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * Capability/LazyOptional/INBTSerializable removed. Per-stack state now lives directly on
 * the ItemStack via `DataComponents.CUSTOM_DATA` through `StackNBTHelper`. Construct a
 * stack-bound view with `BondData.of(stack)` and use it like a plain DTO.
 */

package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.helpers.StackNBTHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BondData implements IBondData
{
	private static final String NBT_BOND_TICKS = "bondTicks";

	protected final ItemStack stack;

	public BondData(ItemStack stack)
	{
		this.stack = stack;
	}

	public static BondData of(ItemStack stack)
	{
		return new BondData(stack);
	}

	@Override
	public UUID getBondedEntity()
	{
		return StackNBTHelper.getUuid(stack, Constants.NBT.ATTUNED_PLAYER);
	}

	@Override
	public String getBondedName()
	{
		return StackNBTHelper.getString(stack, Constants.NBT.ATTUNED_PLAYER_NAME, "");
	}

	@Override
	public boolean isBonded()
	{
		return getBondedEntity() != null;
	}

	@Override
	public void setBondedEntity(LivingEntity entity)
	{
		StackNBTHelper.setUuid(stack, Constants.NBT.ATTUNED_PLAYER, entity.getUUID());
		StackNBTHelper.setString(stack, Constants.NBT.ATTUNED_PLAYER_NAME, entity.getName().getString());
	}

	@Override
	public void setEmptyBond()
	{
		StackNBTHelper.removeEntry(stack, Constants.NBT.ATTUNED_PLAYER);
		StackNBTHelper.removeEntry(stack, Constants.NBT.ATTUNED_PLAYER_NAME);
	}

	@Override
	public int bondTicks()
	{
		return StackNBTHelper.getInt(stack, NBT_BOND_TICKS, 0);
	}

	@Override
	public void tickBondUp()
	{
		StackNBTHelper.setInt(stack, NBT_BOND_TICKS, bondTicks() + 1);
	}

	@Override
	public void resetBondTicks()
	{
		StackNBTHelper.setInt(stack, NBT_BOND_TICKS, 0);
	}
}
