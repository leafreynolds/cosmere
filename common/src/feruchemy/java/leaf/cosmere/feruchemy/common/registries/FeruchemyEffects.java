/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.registries;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.common.registration.impl.CosmereEffectDeferredRegister;
import leaf.cosmere.common.registration.impl.CosmereEffectRegistryObject;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import leaf.cosmere.feruchemy.common.effects.store.*;
import leaf.cosmere.feruchemy.common.effects.tap.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeruchemyEffects
{

	public static final CosmereEffectDeferredRegister EFFECTS = new CosmereEffectDeferredRegister(Feruchemy.MODID);

	public static final Map<Metals.MetalType, CosmereEffectRegistryObject<CosmereEffect>> TAPPING_EFFECTS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.collect(Collectors.toMap(
									Function.identity(),
									type -> EFFECTS.register(
											"tapping_" + type.getName(),
											() -> makeTappingEffect(type))
							)
					);

	public static final Map<Metals.MetalType, CosmereEffectRegistryObject<CosmereEffect>> STORING_EFFECTS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.collect(Collectors.toMap(
									Function.identity(),
									type -> EFFECTS.register(
											"storing_" + type.getName(),
											() -> makeStoringEffect(type))
							)
					);

	private static CosmereEffect makeStoringEffect(Metals.MetalType metalType)
	{
		switch (metalType)
		{
			case IRON:
				return new IronStoreEffect(metalType);
			case STEEL:
				return (new SteelStoreEffect(metalType));
			case PEWTER:
				return new PewterStoreEffect(metalType);
			case ZINC:
				return new ZincStoreEffect(metalType);
			case BRASS:
				return new BrassStoreEffect(metalType);
			case ALUMINUM:
				return new AluminumStoreEffect(metalType);
			case DURALUMIN:
				return new DuraluminStoreEffect(metalType);
			case CHROMIUM:
				return new ChromiumStoreEffect(metalType);
			case GOLD:
				return new GoldStoreEffect(metalType);
			case ELECTRUM:
				return new ElectrumStoreEffect(metalType);
			case CADMIUM:
				return new CadmiumStoreEffect(metalType);
			case BENDALLOY:
				return new BendalloyStoreEffect(metalType);
			case ATIUM:
				return new AtiumStoreEffect(metalType);
			default:
				return new FeruchemyEffectBase(metalType);
		}
	}

	private static CosmereEffect makeTappingEffect(Metals.MetalType metalType)
	{
		switch (metalType)
		{
			case IRON:
				return new IronTapEffect(metalType);
			case STEEL:
				return new SteelTapEffect(metalType);
			case PEWTER:
				return new PewterTapEffect(metalType);
			case ZINC:
				return new ZincTapEffect(metalType);
			case BRASS:
				return new BrassTapEffect(metalType);
			case ALUMINUM:
				return new AluminumTapEffect(metalType);
			case DURALUMIN:
				return new DuraluminTapEffect(metalType);
			case CHROMIUM:
				return new ChromiumTapEffect(metalType);
			case GOLD:
				return new GoldTapEffect(metalType);
			case ELECTRUM:
				return new ElectrumTapEffect(metalType);
			case CADMIUM:
				return new CadmiumTapEffect(metalType);
			case BENDALLOY:
				return new BendalloyTapEffect(metalType);
			case ATIUM:
				return new AtiumTapEffect(metalType);
			default:
				return new FeruchemyEffectBase(metalType);
		}
	}
}
