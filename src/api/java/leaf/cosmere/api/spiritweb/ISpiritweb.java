/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.api.spiritweb;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.cosmereEffect.CosmereEffectInstance;
import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ISpiritweb extends INBTSerializable<CompoundTag>
{
	void tick();

	LivingEntity getLiving();

	boolean hasManifestation(Manifestation manifestation);

	boolean hasManifestation(Manifestation manifestation, boolean ignoreTemporaryPower);

	void giveManifestation(Manifestation manifestation, int i);

	void removeManifestation(Manifestation manifestation);

	Manifestation getSelectedManifestation();

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

	ISpiritwebSubmodule getSubmodule(Manifestations.ManifestationTypes manifestationType);

	Map<Manifestations.ManifestationTypes, ISpiritwebSubmodule> getSubmodules();

	void tickEffects();

	void addEffect(CosmereEffectInstance newEffect);

	void addEffect(CosmereEffectInstance newEffect, @Nullable Entity sourceEntity);

	void onEffectAdded(CosmereEffectInstance newEffect, Entity sourceEntity);

	void onEffectUpdated(CosmereEffectInstance cosmereEffectInstance, boolean forced, Entity entity);

	void removeEffect(UUID uuid);

	void onEffectRemoved(CosmereEffectInstance cosmereEffectInstance);

	int totalStrengthOfEffect(CosmereEffect cosmereEffect);

	CosmereEffectInstance getEffect(UUID uuid);
}
