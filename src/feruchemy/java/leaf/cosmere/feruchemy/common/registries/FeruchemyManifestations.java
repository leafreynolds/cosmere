/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.registries;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.compat.dynamickeybinds.PowerKeybinds;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.manifestation.*;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static leaf.cosmere.api.Constants.Strings.*;

public class FeruchemyManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Feruchemy.MODID);


	// Feruchemy powers
	public static final Map<Metals.MetalType, ManifestationRegistryObject<FeruchemyManifestation>> FERUCHEMY_POWERS =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasAssociatedManifestation)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType ->
									MANIFESTATIONS.register(
											metalType.getName(),
											() -> makeFeruchemyManifestation(metalType))//feruchemy can be base manifestation type because it mostly functions through effects
					));

	// Keybinds, gated behind the matching power when DynamicKeybinds is installed
	public static final Map<Metals.MetalType, PowerKeybinds.Def> KEY_ACTIVATORS =
			FERUCHEMY_POWERS.keySet().stream()
					.collect(Collectors.toMap(
							Function.identity(),
							metalType -> PowerKeybinds.add(KEY_FERUCHEMY + metalType.getName(), GLFW.GLFW_KEY_UNKNOWN, KEYS_ACTIVATE_CATEGORY,
									spiritweb -> spiritweb.hasManifestation(FERUCHEMY_POWERS.get(metalType).getManifestation()))
					));

	private static FeruchemyManifestation makeFeruchemyManifestation(Metals.MetalType metalType)
	{
		switch (metalType)
		{
			case COPPER:
				return new FeruchemyCopper(metalType);
			case BRONZE:
				return new FeruchemyBronze(metalType);
			case ALUMINUM:
				return new FeruchemyAluminum(metalType);
			case NICROSIL:
				return new FeruchemyNicrosil(metalType);
			case GOLD:
				return new FeruchemyGold(metalType);
			case BENDALLOY:
				return new FeruchemyBendalloy(metalType);
			case ATIUM:
				return new FeruchemyAtium(metalType);
			default:
				return new FeruchemyManifestation(metalType);
		}
	}

}
