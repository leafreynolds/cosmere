/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * Forge `INBTSerializable<CompoundTag>` is gone in 1.21.1. See IBondData for migration
 * notes. Implementations now bind to an ItemStack and persist via DataComponents.
 */

package leaf.cosmere.surgebinding.common.capabilities;

public interface IShardplateDynamicData
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
