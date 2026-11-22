/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.manifestation.*;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.compat.dynamickeybinds.PowerKeybinds;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static leaf.cosmere.api.Constants.Strings.*;

public class AllomancyManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Allomancy.MODID);


	// Allomancy
	public static final Map<Metals.MetalType, ManifestationRegistryObject<AllomancyManifestation>> ALLOMANCY_POWERS =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasAssociatedManifestation)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType ->
									MANIFESTATIONS.register(
											metalType.getName(),
											() -> makeAllomancyManifestation(metalType))//get the specific instance of the manifestation for allomancy
					));

	// Keybinds, gated behind the matching power when DynamicKeybinds is installed
	public static final Map<Metals.MetalType, PowerKeybinds.Def> KEY_ACTIVATORS =
			ALLOMANCY_POWERS.keySet().stream()
					.collect(Collectors.toMap(
							Function.identity(),
							metalType -> PowerKeybinds.add(KEY_ALLOMANCY + metalType.getName(), GLFW.GLFW_KEY_UNKNOWN, KEYS_ACTIVATE_CATEGORY, requires(metalType))
					));

	public static final PowerKeybinds.Def KEY_STEEL_PUSH = PowerKeybinds.add(KEY_ALLOMANCY_STEEL_PUSH, GLFW.GLFW_KEY_TAB, KEYS_CATEGORY, requires(Metals.MetalType.STEEL));
	public static final PowerKeybinds.Def KEY_IRON_PULL = PowerKeybinds.add(KEY_ALLOMANCY_IRON_PULL, GLFW.GLFW_KEY_R, KEYS_CATEGORY, requires(Metals.MetalType.IRON));
	public static final PowerKeybinds.Def KEY_SOOTHE = PowerKeybinds.add(KEY_ALLOMANCY_SOOTHE, GLFW.GLFW_KEY_UNKNOWN, KEYS_CATEGORY, requires(Metals.MetalType.BRASS));
	public static final PowerKeybinds.Def KEY_RIOT = PowerKeybinds.add(KEY_ALLOMANCY_RIOT, GLFW.GLFW_KEY_UNKNOWN, KEYS_CATEGORY, requires(Metals.MetalType.ZINC));

	private static Predicate<ISpiritweb> requires(Metals.MetalType metalType)
	{
		return spiritweb -> spiritweb.hasManifestation(ALLOMANCY_POWERS.get(metalType).getManifestation());
	}


	private static AllomancyManifestation makeAllomancyManifestation(Metals.MetalType metalType)
	{
		switch (metalType)
		{
			case IRON:
			case STEEL:
				return new AllomancyIronSteel(metalType);
			case TIN:
				return new AllomancyTin(metalType);
			case PEWTER://done?
				return new AllomancyPewter(metalType);
			case ZINC://done?
				return new AllomancyZinc(metalType);
			case BRASS://done?
				return new AllomancyBrass(metalType);
			case COPPER://done?
				return new AllomancyCopper(metalType);
			case BRONZE://done?
				return new AllomancyBronze(metalType);
			case ALUMINUM:// done?
				return new AllomancyAluminum(metalType);
			case DURALUMIN:
				return new AllomancyDuralumin(metalType);
			case CHROMIUM://done?
				return new AllomancyChromium(metalType);
			case NICROSIL:
				return new AllomancyNicrosil(metalType);
			case CADMIUM:
				return new AllomancyCadmium(metalType);
			case BENDALLOY://done?
				return new AllomancyBendalloy(metalType);
			case GOLD://shows your past //todo
				return new AllomancyGold(metalType);
			case ELECTRUM://shows your future // todo
				return new AllomancyElectrum(metalType);
			case ATIUM://prevents taking damage from others
				return new AllomancyAtium(metalType);
			case MALATIUM://shows other's past // todo
				return new AllomancyMalatium(metalType);
		}

		//others aren't valid allomancy manifestations... for now.
		return new AllomancyManifestation(metalType);
	}


}
