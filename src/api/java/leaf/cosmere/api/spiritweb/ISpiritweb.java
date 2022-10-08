/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.spiritweb;

import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.List;

public interface ISpiritweb extends INBTSerializable<CompoundTag>
{
	void tick();

	LivingEntity getLiving();

	boolean hasManifestation(Manifestation manifestation);

	boolean hasManifestation(Manifestation manifestation, boolean ignoreTemporaryPower);

	void giveManifestation(Manifestation manifestation, int i);

	void removeManifestation(Manifestation manifestation);

	Manifestation manifestation();

	boolean canTickManifestation(Manifestation manifestation);

	int nextMode(Manifestation manifestation);

	int previousMode(Manifestation manifestation);

	void setMode(Manifestation manifestation, int mode);

	int getMode(Manifestation manifestation);

	void syncToClients(@Nullable ServerPlayer serverPlayerEntity);

	void deactivateCurrentManifestation();

	void deactivateManifestations();

	void clearManifestations();

	List<Manifestation> getAvailableManifestations();

	List<Manifestation> getAvailableManifestations(boolean ignoreTemporaryPower);

	String changeManifestation(int dir);

	void renderWorldEffects(RenderLevelStageEvent event);

	void setSelectedManifestation(Manifestation manifestation);


	void transferFrom(ISpiritweb oldSpiritWeb);

	CompoundTag getCompoundTag();
}
