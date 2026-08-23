/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IShardbladeDynamicData extends INBTSerializable<CompoundTag>
{
	String getBladeID();

	String getHandleID();

	String getPommelID();

	String getCrossGuardID();
}
