/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the Suff and their mod Regeneration.
 * That mod taught me how to add ticking capabilities to entities and have them sync
 * https://github.com/WhoCraft/Regeneration
 */

package leaf.cosmere.cap.entity;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.List;

public interface ISpiritweb extends INBTSerializable<CompoundTag>
{
	void tick();

	LivingEntity getLiving();

	boolean hasManifestation(AManifestation aManifestation);

	boolean hasManifestation(AManifestation aManifestation, boolean ignoreTemporaryPower);

	void giveManifestation(AManifestation manifestation, int i);

	void removeManifestation(AManifestation aManifestation);

	AManifestation manifestation();

	boolean canTickManifestation(AManifestation aManifestation);

	boolean canTickSelectedManifestation();

	int nextMode(AManifestation aManifestation);

	int previousMode(AManifestation aManifestation);

	void setMode(AManifestation aManifestation, int mode);

	int getMode(AManifestation aManifestation);

	void syncToClients(@Nullable ServerPlayer serverPlayerEntity);

	void deactivateCurrentManifestation();

	void deactivateManifestations();

	void clearManifestations();

	List<AManifestation> getAvailableManifestations();

	List<AManifestation> getAvailableManifestations(boolean ignoreTemporaryPower);

	String changeManifestation(int dir);

	void renderWorldEffects(RenderLevelLastEvent event);

	void setSelectedManifestation(AManifestation manifestation);

	int getIngestedMetal(Metals.MetalType metalType);

	boolean adjustIngestedMetal(Metals.MetalType metalType, int amountToAdjust, boolean doAdjust);

	void transferFrom(ISpiritweb oldSpiritWeb);

	int getEyeHeight();
	void setEyeHeight(int eyeHeight);
}
