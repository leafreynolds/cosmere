/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.api.Taldain;
import leaf.cosmere.common.compat.dynamickeybinds.PowerKeybinds;
import leaf.cosmere.common.registration.impl.ManifestationDeferredRegister;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.manifestation.*;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static leaf.cosmere.api.Constants.Strings.KEYS_CATEGORY;
import static leaf.cosmere.sandmastery.common.utils.SandmasteryConstants.*;

public class SandmasteryManifestations
{
	public static final ManifestationDeferredRegister MANIFESTATIONS = new ManifestationDeferredRegister(Sandmastery.MODID);

	public static final Map<Taldain.Mastery, ManifestationRegistryObject<SandmasteryManifestation>> SANDMASTERY_POWERS =
			Arrays.stream(Taldain.Mastery.values())
					.collect(Collectors.toMap(
							Function.identity(),
							investiture ->
									MANIFESTATIONS.register(
											investiture.getName(),
											() -> makeSandmasteryManifestation(investiture))
					));

	// Keybinds, gated behind the matching power when DynamicKeybinds is installed
	public static final PowerKeybinds.Def KEY_LAUNCH = key(KEY_SANDMASTERY_LAUNCH, Taldain.Mastery.LAUNCH);
	public static final PowerKeybinds.Def KEY_ELEVATE = key(KEY_SANDMASTERY_ELEVATE, Taldain.Mastery.ELEVATE);
	public static final PowerKeybinds.Def KEY_PROJECTILE = key(KEY_SANDMASTERY_PROJECTILE, Taldain.Mastery.PROJECTILE);
	public static final PowerKeybinds.Def KEY_PLATFORM = key(KEY_SANDMASTERY_PLATFORM, Taldain.Mastery.PLATFORM);

	private static PowerKeybinds.Def key(String name, Taldain.Mastery mastery)
	{
		return PowerKeybinds.add(name, GLFW.GLFW_KEY_UNKNOWN, KEYS_CATEGORY,
				spiritweb -> spiritweb.hasManifestation(SANDMASTERY_POWERS.get(mastery).getManifestation()));
	}


	private static SandmasteryManifestation makeSandmasteryManifestation(Taldain.Mastery mastery)
	{
		switch (mastery)
		{
			case LAUNCH:
				return new MasteryLaunch(mastery);
			case ELEVATE:
				return new MasteryElevate(mastery);
			case CUSHION:
				return new MasteryCushion(mastery);
			case PROJECTILE:
				return new MasteryProjectile(mastery);
			case PLATFORM:
				return new MasteryPlatform(mastery);
			default:
				return new SandmasteryManifestation(mastery);
		}
	}
}
