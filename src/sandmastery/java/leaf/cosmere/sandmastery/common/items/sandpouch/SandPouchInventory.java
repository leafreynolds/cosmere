/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.sandmastery.common.items.sandpouch;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;

// Capability registration (RegisterCapabilitiesEvent) wired in SandmasteryModBusEventHandler
public class SandPouchInventory implements INBTSerializable<CompoundTag>
{
	public static final int size = 3;
	private final SandpouchItemHandler inv = new SandpouchItemHandler(size);

	public IItemHandler getHandler()
	{
		return inv;
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider registries)
	{
		return inv.serializeNBT(registries);
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt)
	{
		inv.deserializeNBT(registries, nbt);
	}
}
