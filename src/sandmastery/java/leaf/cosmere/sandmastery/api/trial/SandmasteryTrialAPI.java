package leaf.cosmere.sandmastery.api.trial;

import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class SandmasteryTrialAPI
{
	public static final ResourceKey<Registry<DiemTrial>> TRIAL_REGISTRY_KEY = ResourceKey.createRegistryKey(Sandmastery.rl("diem_trial"));

	private static Registry<DiemTrial> registry;

	private SandmasteryTrialAPI()
	{
	}

	public static Registry<DiemTrial> trialRegistry()
	{
		return registry;
	}

	public static void setRegistry(Registry<DiemTrial> created)
	{
		registry = created;
	}

	@Nullable
	public static DiemTrial get(ResourceLocation id)
	{
		return registry.get(id);
	}

	public static String nameKey(ResourceLocation id)
	{
		return "diem_trial." + id.getNamespace() + "." + id.getPath();
	}

	public static String descriptionKey(ResourceLocation id)
	{
		return nameKey(id) + ".description";
	}
}
