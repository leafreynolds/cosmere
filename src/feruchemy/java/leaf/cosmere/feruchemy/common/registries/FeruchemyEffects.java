/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.registries;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.MobEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.MobEffectRegistryObject;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import leaf.cosmere.feruchemy.common.effects.store.*;
import leaf.cosmere.feruchemy.common.effects.tap.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeruchemyEffects
{

	public static final MobEffectDeferredRegister EFFECTS = new MobEffectDeferredRegister(Feruchemy.MODID);

	public static final Map<Metals.MetalType, MobEffectRegistryObject<MobEffect>> TAPPING_EFFECTS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.collect(Collectors.toMap(
									Function.identity(),
									type -> EFFECTS.register(
											"tapping_" + type.getName(),
											() -> makeTappingEffect(type))
							)
					);

	public static final Map<Metals.MetalType, MobEffectRegistryObject<MobEffect>> STORING_EFFECTS =
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
				return new IronStoreEffect(metalType, MobEffectCategory.NEUTRAL);
			case STEEL:
				return (new SteelStoreEffect(metalType, MobEffectCategory.NEUTRAL));
			case PEWTER:
				return new PewterStoreEffect(metalType, MobEffectCategory.NEUTRAL);
			case ZINC:
				return new ZincStoreEffect(metalType, MobEffectCategory.NEUTRAL);
			case BRASS:
				return new BrassStoreEffect(metalType, MobEffectCategory.NEUTRAL);
			case DURALUMIN:
				return new DuraluminStoreEffect(metalType, MobEffectCategory.NEUTRAL);
			case CHROMIUM:
				return new ChromiumStoreEffect(metalType, MobEffectCategory.NEUTRAL);
			case NICROSIL:
				return new NicrosilStoreEffect(metalType, MobEffectCategory.NEUTRAL);
			case GOLD:
				return new GoldStoreEffect(metalType, MobEffectCategory.NEUTRAL);
			case CADMIUM:
				return new CadmiumStoreEffect(metalType, MobEffectCategory.NEUTRAL);
			case BENDALLOY:
				return new BendalloyStoreEffect(metalType, MobEffectCategory.NEUTRAL);
			case ATIUM:
				return new AtiumStoreEffect(metalType, MobEffectCategory.NEUTRAL);
			default:
				return new FeruchemyEffectBase(metalType, MobEffectCategory.NEUTRAL);
		}
	}

	private static MobEffect makeTappingEffect(Metals.MetalType metalType)
	{
		switch (metalType)
		{
			case IRON:
				return new IronTapEffect(metalType, MobEffectCategory.NEUTRAL);
			case STEEL:
				return new SteelTapEffect(metalType, MobEffectCategory.NEUTRAL);
			case PEWTER:
				return new PewterTapEffect(metalType, MobEffectCategory.NEUTRAL);
			case ZINC:
				return new ZincTapEffect(metalType, MobEffectCategory.NEUTRAL);
			case BRASS:
				return new BrassTapEffect(metalType, MobEffectCategory.NEUTRAL);
			case DURALUMIN:
				return new DuraluminTapEffect(metalType, MobEffectCategory.NEUTRAL);
			case CHROMIUM:
				return new ChromiumTapEffect(metalType, MobEffectCategory.NEUTRAL);
			case NICROSIL:
				return new NicrosilTapEffect(metalType, MobEffectCategory.NEUTRAL);
			case GOLD:
				return new GoldTapEffect(metalType, MobEffectCategory.NEUTRAL);
			case CADMIUM:
				return new CadmiumTapEffect(metalType, MobEffectCategory.NEUTRAL);
			case BENDALLOY:
				return new BendalloyTapEffect(metalType, MobEffectCategory.NEUTRAL);
			case ATIUM:
				return new AtiumTapEffect(metalType, MobEffectCategory.NEUTRAL);
			default:
				return new FeruchemyEffectBase(metalType, MobEffectCategory.NEUTRAL);
		}
	}
}
