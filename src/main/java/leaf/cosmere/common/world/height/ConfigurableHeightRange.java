/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.common.world.height;

import leaf.cosmere.common.resource.ore.BaseOreConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

//based on ConfigurableHeightRange from Mekanism
// https://github.com/mekanism/Mekanism/blob/7de496745c721fb15d00d590ddcacf00570f3f1b/src/main/java/mekanism/common/world/height/ConfigurableHeightRange.java#L13
public record ConfigurableHeightRange(ModConfigSpec.EnumValue<HeightShape> shape,
                                      ConfigurableVerticalAnchor minInclusive,
                                      ConfigurableVerticalAnchor maxInclusive,
                                      ModConfigSpec.ConfigValue<Integer> plateau)
{

	public static ConfigurableHeightRange create(ModConfigSpec.Builder builder, String veinType, BaseOreConfig baseConfig)
	{
		ModConfigSpec.EnumValue<HeightShape> shape =
				builder.comment("Distribution shape for placing " + veinType + "s.")
						.defineEnum("shape", baseConfig.shape());

		ConfigurableVerticalAnchor minInclusive =
				ConfigurableVerticalAnchor.create(
						builder,
						"minInclusive",
						"Minimum (inclusive) height anchor for " + veinType + "s.", baseConfig.min(),
						null);

		final ModConfigSpec.ConfigValue<Integer> plat = builder.comment("Half length of short side of trapezoid, only used if shape is TRAPEZOID. A value of zero means the shape is a triangle.")
				.define("plateau", baseConfig.plateau(), o ->
				{
					if (o instanceof Integer value)
					{
						if (value == 0)
						{
							return true;
						}
						// validators run during ModConfigSpec.correct before childConfig is set,
						// so cross-references must use getDefault() instead of get()
						return value > 0 && shape.getDefault() == HeightShape.TRAPEZOID;
					}
					return false;
				});

		return new ConfigurableHeightRange(
				shape,
				minInclusive,
				ConfigurableVerticalAnchor.create(
						builder,
						"maxInclusive",
						"Maximum (inclusive) height anchor for " + veinType + "s.",
						baseConfig.max(),
						minInclusive),
				plat
		);
	}
}