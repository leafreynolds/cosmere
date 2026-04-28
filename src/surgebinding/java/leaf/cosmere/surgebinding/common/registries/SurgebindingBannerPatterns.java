package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SurgebindingBannerPatterns
{
	private SurgebindingBannerPatterns()
	{
	}

	public static final DeferredRegister<BannerPattern> BANNER_PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, Surgebinding.MODID);

	public static final RegistryObject<BannerPattern> WINDRUNNER = BANNER_PATTERNS.register("windrunner", () -> new BannerPattern("rwr"));
	public static final RegistryObject<BannerPattern> SKYBREAKER = BANNER_PATTERNS.register("skybreaker", () -> new BannerPattern("rsb"));
	public static final RegistryObject<BannerPattern> DUSTBRINGER = BANNER_PATTERNS.register("dustbringer", () -> new BannerPattern("rdb"));
	public static final RegistryObject<BannerPattern> EDGEDANCER = BANNER_PATTERNS.register("edgedancer", () -> new BannerPattern("red"));
	public static final RegistryObject<BannerPattern> TRUTHWATCHER = BANNER_PATTERNS.register("truthwatcher", () -> new BannerPattern("rtw"));
	public static final RegistryObject<BannerPattern> LIGHTWEAVER = BANNER_PATTERNS.register("lightweaver", () -> new BannerPattern("rlw"));
	public static final RegistryObject<BannerPattern> ELSECALLER = BANNER_PATTERNS.register("elsecaller", () -> new BannerPattern("rec"));
	public static final RegistryObject<BannerPattern> WILLSHAPER = BANNER_PATTERNS.register("willshaper", () -> new BannerPattern("rws"));
	public static final RegistryObject<BannerPattern> STONEWARD = BANNER_PATTERNS.register("stoneward", () -> new BannerPattern("rsw"));
	public static final RegistryObject<BannerPattern> BONDSMITH = BANNER_PATTERNS.register("bondsmith", () -> new BannerPattern("rbs"));

	public static final RegistryObject<BannerPattern> ABRASION = BANNER_PATTERNS.register("abrasion", () -> new BannerPattern("sab"));
	public static final RegistryObject<BannerPattern> ADHESION = BANNER_PATTERNS.register("adhesion", () -> new BannerPattern("sad"));
	public static final RegistryObject<BannerPattern> COHESION = BANNER_PATTERNS.register("cohesion", () -> new BannerPattern("sch"));
	public static final RegistryObject<BannerPattern> DIVISION = BANNER_PATTERNS.register("division", () -> new BannerPattern("sdv"));
	public static final RegistryObject<BannerPattern> GRAVITATION = BANNER_PATTERNS.register("gravitation", () -> new BannerPattern("sgt"));
	public static final RegistryObject<BannerPattern> ILLUMINATION = BANNER_PATTERNS.register("illumination", () -> new BannerPattern("sil"));
	public static final RegistryObject<BannerPattern> PROGRESSION = BANNER_PATTERNS.register("progression", () -> new BannerPattern("spg"));
	public static final RegistryObject<BannerPattern> TENSION = BANNER_PATTERNS.register("tension", () -> new BannerPattern("sts"));
	public static final RegistryObject<BannerPattern> TRANSFORMATION = BANNER_PATTERNS.register("transformation", () -> new BannerPattern("stf"));
	public static final RegistryObject<BannerPattern> TRANSPORTATION = BANNER_PATTERNS.register("transportation", () -> new BannerPattern("stp"));


}
