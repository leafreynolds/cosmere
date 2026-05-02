package leaf.cosmere.surgebinding.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IShardplateDynamicData extends INBTSerializable<CompoundTag>
{
	String getHeadID();
	String getFaceplateID();
	String getBodyID();

	String getKamaID();

	String getRightArmID();

	String getRightPaldronsID();

	String getRightLegID();

	String getRightBootOutsideID();

	String getRightBootTipID();

	String getLeftArmID();

	String getLeftPaldronsID();

	String getLeftLegID();

	String getLeftBootOutsideID();

	String getLeftBootTipID();

	boolean isColored();
}
