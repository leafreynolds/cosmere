/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.biome;


import leaf.cosmere.BaseDatapackRegistryProvider;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.CosmereConfigs;
import leaf.cosmere.common.config.CosmereWorldConfig.OreVeinConfig;
import leaf.cosmere.common.registration.impl.FeatureRegistryObject;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.common.registry.CosmereDamageTypesRegistry;
import leaf.cosmere.common.registry.FeatureRegistry;
import leaf.cosmere.common.resource.ore.OreBlockType;
import leaf.cosmere.common.resource.ore.OreType;
import leaf.cosmere.common.resource.ore.OreType.OreVeinType;
import leaf.cosmere.common.util.CosmereEnumUtils;
import leaf.cosmere.common.world.ConfigurableConstantInt;
import leaf.cosmere.common.world.ResizableOreFeature;
import leaf.cosmere.common.world.ResizableOreFeatureConfig;
import leaf.cosmere.common.world.height.ConfigurableHeightProvider;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


//based on MekanismDatapackRegistryProvider from Mekanism
// https://github.com/mekanism/Mekanism/blob/7de496745c721fb15d00d590ddcacf00570f3f1b/src/datagen/main/java/mekanism/common/registries/MekanismDatapackRegistryProvider.java#L62
public class CosmereDatapackRegistryProvider extends BaseDatapackRegistryProvider
{
	public CosmereDatapackRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(output, lookupProvider, BUILDER, Cosmere.MODID);
	}

	private static final Map<OreType, List<OreConfiguration.TargetBlockState>> ORE_STONE_TARGETS = new EnumMap<>(OreType.class);
	private static final RuleTest STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
	private static final RuleTest DEEPSLATE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

	private static ConfiguredFeature<ResizableOreFeatureConfig, ResizableOreFeature> configureOreFeature(OreVeinType oreVeinType,
	                                                                                                     FeatureRegistryObject<ResizableOreFeatureConfig, ? extends ResizableOreFeature> featureRO)
	{
		OreVeinConfig oreVeinConfig = CosmereConfigs.WORLD_CONFIG.getVeinConfig(oreVeinType);
		List<OreConfiguration.TargetBlockState> targetStates = ORE_STONE_TARGETS.computeIfAbsent(oreVeinType.type(), oreType ->
		{
			OreBlockType oreBlockType = BlocksRegistry.METAL_ORE.get(oreType);
			return List.of(
					OreConfiguration.target(STONE_ORE_REPLACEABLES, oreBlockType.stoneBlock().defaultBlockState()),
					OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, oreBlockType.deepslateBlock().defaultBlockState())
			);
		});
		return new ConfiguredFeature<>(
				featureRO.get(),
				new ResizableOreFeatureConfig(
						targetStates,
						oreVeinType,
						oreVeinConfig.maxVeinSize(),
						oreVeinConfig.discardChanceOnAirExposure()
				)
		);
	}

	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, context ->
			{
				for (OreType type : CosmereEnumUtils.ORE_TYPES)
				{
					int features = type.getBaseConfigs().size();
					for (int vein = 0; vein < features; vein++)
					{
						OreVeinType oreVeinType = new OreVeinType(type, vein);
						ResourceLocation name = Cosmere.rl(oreVeinType.name());
						context.register(configuredFeature(name), configureOreFeature(oreVeinType, FeatureRegistry.ORE));
					}
				}
			})
			.add(Registries.PLACED_FEATURE, context ->
			{
				for (OreType type : CosmereEnumUtils.ORE_TYPES)
				{
					int features = type.getBaseConfigs().size();
					for (int vein = 0; vein < features; vein++)
					{
						OreVeinType oreVeinType = new OreVeinType(type, vein);
						OreVeinConfig oreVeinConfig = CosmereConfigs.WORLD_CONFIG.getVeinConfig(oreVeinType);
						ResourceLocation name = Cosmere.rl(oreVeinType.name());
						registerPlacedFeature(context, name, modifiers -> List.of(
								CountPlacement.of(new ConfigurableConstantInt(oreVeinType, oreVeinConfig.perChunk())),
								InSquarePlacement.spread(),
								HeightRangePlacement.of(ConfigurableHeightProvider.of(oreVeinType, oreVeinConfig)),
								BiomeFilter.biome()
						));
					}
				}
			})
			.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, context ->
			{
				HolderSet.Named<Biome> isTaggedCanSpawnOres = context.lookup(Registries.BIOME).getOrThrow(CosmereTags.Biomes.SPAWN_ORES);
				HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
				for (OreType type : CosmereEnumUtils.ORE_TYPES)
				{
					int features = type.getBaseConfigs().size();
					List<Holder.Reference<PlacedFeature>> placedVeins = new ArrayList<>(features);
					for (int vein = 0; vein < features; vein++)
					{
						OreVeinType oreVeinType = new OreVeinType(type, vein);
						ResourceLocation name = Cosmere.rl(oreVeinType.name());
						placedVeins.add(placedFeatures.getOrThrow(placedFeature(name)));
					}
					context.register(
							biomeModifier(Cosmere.rl(type.getSerializedName())),
							new BiomeModifiers.AddFeaturesBiomeModifier(
									isTaggedCanSpawnOres,
									HolderSet.direct(placedVeins),
									GenerationStep.Decoration.UNDERGROUND_ORES)
					);
				}
			})
			.add(Registries.DAMAGE_TYPE, context ->
			{
				for (CosmereDamageTypesRegistry.CosmereDamageType damageType : CosmereDamageTypesRegistry.DAMAGE_TYPES.values())
				{
					context.register(damageType.key(), new DamageType(damageType.getMsgId(), damageType.exhaustion()));
				}
			});
}