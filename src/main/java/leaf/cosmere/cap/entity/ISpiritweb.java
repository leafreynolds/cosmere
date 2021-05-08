/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the Suff and their mod Regeneration.
 * That mod taught me how to add ticking capabilities to entities and have them sync
 * https://github.com/WhoCraft/Regeneration
 */

package leaf.cosmere.cap.entity;

import leaf.cosmere.cap.Capabilities;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.List;

public interface ISpiritweb extends INBTSerializable<CompoundNBT>
{
    void tick();

    LivingEntity getLiving();

    boolean hasManifestation(Manifestations.ManifestationTypes type, int powerID);

    boolean hasManifestation(Manifestations.ManifestationTypes type, int powerID, boolean ignoreTemporaryPower);

    void giveManifestation(Manifestations.ManifestationTypes type, int powerID);

    void removeManifestation(Manifestations.ManifestationTypes type, int powerID);

    AManifestation manifestation();

    AManifestation manifestation(Manifestations.ManifestationTypes powerType, int powerID);

    boolean manifestationActive(Manifestations.ManifestationTypes powerType, int powerID);

    boolean selectedManifestationActive();

    int nextMode(Manifestations.ManifestationTypes powerType, int powerID);

    int previousMode(Manifestations.ManifestationTypes powerType, int powerID);

    void setMode(Manifestations.ManifestationTypes powerType, int powerID, int mode);

    int getMode(Manifestations.ManifestationTypes powerType, int powerID);

    void syncToClients(@Nullable ServerPlayerEntity serverPlayerEntity);

    void deactivateCurrentManifestation();

    void deactivateManifestations();

    void clearManifestations();

    List<AManifestation> getAvailableManifestations();

    List<AManifestation> getAvailableManifestations(boolean ignoreTemporaryPower);

    String changeManifestation(int dir);

    void renderWorldEffects(RenderWorldLastEvent event);

    void setSelectedManifestation(AManifestation manifestation);

    int getIngestedMetal(Metals.MetalType metalType);

    boolean adjustIngestedMetal(Metals.MetalType metalType, int i, boolean doAdjust);

    class Storage implements Capability.IStorage<ISpiritweb>
    {

        @Override
        public INBT writeNBT(Capability<ISpiritweb> capability, ISpiritweb instance, Direction side)
        {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<ISpiritweb> capability, ISpiritweb instance, Direction side, INBT nbt)
        {
            if (nbt instanceof CompoundNBT)
            {
                instance.deserializeNBT((CompoundNBT) nbt);
            }
        }
    }


    class Provider implements ICapabilitySerializable<CompoundNBT>
    {

        ISpiritweb data;

        public Provider(ISpiritweb data)
        {
            this.data = data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
        {
            return cap == Capabilities.SPIRITWEB_CAPABILITY ? (LazyOptional<T>) LazyOptional.of(() -> data)
                                                            : LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT()
        {
            return data.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt)
        {
            data.deserializeNBT(nbt);
        }

    }
}
