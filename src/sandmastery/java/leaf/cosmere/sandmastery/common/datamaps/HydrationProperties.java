package leaf.cosmere.sandmastery.common.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

//hydration an item grants when finished being used (data/<pack>/data_maps/item/hydration.json)
public record HydrationProperties(int amount)
{
	public static final Codec<HydrationProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.intRange(0, Integer.MAX_VALUE).fieldOf("amount").forGetter(HydrationProperties::amount)
	).apply(instance, HydrationProperties::new));
}
