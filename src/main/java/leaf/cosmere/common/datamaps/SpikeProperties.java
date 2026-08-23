/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

/*
 * File created ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.common.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import leaf.cosmere.api.Metals;

//spike definition for the hemalurgy:spike data map (data/<pack>/data_maps/item/spike.json)
public record SpikeProperties(Metals.MetalType metal, float chargeModifier)
{
	public static final float DEFAULT_CHARGE_MODIFIER = 0.5f / 9f;

	public static final Codec<SpikeProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Metals.MetalType.CODEC.fieldOf("metal").forGetter(SpikeProperties::metal),
			Codec.FLOAT.optionalFieldOf("charge_modifier", DEFAULT_CHARGE_MODIFIER).forGetter(SpikeProperties::chargeModifier)
	).apply(instance, SpikeProperties::new));
}
