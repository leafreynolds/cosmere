/*
 * File updated ~ 7 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.tag;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.api.providers.IEntityTypeProvider;
import leaf.cosmere.common.registration.impl.GameEventRegistryObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

//Based off of Mekanism's implementation of BaseTagProvider, obtained 1st June, 2023
//This lets us make data jsons much easier, so todo is to move to this implementation for other tags and things
public abstract class BaseTagProvider implements DataProvider
{
	private final Map<ResourceKey<? extends Registry<?>>, Map<TagKey<?>, TagBuilder>> supportedTagTypes = new Object2ObjectLinkedOpenHashMap<>();
	private final Set<Block> knownHarvestRequirements = new ReferenceOpenHashSet<>();
	private final CompletableFuture<HolderLookup.Provider> lookupProvider;
	private final ExistingFileHelper existingFileHelper;
	private final PackOutput output;
	private final String modid;

	protected BaseTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modid, @Nullable ExistingFileHelper existingFileHelper)
	{
		this.output = output;
		this.modid = modid;
		this.lookupProvider = lookupProvider;
		this.existingFileHelper = existingFileHelper;
	}

	@NotNull
	@Override
	public String getName()
	{
		return "Tags: " + modid;
	}

	protected abstract void registerTags(HolderLookup.Provider registries);

	protected List<IBlockProvider> getAllBlocks()
	{
		return Collections.emptyList();
	}

	protected void hasHarvestData(Block block)
	{
		knownHarvestRequirements.add(block);
	}

	@NotNull
	@Override
	public CompletableFuture<?> run(@NotNull CachedOutput cache)
	{
		return this.lookupProvider.thenApply(registries ->
		{
			supportedTagTypes.values().forEach(Map::clear);
			registerTags(registries);
			return registries;
		}).thenCompose(registries ->
		{
			for (IBlockProvider blockProvider : getAllBlocks())
			{
				Block block = blockProvider.getBlock();
				if (block.defaultBlockState().requiresCorrectToolForDrops() && !knownHarvestRequirements.contains(block))
				{
					throw new IllegalStateException("Missing harvest tool type for block '" + RegistryHelper.getName(block) + "' that requires the correct tool for drops.");
				}
			}
			List<CompletableFuture<?>> futures = new ArrayList<>();
			supportedTagTypes.forEach((registry, tagTypeMap) ->
			{
				if (!tagTypeMap.isEmpty())
				{
					//Create a dummy provider and pass all our collected data through to it
					futures.add(new TagsProvider(output, registry, lookupProvider, modid, existingFileHelper)
					{
						@Override
						protected void addTags(@NotNull HolderLookup.Provider lookupProvider)
						{
							//Add each tag builder to the wrapped provider's builder
							tagTypeMap.forEach((tag, tagBuilder) -> builders.put(tag.location(), tagBuilder));
						}
					}.run(cache));
				}
			});
			return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
		});
	}

	private <TYPE> Map<TagKey<?>, TagBuilder> getTagTypeMap(ResourceKey<? extends Registry<TYPE>> registry)
	{
		return supportedTagTypes.computeIfAbsent(registry, type -> new Object2ObjectLinkedOpenHashMap<>());
	}

	private <TYPE> TagBuilder getTagBuilder(ResourceKey<? extends Registry<TYPE>> registry, TagKey<TYPE> tag)
	{
		return getTagTypeMap(registry).computeIfAbsent(tag, ignored -> TagBuilder.create());
	}

	protected <TYPE> CosmereTagBuilder<TYPE, ?> getBuilder(ResourceKey<? extends Registry<TYPE>> registry, TagKey<TYPE> tag)
	{
		return new CosmereTagBuilder<>(getTagBuilder(registry, tag), modid);
	}

	protected <TYPE> IntrinsicCosmereTagBuilder<TYPE> getBuilder(ResourceKey<? extends Registry<TYPE>> registry, Function<TYPE, ResourceKey<TYPE>> keyExtractor, TagKey<TYPE> tag)
	{
		return new IntrinsicCosmereTagBuilder<>(keyExtractor, getTagBuilder(registry, tag), modid);
	}

	protected <TYPE> IntrinsicCosmereTagBuilder<TYPE> getBuilder(IForgeRegistry<TYPE> registry, TagKey<TYPE> tag)
	{
		return new IntrinsicCosmereTagBuilder<>(element -> registry.getResourceKey(element).orElseThrow(), getTagBuilder(registry.getRegistryKey(), tag), modid);
	}

	protected IntrinsicCosmereTagBuilder<Item> getItemBuilder(TagKey<Item> tag)
	{
		return getBuilder(ForgeRegistries.ITEMS, tag);
	}

	protected IntrinsicCosmereTagBuilder<Block> getBlockBuilder(TagKey<Block> tag)
	{
		return getBuilder(ForgeRegistries.BLOCKS, tag);
	}

	protected IntrinsicCosmereTagBuilder<EntityType<?>> getEntityTypeBuilder(TagKey<EntityType<?>> tag)
	{
		return getBuilder(ForgeRegistries.ENTITY_TYPES, tag);
	}

	protected IntrinsicCosmereTagBuilder<Fluid> getFluidBuilder(TagKey<Fluid> tag)
	{
		return getBuilder(ForgeRegistries.FLUIDS, tag);
	}

	protected IntrinsicCosmereTagBuilder<BlockEntityType<?>> getTileEntityTypeBuilder(TagKey<BlockEntityType<?>> tag)
	{
		return getBuilder(ForgeRegistries.BLOCK_ENTITY_TYPES, tag);
	}

	protected IntrinsicCosmereTagBuilder<GameEvent> getGameEventBuilder(TagKey<GameEvent> tag)
	{
		return getBuilder(Registries.GAME_EVENT, gameEvent -> gameEvent.builtInRegistryHolder().key(), tag);
	}

	protected CosmereTagBuilder<DamageType, ?> getDamageTypeBuilder(TagKey<DamageType> tag)
	{
		return getBuilder(Registries.DAMAGE_TYPE, tag);
	}

	protected CosmereTagBuilder<Biome, ?> getBiomeBuilder(TagKey<Biome> tag)
	{
		return getBuilder(Registries.BIOME, tag);
	}

	protected IntrinsicCosmereTagBuilder<MobEffect> getMobEffectBuilder(TagKey<MobEffect> tag)
	{
		return getBuilder(ForgeRegistries.MOB_EFFECTS, tag);
	}

	protected void addToTag(TagKey<Item> tag, ItemLike... itemProviders)
	{
		getItemBuilder(tag).addTyped(ItemLike::asItem, itemProviders);
	}

	protected void addToTag(TagKey<Block> tag, IBlockProvider... blockProviders)
	{
		getBlockBuilder(tag).addTyped(IBlockProvider::getBlock, blockProviders);
	}

	@SafeVarargs
	protected final void addToTag(TagKey<Block> blockTag, Map<?, ? extends IBlockProvider>... blockProviders)
	{
		IntrinsicCosmereTagBuilder<Block> tagBuilder = getBlockBuilder(blockTag);
		for (Map<?, ? extends IBlockProvider> blockProvider : blockProviders)
		{
			for (IBlockProvider value : blockProvider.values())
			{
				tagBuilder.add(value.getBlock());
			}
		}
	}

	protected void addToHarvestTag(TagKey<Block> tag, IBlockProvider... blockProviders)
	{
		IntrinsicCosmereTagBuilder<Block> tagBuilder = getBlockBuilder(tag);
		for (IBlockProvider blockProvider : blockProviders)
		{
			Block block = blockProvider.getBlock();
			tagBuilder.add(block);
			hasHarvestData(block);
		}
	}

	@SafeVarargs
	protected final void addToHarvestTag(TagKey<Block> blockTag, Map<?, ? extends IBlockProvider>... blockProviders)
	{
		IntrinsicCosmereTagBuilder<Block> tagBuilder = getBlockBuilder(blockTag);
		for (Map<?, ? extends IBlockProvider> blockProvider : blockProviders)
		{
			for (IBlockProvider value : blockProvider.values())
			{
				Block block = value.getBlock();
				tagBuilder.add(block);
				hasHarvestData(block);
			}
		}
	}

	protected void addToTags(TagKey<Item> itemTag, TagKey<Block> blockTag, IBlockProvider... blockProviders)
	{
		IntrinsicCosmereTagBuilder<Item> itemTagBuilder = getItemBuilder(itemTag);
		IntrinsicCosmereTagBuilder<Block> blockTagBuilder = getBlockBuilder(blockTag);
		for (IBlockProvider blockProvider : blockProviders)
		{
			itemTagBuilder.add(blockProvider.asItem());
			blockTagBuilder.add(blockProvider.getBlock());
		}
	}

	protected void addToTag(TagKey<GameEvent> tag, GameEventRegistryObject<?>... gameEventROs)
	{
		getGameEventBuilder(tag).addTyped(GameEventRegistryObject::get, gameEventROs);
	}

	protected void addToTag(TagKey<EntityType<?>> tag, IEntityTypeProvider... entityTypeProviders)
	{
		getEntityTypeBuilder(tag).addTyped(IEntityTypeProvider::getEntityType, entityTypeProviders);
	}
}