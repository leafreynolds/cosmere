/*
 * File updated ~ 3 - 7 - 2022 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.BiomeModifierSerializerDeferredRegister;
import leaf.cosmere.common.registration.impl.BiomeModifierSerializerRegistryObject;
import leaf.cosmere.common.world.MetalOreBiomeFeatureModifier;

public class BiomeModifierRegistry
{
	public static final BiomeModifierSerializerDeferredRegister BIOME_MODIFIER_SERIALIZERS = new BiomeModifierSerializerDeferredRegister(Cosmere.MODID);

	public static final BiomeModifierSerializerRegistryObject<MetalOreBiomeFeatureModifier> METAL_ORE_MODIFIER = BIOME_MODIFIER_SERIALIZERS.register("ores", MetalOreBiomeFeatureModifier::makeCodec);


}
