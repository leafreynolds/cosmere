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

//metalmind definition for the feruchemy:metalmind data map (data/<pack>/data_maps/item/metalmind.json)
public record MetalmindProperties(Metals.MetalType metal, float chargeModifier)
{
	//ring 4/9, bracelet 5/9, necklace base 1
	public static final float DEFAULT_CHARGE_MODIFIER = 4f / 9f;

	public static final float RING_CHARGE_MODIFIER = 4f / 9f;

	public static final float BRACELET_CHARGE_MODIFIER = 5f / 9f;

	public static final Codec<MetalmindProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Metals.MetalType.CODEC.fieldOf("metal").forGetter(MetalmindProperties::metal),
			Codec.FLOAT.optionalFieldOf("charge_modifier", DEFAULT_CHARGE_MODIFIER).forGetter(MetalmindProperties::chargeModifier)
	).apply(instance, MetalmindProperties::new));
}
