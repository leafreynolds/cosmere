/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.common.world.height;

import leaf.cosmere.common.resource.ore.OreAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

// todo: this will require significant changes, and probably other worldgen stuff too
// new mekanism example: https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/world/height/ConfigurableVerticalAnchor.java
//based on ConfigurableVerticalAnchor from Mekanism
// https://github.com/mekanism/Mekanism/blob/7de496745c721fb15d00d590ddcacf00570f3f1b/src/main/java/mekanism/common/world/height/ConfigurableVerticalAnchor.java#L14
public record ConfigurableVerticalAnchor(Supplier<AnchorType> anchorType, Supplier<Integer> value)
{

	public static ConfigurableVerticalAnchor create(ModConfigSpec.Builder builder,
	                                                String path,
	                                                String comment,
	                                                OreAnchor defaultAnchor,
	                                                @Nullable ConfigurableVerticalAnchor minAnchor)
	{
		builder.comment(comment).push(path);
		ModConfigSpec.EnumValue<AnchorType> type = builder.comment(
						"Type of anchor.",
						"Absolute -> y = value",
						"Above Bottom -> y = minY + value",
						"Below Top -> y = depth - 1 + minY - value")
				.defineEnum("type", defaultAnchor.type());

		ModConfigSpec.Builder valueBuilder = builder.comment("Value used for calculating y for the anchor based on the type.");
		ModConfigSpec.ConfigValue<Integer> value;

		if (minAnchor == null)
		{
			value = valueBuilder.define("value", defaultAnchor.value());
		}
		else
		{
			value = valueBuilder.define("value", defaultAnchor.value(), o -> o instanceof Integer);
		}
		builder.pop();
		return new ConfigurableVerticalAnchor(type, value);
	}

	public int resolveY(WorldGenerationContext context)
	{
		return anchorType.get().resolveY(context, value.get());
	}

	@Override
	public String toString()
	{
		return switch (anchorType.get())
		{
			case ABSOLUTE -> value.get() + " absolute";
			case ABOVE_BOTTOM -> value.get() + " above bottom";
			case BELOW_TOP -> value.get() + " below top";
		};
	}
}
