/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.common.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import leaf.cosmere.common.config.CosmereConfigs;
import leaf.cosmere.common.registry.IntProviderTypesRegistry;
import leaf.cosmere.common.resource.ore.OreType.OreVeinType;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviderType;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

//based on ConfigurableConstantInt from Mekanism
// https://github.com/mekanism/Mekanism/blob/7de496745c721fb15d00d590ddcacf00570f3f1b/src/main/java/mekanism/common/world/ConfigurableConstantInt.java#L17
public class ConfigurableConstantInt extends IntProvider
{

	public static final MapCodec<ConfigurableConstantInt> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
			OreVeinType.CODEC.optionalFieldOf("oreVeinType").forGetter(config -> Optional.ofNullable(config.oreVeinType))
	).apply(builder, oreType ->
	{
		if (oreType.isPresent())
		{
			OreVeinType type = oreType.get();
			return new ConfigurableConstantInt(type, CosmereConfigs.WORLD_CONFIG.getVeinConfig(type).perChunk());
		}
		return null;//todo hope null works
	}));

	@Nullable
	private final OreVeinType oreVeinType;
	private final ModConfigSpec.ConfigValue<Integer> value;

	public ConfigurableConstantInt(@Nullable OreVeinType oreVeinType, ModConfigSpec.ConfigValue<Integer> value)
	{
		this.oreVeinType = oreVeinType;
		this.value = value;
	}

	public int getValue()
	{
		//Needs to be getOrDefault so that when IntProvider's range codec validates things in CountPlacement,
		// even though how it gets that value doesn't matter for syncing. Our actual value here doesn't really
		// matter because we limit our config values at the ranges of CountPlacement
		return this.value.getDefault();
	}

	@Override
	public int sample(@NotNull RandomSource random)
	{
		return getValue();
	}

	@Override
	public int getMinValue()
	{
		return getValue();
	}

	@Override
	public int getMaxValue()
	{
		return getValue();
	}

	@NotNull
	@Override
	public IntProviderType<?> getType()
	{
		return IntProviderTypesRegistry.CONFIGURABLE_CONSTANT.get();
	}

	@Override
	public String toString()
	{
		return Integer.toString(getValue());
	}
}
