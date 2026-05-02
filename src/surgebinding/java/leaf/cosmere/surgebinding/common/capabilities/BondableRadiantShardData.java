/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * Capability/LazyOptional removed. State now persists directly on the ItemStack via
 * `DataComponents.CUSTOM_DATA` through `StackNBTHelper`. Subclass of RadiantShardData
 * with the bond fields layered on (same NBT keys as the standalone BondData so a single
 * stack can carry both views without conflict).
 */

package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.helpers.StackNBTHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BondableRadiantShardData extends RadiantShardData implements IBondData
{
	private static final String NBT_BOND_TICKS = "bondTicks";

	public BondableRadiantShardData(ItemStack stack)
	{
		super(stack);
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
