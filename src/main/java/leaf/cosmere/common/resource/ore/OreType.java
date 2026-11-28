/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.common.resource.ore;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.world.height.HeightShape;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.List;


//based on OreType from Mekanism
// https://github.com/mekanism/Mekanism/blob/7de496745c721fb15d00d590ddcacf00570f3f1b/src/main/java/mekanism/common/resource/ore/OreType.java#L14
public enum OreType implements StringRepresentable
{
	TIN(Metals.MetalType.TIN,
			new BaseOreConfig("small", 14, 0, 4, HeightShape.TRAPEZOID, OreAnchor.absolute(-20), OreAnchor.absolute(94)),
			new BaseOreConfig("medium", 12, 0, 7, HeightShape.TRAPEZOID, OreAnchor.absolute(-32), OreAnchor.absolute(72)),
			new BaseOreConfig("abundant", 20, 0, 6, HeightShape.UNIFORM, OreAnchor.aboveBottom(-64), OreAnchor.aboveBottom(32))
	),
	ZINC(Metals.MetalType.ZINC,
			new BaseOreConfig("upper", 65, 0, 7, HeightShape.TRAPEZOID, OreAnchor.absolute(72), OreAnchor.belowTop(-24), 8),
			new BaseOreConfig("middle", 6, 0, 9, HeightShape.TRAPEZOID, OreAnchor.absolute(-32), OreAnchor.absolute(56)),
			new BaseOreConfig("deep", 10, 0.2F, 10, HeightShape.TRAPEZOID, OreAnchor.aboveBottom(-80), OreAnchor.absolute(-32))
	),
	ALUMINUM(Metals.MetalType.ALUMINUM,
			new BaseOreConfig("small", 4, 0, 4, HeightShape.TRAPEZOID, OreAnchor.aboveBottom(0), OreAnchor.absolute(8)),
			new BaseOreConfig("medium", 8, 0, 4, HeightShape.UNIFORM, OreAnchor.aboveBottom(-60), OreAnchor.absolute(-50)),
			new BaseOreConfig("buried", 13, 0.75F, 9, HeightShape.TRAPEZOID, OreAnchor.aboveBottom(-64), OreAnchor.aboveBottom(0), 16)
	),
	CHROMIUM(Metals.MetalType.CHROMIUM,
			new BaseOreConfig("rare", 4, 0.1F, 6, HeightShape.TRAPEZOID, OreAnchor.aboveBottom(-40), OreAnchor.absolute(16)),
			new BaseOreConfig("normal", 8, 0.25F, 9, HeightShape.TRAPEZOID, OreAnchor.aboveBottom(-24), OreAnchor.absolute(64)),
			new BaseOreConfig("deep", 5, 0.3F, 10, HeightShape.TRAPEZOID, OreAnchor.aboveBottom(-80), OreAnchor.absolute(-64))
	),

	CADMIUM(Metals.MetalType.CADMIUM,
			new BaseOreConfig("normal", 8, 0.25F, 9, HeightShape.TRAPEZOID, OreAnchor.aboveBottom(-24), OreAnchor.absolute(64)),
			new BaseOreConfig("abundant", 15, 0, 7, HeightShape.UNIFORM, OreAnchor.aboveBottom(-64), OreAnchor.absolute(-32)),
			new BaseOreConfig("deep", 6, 0.2F, 5, HeightShape.TRAPEZOID, OreAnchor.aboveBottom(-96), OreAnchor.absolute(-64))
	),
	NICKEL(Metals.MetalType.NICKEL,
			new BaseOreConfig("upper", 65, 0, 7, HeightShape.TRAPEZOID, OreAnchor.absolute(80), OreAnchor.absolute(384), 8),
			new BaseOreConfig("middle", 6, 0, 9, HeightShape.TRAPEZOID, OreAnchor.absolute(-32), OreAnchor.absolute(56)),
			new BaseOreConfig("small", 8, 0, 4, HeightShape.UNIFORM, OreAnchor.absolute(0), OreAnchor.absolute(64))
	),

	SILVER(Metals.MetalType.SILVER,
			new BaseOreConfig("normal", 8, 0.25F, 9, HeightShape.TRAPEZOID, OreAnchor.aboveBottom(-24), OreAnchor.absolute(64)),
			new BaseOreConfig("abundant", 16, 0, 7, HeightShape.UNIFORM, OreAnchor.aboveBottom(-64), OreAnchor.absolute(16)),
			new BaseOreConfig("rich", 12, 0.1F, 10, HeightShape.TRAPEZOID, OreAnchor.absolute(-64), OreAnchor.absolute(-32))
	),
	LEAD(Metals.MetalType.LEAD,
			new BaseOreConfig("normal", 8, 0.25F, 9, HeightShape.TRAPEZOID, OreAnchor.aboveBottom(-24), OreAnchor.absolute(64)),
			new BaseOreConfig("abundant", 15, 0.1F, 7, HeightShape.UNIFORM, OreAnchor.aboveBottom(-64), OreAnchor.absolute(-32)),
			new BaseOreConfig("deep", 10, 0.3F, 6, HeightShape.TRAPEZOID, OreAnchor.aboveBottom(-80), OreAnchor.absolute(-48))
	);


	public static Codec<OreType> CODEC = StringRepresentable.fromEnum(OreType::values);

	private final List<BaseOreConfig> baseConfigs;
	private final Metals.MetalType metalType;

	OreType(Metals.MetalType metalType, BaseOreConfig... configs)
	{
		this.metalType = metalType;
		this.baseConfigs = List.of(configs);
	}

	public Metals.MetalType getMetalType()
	{
		return metalType;
	}

	public List<BaseOreConfig> getBaseConfigs()
	{
		return baseConfigs;
	}

	public static OreType get(Metals.MetalType resource)
	{
		for (OreType ore : values())
		{
			if (resource == ore.metalType)
			{
				return ore;
			}
		}
		return null;
	}

	@NotNull
	@Override
	public String getSerializedName()
	{
		return metalType.getName();
	}

	public record OreVeinType(OreType type, int index)
	{

		public static final Codec<OreVeinType> CODEC = RecordCodecBuilder.create(builder -> builder.group(
				OreType.CODEC.fieldOf("type").forGetter(config -> config.type),
				Codec.INT.fieldOf("index").forGetter(config -> config.index)
		).apply(builder, OreVeinType::new));

		public OreVeinType
		{
			if (index < 0 || index >= type.getBaseConfigs().size())
			{
				throw new IndexOutOfBoundsException("Vein Type index out of range: " + index);
			}
		}

		public String name()
		{
			return "ore_" + type.getSerializedName() + "_" + type.getBaseConfigs().get(index).name();
		}
	}
}
