/*
 * File updated ~ 9 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.common.registration.impl.BiomeModifierSerializerDeferredRegister;
import leaf.cosmere.surgebinding.common.Surgebinding;

public class SurgebindingBiomeModifiers
{
	public static final BiomeModifierSerializerDeferredRegister BIOME_MODIFIER_SERIALIZERS = new BiomeModifierSerializerDeferredRegister(Surgebinding.MODID);

	//public static final BiomeModifierSerializerRegistryObject<GemOreBiomeFeatureModifier> GEM_ORE_MODIFIER = BIOME_MODIFIER_SERIALIZERS.register("ores", GemOreBiomeFeatureModifier::makeCodec);


}
