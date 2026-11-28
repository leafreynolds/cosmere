package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.Roshar;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IRadiantShardData extends INBTSerializable<CompoundTag>
{
	Roshar.RadiantOrder getOrder();

	boolean isLiving();

	void setOrder(Roshar.RadiantOrder order);

	void setLiving(boolean living);
}
