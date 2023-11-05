/*
 * File updated ~ 5 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.capabilities.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IScadrial extends INBTSerializable<CompoundTag>
{
	float getMistNearDistance();

	float getMistFarDistance();
}
