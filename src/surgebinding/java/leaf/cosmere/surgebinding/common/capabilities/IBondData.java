/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * Forge `Capability<T>` + `INBTSerializable<CompoundTag>` are gone in 1.21.1. The data
 * implementations now bind directly to an ItemStack and read/write fields through
 * `StackNBTHelper` (which routes through `DataComponents.CUSTOM_DATA`). The interface
 * therefore drops INBTSerializable; serialization is implicit (already on the stack).
 */

package leaf.cosmere.surgebinding.common.capabilities;

import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

public interface IBondData
{
	UUID getBondedEntity();

	String getBondedName();

	boolean isBonded();

	void setBondedEntity(LivingEntity entity);

	void setEmptyBond();

	int bondTicks();

	void tickBondUp();

	void resetBondTicks();
}
