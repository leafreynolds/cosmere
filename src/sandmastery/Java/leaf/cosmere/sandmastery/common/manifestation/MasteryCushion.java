package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.math.VectorHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.client.SandmasteryKeybindings;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

public class MasteryCushion extends SandmasteryManifestation {
    public MasteryCushion(Taldain.Mastery mastery) {
        super(mastery);
    }

    @Override
    public void tick(ISpiritweb data)
    {
        if(getMode(data) > 0) performEffectServer(data);
    }

    public void tickClient(ISpiritweb data) {
        if(MiscHelper.isClient(data)) performEffectClient(data);
    }

    protected void performEffectServer(ISpiritweb data)
    {
        SpiritwebCapability playerSpiritweb = (SpiritwebCapability) data;
        SandmasterySpiritwebSubmodule submodule = (SandmasterySpiritwebSubmodule) playerSpiritweb.spiritwebSubmodules.get(Manifestations.ManifestationTypes.SANDMASTERY);

        LivingEntity living = data.getLiving();
        Vec3 movement = living.getDeltaMovement();
        if(movement.y > 0) return;
        double distFromGround = MiscHelper.distanceFromGround(living);
        if(!(distFromGround > 1 && distFromGround < 10)) return;

        if(!submodule.adjustHydration(-10, false)) return;
        if(!enoughChargedSand(data)) return;


        living.setDeltaMovement(movement.multiply(1, 0.05, 1));
        living.hurtMarked = true;
        living.resetFallDistance();
        submodule.adjustHydration(-10, true);
        useChargedSand(data);
    }
}
