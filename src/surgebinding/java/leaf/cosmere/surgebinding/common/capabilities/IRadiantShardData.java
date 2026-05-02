/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * Forge `Capability<T>` + `INBTSerializable<CompoundTag>` are gone in 1.21.1. See IBondData
 * for the migration shape. Implementations now bind to an ItemStack and persist via
 * `DataComponents.CUSTOM_DATA` through `StackNBTHelper`.
 */

package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.Roshar;

public interface IRadiantShardData
{
	Roshar.RadiantOrder getOrder();

	boolean isLiving();

	void setOrder(Roshar.RadiantOrder order);

	void setLiving(boolean living);
}
