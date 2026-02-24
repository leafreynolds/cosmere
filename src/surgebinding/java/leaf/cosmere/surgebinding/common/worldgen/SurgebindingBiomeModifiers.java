package leaf.cosmere.surgebinding.common.worldgen;

import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class SurgebindingBiomeModifiers
{
	public static final ResourceKey<BiomeModifier> ADD_SMOKESTONE_ORE =registerKey("add_smokestone_ore");

	public static void bootstrap(BootstapContext<BiomeModifier> context) {
		var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
		var biomes = context.lookup(Registries.BIOME);

		context.register(ADD_SMOKESTONE_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
				biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
				HolderSet.direct(placedFeatures.getOrThrow(SurgebindingPlacedFeatures.SMOKESTONE_ORE_PLACED_KEY)),
				GenerationStep.Decoration.UNDERGROUND_ORES));
	}


	private static ResourceKey<BiomeModifier> registerKey(String name) {
		return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Surgebinding.MODID, name));
	}
}
