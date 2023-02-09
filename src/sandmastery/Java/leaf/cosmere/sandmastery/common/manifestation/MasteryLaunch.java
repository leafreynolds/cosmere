package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.math.VectorHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.client.SandmasteryKeybindings;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import leaf.cosmere.sandmastery.common.utils.SandmasteryConstants;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

public class MasteryLaunch extends SandmasteryManifestation{
    public MasteryLaunch(Taldain.Mastery mastery) {
        super(mastery);
    }

    @Override
    public void tick(ISpiritweb data)
    {
        int hotkeyFlags = CompoundNBTHelper.getOrCreate(data.getCompoundTag(), "sandmastery").getInt("hotkeys");
        boolean enabledViaHotkey = false;
        if((hotkeyFlags & SandmasteryConstants.launchFlagVal) != 0) enabledViaHotkey = true;
        if((hotkeyFlags & 1) != 0) enabledViaHotkey = true;
        if(getMode(data) > 0 && enabledViaHotkey) performEffectServer(data);
    }

    protected void performEffectServer(ISpiritweb data)
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
