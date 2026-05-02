package leaf.cosmere.surgebinding.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public interface IBondData extends INBTSerializable<CompoundTag>
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
