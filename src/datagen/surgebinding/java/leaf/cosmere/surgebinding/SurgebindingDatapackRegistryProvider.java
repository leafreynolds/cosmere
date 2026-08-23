package leaf.cosmere.surgebinding;

import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBannerPatterns;
import leaf.cosmere.surgebinding.common.worldgen.SurgebindingBiomeModifiers;
import leaf.cosmere.surgebinding.common.worldgen.SurgebindingConfiguredFeatures;
import leaf.cosmere.surgebinding.common.worldgen.SurgebindingPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SurgebindingDatapackRegistryProvider extends DatapackBuiltinEntriesProvider
{
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, SurgebindingConfiguredFeatures::boostrap)
			.add(Registries.PLACED_FEATURE, SurgebindingPlacedFeatures::bootstrap)
			.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, SurgebindingBiomeModifiers::bootstrap)
			.add(Registries.BANNER_PATTERN, context ->
			{
				for (ResourceKey<BannerPattern> key : SurgebindingBannerPatterns.ALL)
				{
					final String name = key.location().getPath();
					context.register(key, new BannerPattern(
							Surgebinding.rl(name),
							"block.minecraft.banner." + name + "." + Surgebinding.MODID));
				}
			});

	public SurgebindingDatapackRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries, BUILDER, Set.of(Surgebinding.MODID));
	}
}
