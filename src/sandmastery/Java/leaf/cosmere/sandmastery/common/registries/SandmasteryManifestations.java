/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.api.Taldain;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.manifestation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SandmasteryManifestations
{
    public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Sandmastery.MODID);

    public static final Map<Taldain.Mastery, ManifestationRegistryObject<SandmasteryManifestation>> SANDMASTERY_POWERS =
            Arrays.stream(Taldain.Mastery.values())
                    .collect(Collectors.toMap(
                            Function.identity(),
                            investiture ->
                                    MANIFESTATIONS.register(
                                            investiture.getName(),
                                            () -> makeSandmasteryManifestation(investiture))
                    ));


    private static SandmasteryManifestation makeSandmasteryManifestation(Taldain.Mastery mastery)
    {
        switch (mastery)
        {
            case LAUNCH:
                return new MasteryLaunch(mastery);
            case ELEVATE:
                return new MasteryElevate(mastery);
            case CUSHION:
                return new MasteryCushion(mastery);
            case PROJECTILE:
                return new MasteryProjectile(mastery);
            case PLATFORM:
                return new MasteryPlatform(mastery);
            default:
                return new SandmasteryManifestation(mastery);
        }
    }
}
