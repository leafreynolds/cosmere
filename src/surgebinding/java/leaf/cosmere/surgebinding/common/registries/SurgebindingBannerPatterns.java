/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * 1.21.1 changes:
 *  - `BannerPattern` constructor: `(String hashname)` → `(ResourceLocation assetId, String translationKey)`.
 *  - DeferredRegister/RegistryObject moved to neoforged. RegistryObject → DeferredHolder.
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SurgebindingBannerPatterns
{
	private SurgebindingBannerPatterns()
	{
	}

	public static final DeferredRegister<BannerPattern> BANNER_PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, Surgebinding.MODID);

	public static final DeferredHolder<BannerPattern, BannerPattern> WINDRUNNER = register("windrunner");
	public static final DeferredHolder<BannerPattern, BannerPattern> SKYBREAKER = register("skybreaker");
	public static final DeferredHolder<BannerPattern, BannerPattern> DUSTBRINGER = register("dustbringer");
	public static final DeferredHolder<BannerPattern, BannerPattern> EDGEDANCER = register("edgedancer");
	public static final DeferredHolder<BannerPattern, BannerPattern> TRUTHWATCHER = register("truthwatcher");
	public static final DeferredHolder<BannerPattern, BannerPattern> LIGHTWEAVER = register("lightweaver");
	public static final DeferredHolder<BannerPattern, BannerPattern> ELSECALLER = register("elsecaller");
	public static final DeferredHolder<BannerPattern, BannerPattern> WILLSHAPER = register("willshaper");
	public static final DeferredHolder<BannerPattern, BannerPattern> STONEWARD = register("stoneward");
	public static final DeferredHolder<BannerPattern, BannerPattern> BONDSMITH = register("bondsmith");

	public static final DeferredHolder<BannerPattern, BannerPattern> ABRASION = register("abrasion");
	public static final DeferredHolder<BannerPattern, BannerPattern> ADHESION = register("adhesion");
	public static final DeferredHolder<BannerPattern, BannerPattern> COHESION = register("cohesion");
	public static final DeferredHolder<BannerPattern, BannerPattern> DIVISION = register("division");
	public static final DeferredHolder<BannerPattern, BannerPattern> GRAVITATION = register("gravitation");
	public static final DeferredHolder<BannerPattern, BannerPattern> ILLUMINATION = register("illumination");
	public static final DeferredHolder<BannerPattern, BannerPattern> PROGRESSION = register("progression");
	public static final DeferredHolder<BannerPattern, BannerPattern> TENSION = register("tension");
	public static final DeferredHolder<BannerPattern, BannerPattern> TRANSFORMATION = register("transformation");
	public static final DeferredHolder<BannerPattern, BannerPattern> TRANSPORTATION = register("transportation");

	private static DeferredHolder<BannerPattern, BannerPattern> register(String name)
	{
		ResourceLocation assetId = ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, name);
		String translationKey = "block.minecraft.banner." + Surgebinding.MODID + "." + name;
		return BANNER_PATTERNS.register(name, () -> new BannerPattern(assetId, translationKey));
	}
}
