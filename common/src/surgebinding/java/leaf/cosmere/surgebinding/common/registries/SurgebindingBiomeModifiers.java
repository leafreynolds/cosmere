/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.common.registration.impl.BiomeModifierSerializerDeferredRegister;
import leaf.cosmere.common.registration.impl.BiomeModifierSerializerRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.world.GemOreBiomeFeatureModifier;

public class SurgebindingBiomeModifiers
{
	public static final BiomeModifierSerializerDeferredRegister BIOME_MODIFIER_SERIALIZERS = new BiomeModifierSerializerDeferredRegister(Surgebinding.MODID);

	public static final BiomeModifierSerializerRegistryObject<GemOreBiomeFeatureModifier> GEM_ORE_MODIFIER = BIOME_MODIFIER_SERIALIZERS.register("ores", GemOreBiomeFeatureModifier::makeCodec);


}
