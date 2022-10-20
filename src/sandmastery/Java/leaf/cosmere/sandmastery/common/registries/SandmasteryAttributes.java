package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.api.Taldain;
import leaf.cosmere.common.registration.impl.AttributeDeferredRegister;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SandmasteryAttributes {
    public static final AttributeDeferredRegister ATTRIBUTES = new AttributeDeferredRegister(Sandmastery.MODID);

    public static final Map<Taldain.Mastery, AttributeRegistryObject<Attribute>> SANDMASTER_ATTRIBUTES =
            Arrays.stream(Taldain.Mastery.values())
                    .collect(Collectors.toMap(
                            Function.identity(),
                            surge ->
                                    ATTRIBUTES.register(
                                            surge.getName(),
                                            Sandmastery.MODID,
                                            0,
                                            0,
                                            10
                                    )));
}
