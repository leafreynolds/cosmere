package leaf.cosmere.surgebinding;

import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.worldgen.SurgebindingBiomeModifiers;
import leaf.cosmere.surgebinding.common.worldgen.SurgebindingConfiguredFeatures;
import leaf.cosmere.surgebinding.common.worldgen.SurgebindingPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SurgebindingWorldGenProvider extends DatapackBuiltinEntriesProvider
{
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, SurgebindingConfiguredFeatures::boostrap)
			.add(Registries.PLACED_FEATURE, SurgebindingPlacedFeatures::bootstrap)
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, SurgebindingBiomeModifiers::bootstrap);

	public SurgebindingWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries, BUILDER, Set.of(Surgebinding.MODID));
	}
}
