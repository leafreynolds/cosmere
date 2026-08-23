/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;

//banner patterns are a datapack registry in 1.21, so these are just keys.
public class SurgebindingBannerPatterns
{
	private SurgebindingBannerPatterns()
	{
	}

	private static ResourceKey<BannerPattern> key(String name)
	{
		return ResourceKey.create(Registries.BANNER_PATTERN, Surgebinding.rl(name));
	}

	public static final ResourceKey<BannerPattern> WINDRUNNER = key("windrunner");
	public static final ResourceKey<BannerPattern> SKYBREAKER = key("skybreaker");
	public static final ResourceKey<BannerPattern> DUSTBRINGER = key("dustbringer");
	public static final ResourceKey<BannerPattern> EDGEDANCER = key("edgedancer");
	public static final ResourceKey<BannerPattern> TRUTHWATCHER = key("truthwatcher");
	public static final ResourceKey<BannerPattern> LIGHTWEAVER = key("lightweaver");
	public static final ResourceKey<BannerPattern> ELSECALLER = key("elsecaller");
	public static final ResourceKey<BannerPattern> WILLSHAPER = key("willshaper");
	public static final ResourceKey<BannerPattern> STONEWARD = key("stoneward");
	public static final ResourceKey<BannerPattern> BONDSMITH = key("bondsmith");

	public static final ResourceKey<BannerPattern> ABRASION = key("abrasion");
	public static final ResourceKey<BannerPattern> ADHESION = key("adhesion");
	public static final ResourceKey<BannerPattern> COHESION = key("cohesion");
	public static final ResourceKey<BannerPattern> DIVISION = key("division");
	public static final ResourceKey<BannerPattern> GRAVITATION = key("gravitation");
	public static final ResourceKey<BannerPattern> ILLUMINATION = key("illumination");
	public static final ResourceKey<BannerPattern> PROGRESSION = key("progression");
	public static final ResourceKey<BannerPattern> TENSION = key("tension");
	public static final ResourceKey<BannerPattern> TRANSFORMATION = key("transformation");
	public static final ResourceKey<BannerPattern> TRANSPORTATION = key("transportation");

	public static final ResourceKey<BannerPattern>[] ALL = new ResourceKey[]
			{
					WINDRUNNER,
					SKYBREAKER,
					DUSTBRINGER,
					EDGEDANCER,
					TRUTHWATCHER,
					LIGHTWEAVER,
					ELSECALLER,
					WILLSHAPER,
					STONEWARD,
					BONDSMITH,
					ABRASION,
					ADHESION,
					COHESION,
					DIVISION,
					GRAVITATION,
					ILLUMINATION,
					PROGRESSION,
					TENSION,
					TRANSFORMATION,
					TRANSPORTATION
			};
}
