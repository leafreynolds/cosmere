/*
 * File updated ~ 30 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.capabilities.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IHemalurgyWorldCap extends INBTSerializable<CompoundTag>
{

	void tick();
}
