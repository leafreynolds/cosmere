package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.math.VectorHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.client.SandmasteryKeybindings;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
public class MasteryLaunch extends SandmasteryManifestation{
    public MasteryLaunch(Taldain.Mastery mastery) {
        super(mastery);
    }

    @Override
    public void tick(ISpiritweb data)
    {
        int mode = getMode(data);
        if (mode > 0 && (MiscHelper.isActivatedAndActive(data, this) || SandmasteryKeybindings.SANDMASTERY_LAUNCH.isDown())) {
            applyEffectTick(data);
        }
    }

    @Override
    public void applyEffectTick(ISpiritweb data)
    {
        performEffectServer(data);
    }

    private void performEffectServer(ISpiritweb data)
    {
        SpiritwebCapability playerSpiritweb = (SpiritwebCapability) data;
        SandmasterySpiritwebSubmodule submodule = (SandmasterySpiritwebSubmodule) playerSpiritweb.spiritwebSubmodules.get(Manifestations.ManifestationTypes.SANDMASTERY);

        if(!submodule.adjustHydration(-10, false)) return;
        int scaleFactor = getMode(data);
        if(!enoughChargedSand(data)) return;

        LivingEntity living = data.getLiving();
        Vec3 direction = living.getForward();
        Vec3 add = living.getDeltaMovement().add(direction.multiply(scaleFactor, scaleFactor, scaleFactor));
        living.setDeltaMovement(VectorHelper.ClampMagnitude(add, 10));
        living.hurtMarked = true; // Allow the game to move the player
        living.resetFallDistance();

        data.setMode(this, getMode(data)-1);
        data.syncToClients(null);

        submodule.adjustHydration(-10, true);
        useChargedSand(data);
    }
}
