/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.compat.dynamickeybinds.PowerKeybinds;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.manifestation.*;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static leaf.cosmere.api.Constants.Strings.*;

public class SurgebindingManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Surgebinding.MODID);


	public static final Map<Roshar.Surges, ManifestationRegistryObject<Manifestation>> SURGEBINDING_POWERS =
			Arrays.stream(EnumUtils.SURGES)
					.collect(Collectors.toMap(
							Function.identity(),
							surge ->
									MANIFESTATIONS.register(
											surge.getName(),
											() -> makeSurgebindingManifestation(surge))
					));

	// Keybinds, gated behind the matching power when DynamicKeybinds is installed
	public static final String KEYS_SURGEBINDING_CATEGORY = "category.keys.surgebinding.main";

	public static final Map<Roshar.Surges, PowerKeybinds.Def> KEY_ACTIVATORS =
			SURGEBINDING_POWERS.keySet().stream()
					.collect(Collectors.toMap(
							Function.identity(),
							surge -> PowerKeybinds.add(KEY_STORMLIGHT + surge.getName(), GLFW.GLFW_KEY_UNKNOWN, KEYS_ACTIVATE_CATEGORY,
									spiritweb -> spiritweb.hasManifestation(SURGEBINDING_POWERS.get(surge).getManifestation()))
					));

	public static final PowerKeybinds.Def KEY_REQUEST_STORMLIGHT_DEF = PowerKeybinds.add(KEY_REQUEST_STORMLIGHT, GLFW.GLFW_KEY_Z, KEYS_SURGEBINDING_CATEGORY, SurgebindingManifestations::hasAnySurge);
	public static final PowerKeybinds.Def KEY_DISPATCH_STORMLIGHT_DEF = PowerKeybinds.add(KEY_DISPATCH_STORMLIGHT, GLFW.GLFW_KEY_Y, KEYS_SURGEBINDING_CATEGORY, SurgebindingManifestations::hasAnySurge);

	private static boolean hasAnySurge(ISpiritweb spiritweb)
	{
		return spiritweb.hasManifestationOfType(Manifestations.ManifestationTypes.SURGEBINDING);
	}


	private static SurgebindingManifestation makeSurgebindingManifestation(Roshar.Surges surge)
	{
		return switch (surge)
		{
			case ADHESION -> new SurgeAdhesion(surge);
			case GRAVITATION -> new SurgeGravitation(surge);
			case DIVISION -> new SurgeDivision(surge);
			case ABRASION -> new SurgeAbrasion(surge);
			case PROGRESSION -> new SurgeProgression(surge);
			case ILLUMINATION -> new SurgeIllumination(surge);
			case TRANSFORMATION -> new SurgeTransformation(surge);
			case TRANSPORTATION -> new SurgeTransportation(surge);
			case COHESION -> new SurgeCohesion(surge);
			case TENSION -> new SurgeTension(surge);
		};
	}
}
