package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.Roshar;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public interface IShard extends INBTSerializable<CompoundTag>
{
	Roshar.RadiantOrder getOrder();

	boolean isLiving();

	UUID getBondedEntity();
	String getBondedName();
	boolean isBonded();

	void setOrder(Roshar.RadiantOrder order);
	void setLiving(boolean living);

	void setBondedEntity(LivingEntity entity);

	int bondTicks();
	void tickBondUp();
	void resetBondTicks();

}
