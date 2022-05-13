/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.allomancy.AllomancyBoostEffect;
import leaf.cosmere.effects.allomancy.AllomancyEffectBase;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import leaf.cosmere.effects.feruchemy.store.*;
import leaf.cosmere.effects.feruchemy.tap.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EffectsRegistry
{

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Cosmere.MODID);

	public static final RegistryObject<MobEffect> ALLOMANTIC_COPPER = EFFECTS.register(
			"copper_cloud",
			() -> new AllomancyEffectBase(Metals.MetalType.COPPER, MobEffectCategory.BENEFICIAL));

	public static final RegistryObject<MobEffect> ALLOMANCY_BOOST = EFFECTS.register(
			"allomancy_boost",
			() -> new AllomancyBoostEffect(Metals.MetalType.DURALUMIN, MobEffectCategory.BENEFICIAL));

	public static final Map<Metals.MetalType, RegistryObject<MobEffect>> TAPPING_EFFECTS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> EFFECTS.register(
									"tapping_" + type.getName(),
									() -> makeTappingEffect(type))
							)
					);

	public static final Map<Metals.MetalType, RegistryObject<MobEffect>> STORING_EFFECTS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> EFFECTS.register(
									"storing_" + type.getName(),
									() -> makeStoringEffect(type))
							)
					);


	private static MobEffect makeStoringEffect(Metals.MetalType metalType)
	{
		switch (metalType)
		{
			case IRON:
				return new IronStoreEffect(metalType, MobEffectCategory.BENEFICIAL);
			case STEEL:
				return (new SteelStoreEffect(metalType, MobEffectCategory.BENEFICIAL));
			case PEWTER:
				return new PewterStoreEffect(metalType, MobEffectCategory.BENEFICIAL);
			case BRASS:
				return new BrassStoreEffect(metalType, MobEffectCategory.BENEFICIAL);
			case DURALUMIN:
				return new DuraluminStoreEffect(metalType, MobEffectCategory.BENEFICIAL);
			case CHROMIUM:
				return new ChromiumStoreEffect(metalType, MobEffectCategory.BENEFICIAL);
			case GOLD:
				return new GoldStoreEffect(metalType, MobEffectCategory.BENEFICIAL);
			case CADMIUM:
				return new CadmiumStoreEffect(metalType, MobEffectCategory.BENEFICIAL);
			case BENDALLOY:
				return new BendalloyStoreEffect(metalType, MobEffectCategory.BENEFICIAL);
			//todo atium
			// handled as part of the manifestation or maybe a mixin
			default:
				return new FeruchemyEffectBase(metalType, MobEffectCategory.BENEFICIAL);
		}
	}

	private static MobEffect makeTappingEffect(Metals.MetalType metalType)
	{
		switch (metalType)
		{
			case IRON:
				return new IronTapEffect(metalType, MobEffectCategory.BENEFICIAL);
			case STEEL:
				return new SteelTapEffect(metalType, MobEffectCategory.BENEFICIAL);
			case PEWTER:
				return new PewterTapEffect(metalType, MobEffectCategory.BENEFICIAL);
			case BRASS:
				return new BrassTapEffect(metalType, MobEffectCategory.BENEFICIAL);
			case DURALUMIN:
				return new DuraluminTapEffect(metalType, MobEffectCategory.BENEFICIAL);
			case CHROMIUM:
				return new ChromiumTapEffect(metalType, MobEffectCategory.BENEFICIAL);
			case GOLD:
				return new GoldTapEffect(metalType, MobEffectCategory.BENEFICIAL);
			case CADMIUM:
				return new CadmiumTapEffect(metalType, MobEffectCategory.BENEFICIAL);
			case BENDALLOY:
				return new BendalloyTapEffect(metalType, MobEffectCategory.BENEFICIAL);
			//todo atium
			// handled as part of the manifestation or maybe a mixin
			default:
				return new FeruchemyEffectBase(metalType, MobEffectCategory.BENEFICIAL);
		}
	}
}
