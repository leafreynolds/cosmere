# Cosmere 1.20.1 Forge → 1.21.1 NeoForge Port Status

Branch: `feature/1.21.1/1.20.1_to_1.21.1_port`
Started: 2026-04-23

## Target stack

| Component | Version |
|---|---|
| Minecraft | 1.21.1 |
| Loader | NeoForge 21.1.172 |
| Java | 21 |
| Gradle plugin | `net.neoforged.moddev` 2.0.141 |
| Parchment | `2024.11.17-1.21.1` |
| Curios | `9.5.1+1.21.1` |
| JEI | `19.27.0.340` |
| Patchouli | `1.21.1-92` |
| Jade | `15.10.5+neoforge` |

---

## Done

### Phase 0 — Build system
- `gradle.properties` — Java 17→21, Forge→NeoForge version vars, parchment split into `parchment_minecraft`/`parchment_version`, dep versions updated.
- `settings.gradle` — dropped ForgeGradle/Mixingradle `resolutionStrategy`; added NeoForged plugin maven.
- `build.gradle` — full rewrite:
  - Plugins: `net.minecraftforge.gradle` + parchment-forgegradle + `org.spongepowered.mixin` → `net.neoforged.moddev`.
  - `minecraft { … }` + `setupRunConfig()` helper replaced by `neoForge { version, parchment, accessTransformers, runs, mods }` block.
  - Every secondary module registered as its own mod via `neoForge.mods { … }`.
  - Dropped `fg.deobf()` wrappers, `createReobf` machinery, `reobfJar` finalizer, `mixin { … }` block (MixinGradle no longer used — NeoForge reads mixin configs from `neoforge.mods.toml`).
  - Dep coordinates: `curios-neoforge`, `jei-*-neoforge[-api]`, `Patchouli:…-NEOFORGE`, `maven.modrinth:jade:…`.
  - `processResources` in `setupTasks` expands `META-INF/neoforge.mods.toml` (not `mods.toml`).
  - Publishing: CurseForge `addModLoader("Forge")` dropped; Modrinth `loaders=neoforge`, `gameVersions=[1.21.1]`.

### Phase 0.5 — `mods.toml` → `neoforge.mods.toml`
All 12 modules converted and old files deleted:
- `src/{main,allomancy,feruchemy,hemalurgy,surgebinding,sandmastery,awakening,aondor,aviar,soulforgery,cosmeretools,example}/resources/META-INF/neoforge.mods.toml`
- Changes: `mandatory = true/false` → `type = "required"/"optional"`; `[[mixins]] config = "<modid>.mixins.json"` appended to each.
- Main cosmere file additionally: `modId = "forge"` → `modId = "neoforge"` with `${neoforge_version}`; explicit `ordering = "NONE"` on minecraft/neoforge deps.
- Submodules kept original dep structure (only declare `cosmere` + siblings).

### Phase 1 — Foundational Java + registration layer
- `src/main/java/leaf/cosmere/common/Cosmere.java` — new `@Mod` ctor `(IEventBus modBus, ModContainer modContainer)`; imports moved `net.minecraftforge.*` → `net.neoforged.*`; `ResourceLocation.fromNamespaceAndPath`; removed `onAddCaps` listener; `packetHandler.register(modBus)` stub call added.
- `src/main/java/leaf/cosmere/common/registration/WrappedDeferredRegister.java` — dropped `IForgeRegistry<T>` ctor; `RegistryObject` → `DeferredHolder<T,I>`; `.hasTags().setDefaultKey(...)` → `.defaultKey(...)`; `makeRegistry` takes `Consumer<RegistryBuilder<T>>`.
- `src/main/java/leaf/cosmere/common/registration/WrappedRegistryObject.java` — `RegistryObject<T>` → `DeferredHolder<?, T>`.
- `src/main/java/leaf/cosmere/common/registration/DoubleDeferredRegister.java` — dropped `IForgeRegistry` ctor; `RegistryObject` → `DeferredHolder<P,?>/DeferredHolder<S,?>` in register signatures; eventbus import moved.
- `src/main/java/leaf/cosmere/common/registration/DoubleWrappedRegistryObject.java` — `RegistryObject<T>` → `DeferredHolder<?, T>`.
- `src/main/java/leaf/cosmere/common/registration/impl/*` (46 files) — all `*RegistryObject.java` swapped `RegistryObject<T>` → `DeferredHolder<?, T>`; all `*DeferredRegister.java` swapped `ForgeRegistries.X` → `Registries.X` (or `NeoForgeRegistries.Keys.X` for biome modifier + global loot modifier serializers). Notable:
  - `ItemDeferredRegister` — `ForgeSpawnEggItem` → `net.neoforged.neoforge.common.DeferredSpawnEggItem`.
  - `CreativeTabDeferredRegister` — `BuildCreativeModeTabContentsEvent` import moved to `net.neoforged.neoforge.event.*`.
  - `EntityTypeDeferredRegister` — `EntityAttributeCreationEvent` import moved to `net.neoforged.neoforge.event.entity.*`.
  - `BiomeModifierSerializer*` / `GlobalLootModifier*` — `Codec<T>` → `MapCodec<T>` (required by the NeoForge registry value type; normally Phase 9 work, pulled forward for type coherence).
  - `HeightProviderTypeDeferredRegister` / `IntProviderTypeDeferredRegister` — codec params switched to `MapCodec` for the same reason (`HeightProviderType` / `IntProviderType` are SAMs returning `MapCodec` in 1.21.1).
  - `GameEventDeferredRegister` — `new GameEvent(String, int)` → `new GameEvent(int)` (name arg removed in 1.21.1; the registry id is the identifier now).
  - `VillagerProfessionDeferredRegister` — `poi.getRegistryObject().getHolder().get()` → `poi.getRegistryObject()` (DeferredHolder *is* a Holder, no Optional unwrap).
  - `StatDeferredRegister` — `new ResourceLocation(name)` → `ResourceLocation.parse(name)`.

### Phase 2 — Registry classes (`common/registry/`)
All 22 files scanned; only 5 required edits (the rest consume `*RegistryObject` / `*DeferredRegister` wrappers already ported in Phase 1):
- `ArgumentTypeRegistry` — swapped raw `DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, …) / RegistryObject<>` for `DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, …) / DeferredHolder<>`; dropped unused `IEventBus` import.
- `LootModifiersRegistry` — generic param `Codec<? extends IGlobalLootModifier>` → `MapCodec<? extends IGlobalLootModifier>`; import moved to `net.neoforged.neoforge.common.loot.IGlobalLootModifier`. NOTE: `FortuneBonusModifier.CODEC` is still a `Codec` supplier and will not compile until Phase 9 flips it to `MapCodec`.
- `ManifestationRegistry` — `new ResourceLocation(location)` → `ResourceLocation.parse(location)`.
- `CreativeTabsRegistry` — `BuildCreativeModeTabContentsEvent` import moved to `net.neoforged.neoforge.event.*`.
- `BiomeRegistry` — Mojang typo-fix: `BootstapContext` → `BootstrapContext` (import + both method signatures).

### Phase 3 — Config (main source set only)
All 7 files in `src/main/java/leaf/cosmere/common/config/` ported; plus three tightly-coupled consumers in `common/world/`:
- `ICosmereConfig` — `ForgeConfigSpec` → `ModConfigSpec` on `getConfigSpec()` return type; imports moved (`net.minecraftforge.common.ForgeConfigSpec` → `net.neoforged.neoforge.common.ModConfigSpec`, `net.minecraftforge.fml.config.ModConfig` → `net.neoforged.fml.config.ModConfig`).
- `CosmereClientConfig`, `CosmereServerConfig`, `CosmereWorldConfig` — every `ForgeConfigSpec.*` reference → `ModConfigSpec.*`; imports moved; no behavior changes.
- `CosmereConfigs.registerConfigs(ModLoadingContext)` → `registerConfigs(ModContainer)` (matches the value `Cosmere.java` already passes in Phase 1). Dropped `ModLoadingContext.getActiveContainer()` hop.
- `CosmereConfigHelper` — now calls `modContainer.registerConfig(type, spec, fileName)` directly (still respects the `ICosmereConfig.addToContainer()` skip flag; still prefixes path with `cosmere/`).
- **Deleted `CosmereModConfig.java`.** The class extended `net.minecraftforge.fml.config.ModConfig` and overrode `getHandler()` to a custom `ConfigFileTypeHandler` (Mekanism trick, to move `SERVER`-type configs from `saves/<world>/serverconfig/` into the top-level `config/cosmere/` folder). In NeoForge 1.21.1 both `ModConfig`'s constructor and the `ConfigFileTypeHandler` class are no longer exposed — this path is closed. Consequence: **`SERVER`-type configs now live at `saves/<world>/serverconfig/cosmere/…` (per-world)**, matching modern NeoForge convention.
- `common/Cosmere.java` — `onConfigLoad`/`onConfigReload` can no longer `instanceof CosmereModConfig`. Rewrote both to dispatch through a shared `handleConfigEvent` that matches `event.getConfig().getSpec() == cosmereConfig.getConfigSpec()` against the three known `ICosmereConfig` singletons, then calls `clearCache()`. Also bound `onConfigLoad` to `ModConfigEvent.Loading` specifically.
- `common/world/ResizableOreFeatureConfig.java`, `common/world/ConfigurableConstantInt.java`, `common/world/height/ConfigurableHeightRange.java`, `common/world/height/ConfigurableVerticalAnchor.java` — `ForgeConfigSpec.{ConfigValue,Builder,EnumValue}` → `ModConfigSpec.*` (consume the `CosmereWorldConfig` spec values, so they had to move with it).
- Note on `Type.SERVER` sync: `CosmereWorldConfig.addToContainer()` still returns `false`, so the world spec is never registered with the mod container. Its `ConfigValue`s therefore return **default values only** (same as before — the old code built but didn't register this spec either). If the world config needs to become per-world-editable, flip `addToContainer()` to `true` in a future pass.

### Phase 4 — Capabilities → Attachments (ISpiritweb only)
The player/villager/etc. `ISpiritweb` capability is now a NeoForge data attachment. The remaining world-level / item-level capabilities (Scadrial, Roshar, Hemalurgy world caps, DynamicShardbladeData, CoinPouch/SandPouch inventories) are untouched — they stay Forge-era stubs until their submodule port passes.
- **New `src/main/java/leaf/cosmere/common/cap/entity/SpiritwebAttachments.java`** — `DeferredRegister<AttachmentType<?>>` on `NeoForgeRegistries.ATTACHMENT_TYPES`; single entry `"spiritweb"` registered via `AttachmentType.serializable(holder -> new SpiritwebCapability((LivingEntity) holder)).build()`. `copyOnDeath()` deliberately NOT set — the explicit `SpiritwebCapability.onPlayerClone` handler (to be re-wired via `PlayerEvent.Clone` in Phase 6) remains the single source of truth for post-death state transfer and `ISpiritwebSubmodule#resetOnDeath` dispatch.
- `src/main/java/leaf/cosmere/common/cap/entity/SpiritwebCapability.java` —
  - Dropped `Capability<ISpiritweb> CAPABILITY` field + `CapabilityManager` / `CapabilityToken` imports.
  - `get(LivingEntity)` now returns `java.util.Optional<ISpiritweb>` (was `LazyOptional<ISpiritweb>`), gated by `isValidSpiritWebEntity`; calls `entity.getData(SpiritwebAttachments.SPIRITWEB.get())`.
  - `isValidSpiritWebEntity(Entity)` moved here from the deleted `CapabilitiesHandler`.
  - `INBTSerializable` (NeoForge flavour) method signatures updated: `serializeNBT(HolderLookup.Provider)` / `deserializeNBT(HolderLookup.Provider, CompoundTag)`.
  - Internal callers (`syncToClients`, `onPlayerClone`) source the provider from `livingEntity.level().registryAccess()`.
  - `@OnlyIn` / `Dist` imports moved `net.minecraftforge.api.distmarker.*` → `net.neoforged.api.distmarker.*`; `RenderLevelStageEvent` and `PlayerEvent` likewise repathed.
- `src/api/java/leaf/cosmere/api/spiritweb/ISpiritweb.java` + `src/api/java/leaf/cosmere/api/ISpiritwebSubmodule.java` — `INBTSerializable`, `RenderLevelStageEvent`, `PlayerEvent`, `Dist`, `OnlyIn` imports all moved to the NeoForge packages. No method signatures changed at the API level — the new `HolderLookup.Provider` params come from the updated `INBTSerializable` parent.
- **Deleted `src/main/java/leaf/cosmere/common/eventHandlers/CapabilitiesHandler.java`.** `AttachCapabilitiesEvent` no longer exists; attachments auto-attach on first access.
- `src/main/java/leaf/cosmere/common/Cosmere.java` — `SpiritwebAttachments.ATTACHMENT_TYPES.register(modBus)` wired alongside the other `DeferredRegister`s; Phase 4 TODO comment removed.
- Consumer fixes (files that stored `LazyOptional<ISpiritweb>` explicitly or used `LazyOptional`-specific idioms — most `.ifPresent(...)` callsites ported transparently because `Optional` implements the same method):
  - `src/allomancy/.../AllomancyAtium.java` — `LazyOptional<ISpiritweb>` → `Optional<ISpiritweb>`; `LazyOptional` import removed.
  - `src/allomancy/.../capabilities/world/ScadrialCapability.java` — same; `.resolve()` dropped (the value is already an `Optional`). The rest of that file (its own Forge-era world capability) stays until its submodule pass.
  - `src/main/java/leaf/cosmere/mixin/LightTextureMixin.java` — same.
  - `src/surgebinding/.../SurgeGravitation.java` — `.resolve().get()` → `.get()`.
- `src/main/java/leaf/cosmere/common/network/packets/SyncPlayerSpiritwebMessage.java` — `c.deserializeNBT(entityNBT)` → `c.deserializeNBT(result.level().registryAccess(), entityNBT)` to match the new `INBTSerializable` shape. The rest of the packet file (`SimpleChannel` / `NetworkEvent.Context` plumbing) is still broken — that's Phase 5 work.

Notes:
- `renderSelectedHUD` in `SpiritwebCapability.java` still uses pre-1.21 render APIs (`BufferBuilder#vertex().color().endVertex()`, `new ResourceLocation(ns, path)`, `Tesselator#getBuilder()`). Pre-existing breaks; slated for Phase 7.
- NeoForge `AttachmentType` has no built-in "attach only to entities of type X" filter. The gate lives in `SpiritwebCapability.get(...)` via `isValidSpiritWebEntity`. A Forge-era save could never have attached a spiritweb to a Ravager/Creeper/etc., so no migration for stale save data is needed.

### Phase 5 — Networking (main source set only)
`SimpleChannel` + `NetworkEvent.Context` replaced with `PayloadRegistrar` + `CustomPacketPayload` + `StreamCodec` + `IPayloadContext`. Submodule packet handlers (allomancy/surgebinding/sandmastery) still reference the removed `SimpleChannel`/`createChannel` — they remain broken until their per-module port passes.
- `ICosmerePacket` — now extends `net.minecraft.network.protocol.common.custom.CustomPacketPayload`; sole method is `handle(IPayloadContext)`. Dropped `encode(FriendlyByteBuf)` (replaced by per-packet `STREAM_CODEC`) and the static `handle(PACKET, Supplier<Context>)` helper (unused).
- `BasePacketHandler` — rewritten. Dropped `SimpleChannel createChannel(...)`, `getChannel()`, and the old varargs `registerMessage`. New shape:
  - `register(IEventBus modBus)` — subscribes `onRegisterPayloadHandlers` (the stub `Cosmere.java:86` was calling).
  - `onRegisterPayloadHandlers(RegisterPayloadHandlersEvent)` — grabs `event.registrar(getProtocolVersion())` and delegates to `initialize(PayloadRegistrar)`.
  - Abstract `getProtocolVersion()` — subclass provides the version string (previously read from `Cosmere.instance.versionNumber.toString()` via `NetworkRegistry.ChannelBuilder`).
  - Abstract `initialize(PayloadRegistrar)` — subclass registers each packet via `registrar.playToServer/playToClient`.
  - Send helpers: `sendToServer(ICosmerePacket)` → `PacketDistributor.sendToServer`; `sendTo(ICosmerePacket, ServerPlayer)` → `PacketDistributor.sendToPlayer` (still skips `FakePlayer`); `sendToAllInWorld(ICosmerePacket, ServerLevel)` → `PacketDistributor.sendToPlayersInDimension`. Object→ICosmerePacket narrowing is source-compatible with all existing main callsites.
  - **Dropped as dead code** (zero callers anywhere in the repo): `sendPacketToAll`, `sendToAllAround`, `sendToTrackingTE`. If a submodule revives them later, the 1.21.1 replacements are `PacketDistributor.sendToAllPlayers`, `sendToPlayersNear`, and `sendToPlayersTrackingChunk` respectively.
- `NetworkPacketHandler` — thin subclass; overrides `getProtocolVersion()` (defers to `Cosmere.instance.versionNumber.toString()`) and `initialize(PayloadRegistrar)`. Registration list is unchanged from the pre-port code: 1× `playToClient` (`SyncPlayerSpiritwebMessage`), 4× `playToServer` (`DeactivateManifestationsMessage`, `ChangeManifestationModeMessage`, `ChangeSelectedManifestationMessage`, `SetSelectedManifestationMessage`). All handlers wired as `ICosmerePacket::handle` (unbound method reference; receiver is the decoded payload).
- `network/packets/*` — all 6 packet classes converted from plain classes to `record` types implementing `ICosmerePacket`:
  - Each adds `public static final CustomPacketPayload.Type<SELF> TYPE` (with a `Cosmere.rl("snake_case_name")` id) and `public static final StreamCodec<ByteBuf, SELF> STREAM_CODEC`.
  - Implements `type()` returning `TYPE`.
  - `handle(NetworkEvent.Context)` → `handle(IPayloadContext)`. Server-bound handlers guard with `if (!(context.player() instanceof ServerPlayer sender)) return;` (replaces `context.getSender()`); then `context.enqueueWork(() -> ...)` replaces the `server.submitAsync(...)` hop.
  - `SyncPlayerSpiritwebMessage(int, CompoundTag)` — `StreamCodec.composite(INT, ::entityID, TRUSTED_COMPOUND_TAG, ::entityNBT, ::new)`. Handler still reaches through `Minecraft.getInstance().level.getEntity(...)`; safe because the class is only registered `playToClient`.
  - `DeactivateManifestationsMessage()` — empty record; codec is `StreamCodec.unit(new DeactivateManifestationsMessage())`.
  - `ChangeSelectedManifestationMessage(int dir)` — single-field, uses `VAR_INT.map(...)`.
  - `SetSelectedManifestationMessage(Manifestation)` — `STRING_UTF8.map` encodes `manifestation.getRegistryName().toString()` and decodes via `ManifestationRegistry.fromID(...)`.
  - `ChangeManifestationModeMessage(Manifestation, int)` — 2-field `composite` with the same registry-name string + `VAR_INT` shape.
  - `SyncPushPullMessage(CompoundTag)` — `COMPOUND_TAG.map(...)`. Uses untrusted `COMPOUND_TAG` since this packet is client-to-server (size-limited to ~32 KiB; push/pull sets are small). Still not registered in `NetworkPacketHandler.initialize` (matches pre-port behavior — the packet is ported so main compiles, but allomancy's callsite flows through its own handler and will be re-wired in Phase 7).
- Callsites in the main source set (`SpiritwebCapability.syncToClients`, `client/PowerSaveState`, `client/gui/SpiritwebMenu`, `client/ClientForgeEvents`) were **not** touched — the signature change `(Object → ICosmerePacket)` is source-compatible because every callsite passes a concrete packet instance.

Notes:
- Submodule packet handlers (`AllomancyPacketHandler`, `SurgebindingPacketHandler`, `SandmasteryPacketHandler`) still extend `BasePacketHandler` but reference removed symbols (`SimpleChannel`, `createChannel`, `getChannel`, the old `registerMessage` shape). They do not compile and won't until each submodule's port pass. The `BasePacketHandler` contract was kept minimal so submodule ports can follow the same pattern: add a `getProtocolVersion()` returning `ThatMod.instance.versionNumber.toString()` and an `initialize(PayloadRegistrar)` body.

### Phase 0.75 — `api` source set classpath (ModDevGradle 2.x opt-in)
`./gradlew compileApiJava` was failing with 1,026 errors of the form `package net.minecraft.client does not exist` / `package net.minecraft.network.chat does not exist` on files in `src/api/`. Root cause: in ModDevGradle 2.x, `neoForge { version = ... }` attaches the Minecraft/NeoForge/Parchment classpath **only to `sourceSets.main`**. Every other source set in the mod must opt in explicitly via `neoForge.addModdingDependenciesTo(sourceSet)` — the `mods { cosmere { sourceSet sourceSets.api } }` block only drives mod packaging, not compile classpath.
- `build.gradle` — inside the `neoForge { }` block, after `accessTransformers.from(...)`, added `addModdingDependenciesTo` calls for: `api`, `datagenMain`, `gameTestMain`, and (via `for (name : secondaryModules)`) each submodule source set plus its `datagen` / `gameTest` extras. That's ~37 source sets total.
- Result: `compileApiJava` now drops from 1,026 → 169 errors. The remaining 169 are real Forge→NeoForge code migrations in `src/api/` (not classpath): `net.minecraftforge.registries.ForgeRegistries` → `BuiltInRegistries`/`NeoForgeRegistries`; `new ResourceLocation(ns, path)` → `ResourceLocation.fromNamespaceAndPath(...)` (~23 sites); `ItemStack#getOrCreateTag()` → the new `DataComponents` API (Phase 7 scope); a handful of `AttributeModifier` ctor changes (now takes `Holder<Attribute>` + `ResourceLocation`); `LivingEntity#getExperienceReward` signature changed. These are authentic code-level work for the next phases, not a build-system issue.

### Phase 0.8 — `api/` code migration (surfaced by Phase 0.75)
`./gradlew compileApiJava` now succeeds. All 169 residual errors cleared. Files touched in `src/api/java/leaf/cosmere/api/`:
- `CosmereAPI.java` — `IForgeRegistry<T>` + `RegistryManager.ACTIVE.getRegistry(key)` → `Registry<T>` + `BuiltInRegistries.REGISTRY.get(key.location())` (cast because the root registry is `Registry<Registry<?>>`). `net.minecraftforge.common.util.Lazy` → `net.neoforged.neoforge.common.util.Lazy`. `ResourceKey<? extends Registry<T>>` → `ResourceKey<Registry<T>>` (the `WrappedDeferredRegister` ctor already accepts the narrower type). `new ResourceLocation(...)` → `ResourceLocation.fromNamespaceAndPath(...)`.
- `Version.java` — `net.minecraftforge.fml.ModContainer` → `net.neoforged.fml.ModContainer`.
- `Constants.java` — 7× `new ResourceLocation(ns, path)` → `ResourceLocation.fromNamespaceAndPath(ns, path)` (done via `replace_all`).
- `CosmereTags.java` — `ForgeRegistries.BIOMES.getRegistryKey()` / `ForgeRegistries.ENTITY_TYPES.getRegistryKey()` → `Registries.BIOME` / `Registries.ENTITY_TYPE`. `"forge"` common-tag namespace → `"c"` (NeoForge 1.21.1 convention). All `new ResourceLocation(...)` → `.fromNamespaceAndPath(...)`.
- `Metals.java` — dropped `implements ArmorMaterial` (it's a record in 1.21, no longer an interface); kept the armor-shaped getters as plain methods for Phase 7 to consume when it builds real `ArmorMaterial` records. Added `@Override TagKey<Block> getIncorrectBlocksForDrops()` dispatching by level. `getLevel()` kept as a plain accessor (Tier no longer declares it). Fixed the copper hemalurgy branch: `killedEntity.getExperienceReward()` → `getExperienceReward(ServerLevel, Entity)` guarded by a `ServerLevel` check; `ForgeRegistries.ATTRIBUTES.getValue(rl)` + `killedEntity.getAttribute(Attribute)` → `BuiltInRegistries.ATTRIBUTE.getHolder(...)` + `.getAttribute(Holder<Attribute>)`. `cat.getVariant()` → `cat.getVariant().value()` (returns `Holder<CatVariant>` in 1.21). `getNugget()` uses `ResourceLocation.fromNamespaceAndPath` + `BuiltInRegistries.ITEM.get(...)`. `getEquipSound()` return type → `Holder<SoundEvent>` to match `SoundEvents.ARMOR_EQUIP_IRON`'s new shape.
- `Manifestation.java` — `IForgeRegistry<Manifestation>` → `Registry<Manifestation>`. `ForgeRegistries.ATTRIBUTES.getValue(rl)` → `BuiltInRegistries.ATTRIBUTE.get(rl)`. `cap.getLiving().getAttribute(Attribute)` → `getAttribute(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attr))`.
- `Roshar.java` (`Gemstone` enum) — added `@Override TagKey<Block> getIncorrectBlocksForDrops()` → `BlockTags.INCORRECT_FOR_WOODEN_TOOL` (all gemstones level 0). `@Override int getLevel()` → plain `int getLevel()`.
- `providers/` (IAttributeProvider, IBlockProvider, IEntityTypeProvider, IItemProvider, IMobEffectProvider) — `ForgeRegistries.X` → `BuiltInRegistries.X` (singular: ATTRIBUTE, BLOCK, ENTITY_TYPE, ITEM, MOB_EFFECT).
- `helpers/RegistryHelper.java` — `ForgeRegistries.X.getKey(y)` → `BuiltInRegistries.X.getKey(y)` (linter also swapped plurals to the vanilla singulars: ITEM, BLOCK, ENTITY_TYPE, FLUID, MENU, PARTICLE_TYPE, BLOCK_ENTITY_TYPE, RECIPE_SERIALIZER). Private helper signature `IForgeRegistry<T>` → `Registry<T>`.
- `helpers/EntityHelper.java` — `attributes.hasAttribute(Attribute)` / `getValue(Attribute)` → the `Holder<Attribute>` overloads, wrapping via `BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attr)`.
- `helpers/EffectsHelper.java` — `new MobEffectInstance(MobEffect, ...)` → `new MobEffectInstance(Holder<MobEffect>, ...)` via `BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect)`.
- `cosmereEffect/AttributeModifierInfo.java` — `ForgeRegistries.ATTRIBUTES.{getKey,containsKey,getValue}` → `BuiltInRegistries.ATTRIBUTE.{getKey,get}`-with-null-guard. `new ResourceLocation(s)` → `ResourceLocation.parse(s)`. `Operation.toValue()/fromValue(int)` kept as-is in the save path (linter confirmed they still exist as `operation.id()` / `Operation.BY_ID.apply(int)` in NeoForge-patched vanilla).
- `cosmereEffect/CosmereEffectInstance.java` — **non-trivial** refactor. 1.21.1 moved `AttributeModifier` from a 4-arg ctor `(UUID id, String name, double amount, Operation op)` to a 3-arg ctor `(ResourceLocation id, double amount, Operation op)`, and `AttributeInstance.removeModifier(...)` now takes the same `ResourceLocation` id (not UUID). The effect instance's existing `UUID uuid` now serves as the *path* of a `ResourceLocation.fromNamespaceAndPath(CosmereAPI.COSMERE_MODID, uuid.toString())` (UUID.toString is lowercase hex+hyphens which satisfies the RL path regex). Added a private `modifierId()` helper. `AttributeMap.getInstance(Attribute)` → `getInstance(Holder<Attribute>)` via a private `holder(Attribute)` helper. The old human-readable format-string modifier name is dropped.
- `IHasSize.java` — `ItemStack#getOrCreateTag()` is gone. Reads now go through `stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag()`; writes through `CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> ...)`. Keeps the same `"nuggetSize"` key for save-data continuity, though Phase 7 should migrate to a purpose-built component type.
- `helpers/DrawHelper.java` — **Phase 0.8 stub**. The full render implementation (vertex builder + `RenderType.create` + `LineStateShard`) all changed shape in 1.21 (vertex builder drops `endVertex()`, uses `addVertex/setColor/setNormal(Pose, ...)/setUv/setLight/setOverlay`, and `RenderType.create` signature took on new parameters). The only caller is `AllomancySpiritwebSubmodule` (out of scope until allomancy module port). Public methods (`drawLinesFromPoint`, `drawSquareAtPoint`, `drawBlocksAtPoint`, enum `CosmereAPIRenderTypes`) kept as callable no-ops. **Phase 7 must restore the full bodies** — the original implementation is preserved in git history.
- `helpers/StackNBTHelper.java` — every `ItemStack#getOrCreateTag()` call rerouted through `CustomData` (`stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag()` for reads; `CustomData.update(DataComponents.CUSTOM_DATA, stack, t -> ...)` for writes). Public API signatures preserved so callers in hemalurgy/feruchemy/sandmastery/main keep compiling (semantically they now share one `CUSTOM_DATA` blob instead of living directly on the stack, which will matter for Phase 7 when each feature gets its own DataComponentType). `serializeStack(ItemStack)` stubbed to `null` — the 1.21 replacement (`ItemStack#save(HolderLookup.Provider)`) needs a registry-lookup provider the api source set can't construct in isolation; the only caller (feruchemy recipe-result provider) will be ported with Phase 7.

**Build status after Phase 0.8**:
- `./gradlew compileApiJava` — **green**.
- `./gradlew compileJava` (main source set) — 353 errors, all downstream Phase 6/7 work (event handlers, items, blocks, commands, datagen). Phase 5 networking correctness still not directly verifiable until those clear, but `api` is no longer blocking.

### Phase 6 — Event handlers
All 5 files in `src/main/java/leaf/cosmere/common/eventHandlers/` ported. Main source set errors: **353 → 280** (73 eventHandlers-specific errors cleared).
- Uniform Forge→NeoForge swaps across every file: `@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)` → `@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)` (and `.MOD` where the handler was already on the mod bus). Import moves: `net.minecraftforge.event.*` → `net.neoforged.neoforge.event.*`; `net.minecraftforge.eventbus.api.*` → `net.neoforged.bus.api.*`; `net.minecraftforge.fml.common.Mod` / `Mod.EventBusSubscriber` → `net.neoforged.fml.common.EventBusSubscriber`; `net.minecraftforge.fml.ModList` → `net.neoforged.fml.ModList`; `net.minecraftforge.registries.ForgeRegistries` → `BuiltInRegistries`.
- `CommonEvents.java` — `net.minecraftforge.common.BasicItemListing` → `net.neoforged.neoforge.common.BasicItemListing`. `Item#getRarity(ItemStack)` removed in 1.21 (rarity moved to `DataComponents.RARITY`); swapped all 5 sites to `ItemStack#getRarity()`. Rarity loops now construct the `ItemStack` *before* the rarity check rather than calling `item.getRarity(ItemStack.EMPTY)` which would miss component-based rarities.
- `PlayerEventHandler.java` — `event.getEntity().getAttribute(Attribute)` → `getAttribute(Holder<Attribute>)`, wrapping via `BuiltInRegistries.ATTRIBUTE.wrapAsHolder(...)`. `PlayerEvent.Clone` / `PlayerEvent.StartTracking` / `ItemTossEvent` / `PlayerXpEvent.XpChange` all still exist under the NeoForge package paths.
- `EntityEventHandler.java` — biggest change. 1.21.1 deleted several events:
  - `LivingHurtEvent` → replaced by `LivingIncomingDamageEvent` (NeoForge 1.21.1). Renamed the handler method + parameter; dropped the `event.isCanceled()` guard (the new event is early enough in the damage pipeline that cancellation checks happen elsewhere).
  - `LivingEvent.LivingTickEvent` → `EntityTickEvent.Post` (fires on all entities, not just living — added a `LivingEntity` instanceof guard before calling `SpiritwebCapability.get`).
  - **`LootingLevelEvent` deleted outright** in NeoForge 1.21.1 (looting is now driven by enchantment-effect components + `LootItemFunction`). The `onLootingLevelEvent` handler that boosted drop rolls by the `COSMERE_FORTUNE` attribute is **gone**. The feature will be re-implemented as a GlobalLootModifier in Phase 9 — a comment in the file documents this decision so the intent isn't lost.
  - Warden spawn bronze-attribute lookup: `BuiltInRegistries.ATTRIBUTE.getValue(new ResourceLocation("allomancy:bronze"))` → `getHolder(ResourceLocation.parse("allomancy:bronze"))` returning `Holder<Attribute>`; `livingEntity.getAttribute(Attribute)` now takes the Holder directly.
- `ModBusEventHandler.java` — `EntityAttributeModificationEvent.add(EntityType, Attribute)` → `add(EntityType<? extends LivingEntity>, Holder<Attribute>)`. Added a private `holder(Attribute) -> Holder<Attribute>` helper. The `ENTITIES_THAT_CAN_HAVE_POWERS` raw-typed array now declares as `EntityType<? extends LivingEntity>[]` (cast suppressed, since every hard-coded constant already targets a LivingEntity subtype).
- `ColorHandler.java` — `ForgeRegistries.ITEMS` → `BuiltInRegistries.ITEM`. `Minecraft.getBlockColors()` / `getItemColors()` still exist unchanged.

### Phase 7 — Items / blocks / entities / capabilities / client
Main source errors: **353 → 59** (294 errors cleared). Remaining 59 are all Phase 9 (loot, 33) and Phase 10 (recipes, 21) scope plus 5 associated registry-codec signatures. Core survey below — the volume of touched files is large, so groupings are by theme:

**Registration generic bounds (root of the dep DAG)**: The Phase 1 port left `DeferredHolder<?, X>` wildcards in `WrappedRegistryObject`, `DoubleWrappedRegistryObject`, and all 23 `src/main/java/leaf/cosmere/common/registration/impl/*RegistryObject.java` constructors. NeoForge's `DeferredHolder<R, T extends R>` rejects that (T can't be bounded by a wildcard). Every occurrence rewritten as `DeferredHolder<? super X, X>` (via a perl one-liner across impl/ plus manual edits in the base classes). This was the cascade that brought the error count from 126 → 78 in one pass.

**Client event handlers**: `ClientForgeEvents`, `ClientModEvents`, `Keybindings`, `KeyConflictContext` ported. `MouseScrollingEvent#getScrollDelta()` → `getScrollDeltaY()`. `RegisterGuiOverlaysEvent` + `VanillaGuiOverlay.DEBUG_TEXT.id()` → `RegisterGuiLayersEvent` + `VanillaGuiLayers.DEBUG_OVERLAY` (ResourceLocation id now, not string). Layer lambda signature became `(GuiGraphics, DeltaTracker) -> void`. `IKeyConflictContext` moved to `net.neoforged.neoforge.client.settings`.

**SpiritwebMenu** (radial power picker): vertex-builder rendering stubbed for Phase 7 — 1.21.1 removed `Tesselator#getBuilder()` and `VertexConsumer#endVertex()`, and the button/quadrant background fills no longer work via the old `.vertex(...).color(...).endVertex()` pipeline. Kept the hit-test math as `updateRadialButtonHitboxes` / `updateSidedButtonHitboxes`; icons and strings still draw via `GuiGraphics`. A future render-polish pass should restore filled backgrounds via `GuiGraphics#fill` or a 1.21 BufferBuilder. All `new ResourceLocation(...)` swapped to `fromNamespaceAndPath(...)`.

**SpiritwebCapability**: `AttributeMap#hasAttribute(Attribute)` / `getInstance(Attribute)` / `LivingEntity#getAttribute(Attribute)` / `gameEvent(GameEvent)` all now take `Holder<…>`. Added private `attrHolder(Attribute) -> Holder<Attribute>` helper; 6 call sites routed through it. `spiritWebEntity.gameEvent(GameEventRegistry.KINETIC_INVESTITURE.get())` wraps via `BuiltInRegistries.GAME_EVENT.wrapAsHolder`. `renderSelectedHUD`'s FileNotFoundException fallback (old Tesselator pipeline) replaced with `gg.fill(...)`. Two `new ResourceLocation(...)` → `.fromNamespaceAndPath(...)`.

**Items (`items/*.java`)**:
- `Item#appendHoverText(ItemStack, Level, List<Component>, TooltipFlag)` → `appendHoverText(ItemStack, Item.TooltipContext, List<Component>, TooltipFlag)` across `GuideItem`, `ChargeableItemBase`, `GodMetalAlloyNuggetItem`, `GodMetalNuggetItem`. `@OnlyIn(Dist.CLIENT)` dropped (the new sig has no client-only binding).
- `Item#getRarity(ItemStack)` removed in favor of `DataComponents.RARITY`. `GodMetalAlloyNuggetItem` and `GodMetalNuggetItem`'s dynamic per-stack rarity can't be expressed cleanly yet — dropped `@Override` and left a TODO for Phase 7.5 to flip the `RARITY` component on size change.
- `Item#getUseDuration(ItemStack)` → `getUseDuration(ItemStack, LivingEntity)` on `MetalNuggetItem`.
- `ItemStack#hasTag()` / `#getTag()` / `#getOrCreateTag()` are gone in 1.21.1 — `GuideItem.getTitle` stubbed (the akashic-tome name override needs `CUSTOM_DATA` read + `Component.Serializer.fromJson(String, Provider)`; both need a registry-lookup context we don't have here; TODO for later). `GodMetalNuggetItem#onCraftedBy` refactored — the size is now lazy-initialized via `IHasSize#readMetalAlloySizeNbtData` which routes through `DataComponents.CUSTOM_DATA`.
- `ChargeableMetalCurioItem#onUnequip` — `removeModifier(UUID)` → `removeModifier(ResourceLocation)` (legacy UUID mapped to a stable `ResourceLocation.fromNamespaceAndPath("cosmere", "feru_nicrosil_" + uuid)`).

**Capability plumbing (`items/CapWrapper.java`)**: `net.minecraftforge.items.{IItemHandler,IItemHandlerModifiable}` → `net.neoforged.neoforge.items.*`.

**Curios 9.x integration**:
- `CuriosCompat`: `net.minecraftforge.fml.*` → `net.neoforged.fml.*`.
- `ItemChargeHelper`: `CuriosApi.getCuriosInventory(player)` returns `Optional<ICuriosItemHandler>` directly now (was `LazyOptional`). `net.minecraftforge.items.wrapper.PlayerInvWrapper` → `net.neoforged.neoforge.items.wrapper.PlayerInvWrapper`. Dropped unused `LazyOptional` + `EmptyHandler` imports.

**Charge system (`IChargeable`)**: `player.getEffect(MobEffect)` requires `Holder<MobEffect>`; wrap via `BuiltInRegistries.MOB_EFFECT.getHolder(...)`. `new ResourceLocation(...)` → `.fromNamespaceAndPath(...)`.

**Utility (`common/util/`)**:
- `CosmereAttributeUtils`: `BuiltInRegistries.ATTRIBUTE.getValue(new ResourceLocation(...))` → `.get(ResourceLocation.fromNamespaceAndPath(...))`; `LivingEntity#getAttribute(Attribute)` → `getAttribute(Holder<Attribute>)`.
- `TaskQueueManager`: `TickEvent.ServerTickEvent` → `ServerTickEvent.Post` (1.21.1 rename), event package moves, `@Mod.EventBusSubscriber(Bus.FORGE)` → `@EventBusSubscriber(Bus.GAME)`.

**Fog (`fog/FogManager`)**: `MobEffectInstance#getFactorData()` was removed in 1.21.1 (the factor is now surfaced through biome/render state). The DARKNESS-effect fog scaling branch was converted to an inert TODO; BLINDNESS scaling still works because `Holder<MobEffect>` overloads of `hasEffect`/`getEffect` didn't require a source change here.

**Block/FallingBlock codec**: 1.21.1 made `Block#codec()` / `FallingBlock#codec()` abstract (required for datapack-loadable blocks). Added `MapCodec<BaseFallingBlock> CODEC = simpleCodec(BaseFallingBlock::new)` + `codec()` override on `BaseFallingBlock`. `DropExperienceBlock` constructor arg order swapped `(Properties, IntProvider)` → `(IntProvider, Properties)` on `MetalOreBlock`.

**BlockBehaviour.Properties.copy(...) → ofFullCopy(...)**: `PropTypes.Blocks.{EXAMPLE,ORE,METAL,SAND}` all updated.

**Mixin (`LightTextureMixin`)**: `new ResourceLocation(...)` → `.fromNamespaceAndPath(...)`; `CosmereAPI.manifestationRegistry().getValue(rl)` → `.get(rl)`; `clientPlayer.getAttribute(Attribute)` → `Holder<Attribute>` wrap; `mc.getPartialTick()` (gone in 1.21) → `mc.getTimer().getGameTimeDeltaPartialTick(true)`.

**Commands (partial Phase 8 touch)**: `CosmereAPI.manifestationRegistry().getValue(rl)` → `.get(rl)` on `AllomancyArgumentType`, `FeruchemyArgumentType`, `ManifestationsArgumentType`. `ChooseMetalbornPowersCommand`: `net.minecraftforge.fml.ModList` → `net.neoforged.fml.ModList`.

**Registry lookups**: `CosmereAPI.manifestationRegistry().getValue(rl)` → `.get(rl)` in `ManifestationRegistry` itself. (Registry.getValue was a Forge extension; vanilla uses `Registry#get`.)

### Phase 8 — Commands / argument types
All 11 files in `src/main/java/leaf/cosmere/common/commands/**` and the supporting `ArgumentTypeRegistry` are green. Phase 7 pulled the heavy lifting forward (registry `getValue(rl)` → `get(rl)` + `ModList` package move) so the residual work here was minor:
- `CosmereCommand.java` — dropped unused imports (`ArgumentTypeInfos`, `SingletonArgumentInfo`, and the three `*ArgumentType` imports). Those referenced the pre-1.21 inline-registration pattern; actual `ArgumentTypeInfo` registration lives in `ArgumentTypeRegistry` via `DeferredRegister<ArgumentTypeInfo<?,?>>` + `ArgumentTypeInfos.registerByClass(...)`.
- `ArgumentTypeRegistry` — verified: `DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, Cosmere.MODID)` with `DeferredHolder<ArgumentTypeInfo<?,?>, ArgumentTypeInfo<?,?>>` entries for `manifestation_argument_type`, `allomancy_argument_type`, `feruchemy_argument_type`. Registered on the mod bus in `Cosmere.java:69` (`ARGUMENT_TYPE_INFOS.register(modBus)`).
- `CommonEvents.java` — verified: `@SubscribeEvent registerCommands(RegisterCommandsEvent)` is on the GAME bus, calls `CosmereCommand.register(event.getDispatcher())`. `RegisterCommandsEvent` import already repathed to `net.neoforged.neoforge.event.*` in Phase 6.
- `ManifestationsArgumentType` / `AllomancyArgumentType` / `FeruchemyArgumentType` — all three use `ResourceLocation.read(StringReader)` (unchanged in 1.21.1), `CosmereAPI.manifestationRegistry().get(location)` (ported Phase 7), `SharedSuggestionProvider.suggest(...)`, and iterate `CosmereAPI.manifestationRegistry()` directly (Registry is Iterable in vanilla). `manifestation.getRegistryName().toString()` still works — `getRegistryName()` is the mod's own wrapper method on `Manifestation`, not the removed Forge `IForgeRegistryEntry#getRegistryName`.
- `ModCommand`, `EyeCommand`, `SummonCommand`, `ManifestationCommand`, `CosmereEffectCommand`, `ChooseMetalbornPowersCommand`, `TestCommand` — all ported in earlier phases; no further changes needed. Permission API (`context.hasPermission(2)`) is still valid in 1.21.1 Brigadier.
- `TestCommand` remains unregistered in `CosmereCommand.register` (matches pre-port; dev-only helper).

**Build status after Phase 8**:
- `./gradlew compileJava` — **58 errors**, all in `loot/`, `recipes/`, `registry/CosmereRecipesRegistry`, `registry/LootFunctionRegistry`, `registry/LootModifiersRegistry`, `registry/HeightProviderTypesRegistry`, `registry/IntProviderTypesRegistry`. Zero errors in `commands/`. Remaining failures are entirely Phase 9 (loot / world-features) and Phase 10 (recipes / datagen) scope.

### Phase 9 — Loot / world / features / biomes
Main source errors: **58 → 21** (37 errors cleared). All 21 residual errors are in `recipes/` + `CosmereRecipesRegistry` — pure Phase 10 (recipes) scope. Zero errors in `loot/`, `world/`, or any Phase 9 registry.

- `common/world/height/ConfigurableHeightProvider.java` + `common/world/ConfigurableConstantInt.java` — `Codec<T> CODEC = RecordCodecBuilder.create(...)` → `MapCodec<T> CODEC = RecordCodecBuilder.mapCodec(...)`. In 1.21.1, `HeightProviderType` / `IntProviderType` are SAM interfaces returning `MapCodec`, and the `*DeferredRegister` wrappers already expected `MapCodec` (pulled forward in Phase 1). `ResizableOreFeatureConfig.CODEC` left as `Codec` (correct — `Feature<T>` constructor still takes `Codec<T>`).
- `common/loot/FortuneBonusModifier.java` — mostly rewritten:
  - `net.minecraftforge.common.loot.{IGlobalLootModifier,LootModifier}` → `net.neoforged.neoforge.common.loot.*`.
  - `CODEC` now `Supplier<MapCodec<FortuneBonusModifier>>` using `RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, ::new))` (matches NeoForge's canonical `SmeltingEnchantmentModifier` pattern in `GlobalLootModifiersTest`).
  - `codec()` return type → `MapCodec<? extends IGlobalLootModifier>` (IGlobalLootModifier#codec() signature change).
  - Enchantment path rewritten for the 1.21.1 Holder-based API: `Enchantments.BLOCK_FORTUNE` (gone) → `Enchantments.FORTUNE` as `ResourceKey<Enchantment>`. Resolve via `server.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE)`. The old `EnchantmentHelper.getEnchantments(stack)` / `setEnchantments(map, stack)` pattern is gone — replaced with `EnchantmentHelper.getItemEnchantmentLevel(Holder, stack)` to read + `fakeTool.enchant(Holder, newLevel)` to write (the new `enchant` method does `max(old, new)` via `EnchantmentHelper.updateEnchantments`, which is fine because we compute `existingLevel + bonus` explicitly).
  - Tool marker tag (`HasCosmereFortuneBonus`) moved off `ItemStack#getOrCreateTag()` (gone) onto `DataComponents.CUSTOM_DATA`. Reads: `tool.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean(...)`. Writes: `CustomData.update(DataComponents.CUSTOM_DATA, fakeTool, t -> t.putBoolean(...))`.
  - Loot table lookup: `context.getLevel().getServer().getLootData().getLootTable(rl)` → `server.reloadableRegistries().getLootTable(blockState.getBlock().getLootTable())`. `Block#getLootTable()` now returns `ResourceKey<LootTable>` directly (not ResourceLocation), which is what `reloadableRegistries().getLootTable` expects.
- `common/loot/LootHandler.java` — event bus annotations ported (`@Mod.EventBusSubscriber(bus = Bus.FORGE)` → `@EventBusSubscriber(bus = Bus.GAME)`); `net.minecraftforge.{event,eventbus.api,fml.common}` → `net.neoforged.{neoforge.event,bus.api,fml.common}`. `LootTableReference` was renamed to `NestedLootTable` in 1.21.1 — import moved + static call updated. `NestedLootTable.lootTableReference(...)` now takes `ResourceKey<LootTable>` (not `ResourceLocation`), so wrap via `ResourceKey.create(Registries.LOOT_TABLE, Cosmere.rl("inject/" + name))`. `LootTableLoadEvent` itself still exists in NeoForge 1.21.1 (deprecated-but-functional; the Vampirism/AnvilCraft/etc pattern of `evt.getTable().addPool(...)` is unchanged).
- `common/loot/RandomiseMetalTypeLootFunction.java` — rewritten for the 1.21.1 MapCodec loot-function pattern:
  - Added `public static final MapCodec<RandomiseMetalTypeLootFunction> CODEC = RecordCodecBuilder.mapCodec(inst -> commonFields(inst).apply(inst, ::new))`. `commonFields(instance)` is the static helper on `LootItemConditionalFunction` that replaces the old Gson-based `LootItemConditionalFunction.Serializer` inner class.
  - Constructor parameter `LootItemCondition[]` → `List<LootItemCondition>` (super-class signature change).
  - Dropped the inner `Serializer` class entirely — registration now uses `MapCodec` directly.
  - `LootItemFunctionType` is generic in 1.21.1 — `getType()` return type → `LootItemFunctionType<RandomiseMetalTypeLootFunction>`.
  - `ForgeRegistries.ITEMS.getValues()` → `BuiltInRegistries.ITEM` (the registry is Iterable<Item>).
  - `stack.getOrCreateTag().copy()` / `stack.setTag(nbt)` → `CustomData`: read the `CUSTOM_DATA` component from the old stack, construct the new item stack, then `stack.set(DataComponents.CUSTOM_DATA, customData)` if non-empty. This preserves the "copy NBT across metal-type randomisation" behaviour while respecting the 1.21 data-component model.
- `common/registration/impl/LootFunctionDeferredRegister.java` — rewritten to match Mekanism's pattern:
  - Old: `register(String name, Supplier<LOOT_ITEM_FUNCTION_TYPE> sup)` taking a `Supplier<LootItemFunctionType>` (from the old Gson-Serializer ctor).
  - New: `register(String name, Supplier<MapCodec<T>> codec)` which internally does `new LootItemFunctionType<>(codec.get())` — `LootItemFunctionType<T>` is now generic with a `(MapCodec<T>)` constructor in 1.21.1.
- `common/registration/impl/LootItemFunctionTypeRegistryObject.java` — generic bound tightened from `<T extends LootItemFunctionType<?>>` (the old pre-generic shape) to `<T extends LootItemFunction>` — the wrapper now parameterises over the **function type T**, and internally stores a `WrappedRegistryObject<LootItemFunctionType<T>>`. Mirrors the vanilla generic shape.
- `common/registry/LootFunctionRegistry.java` — call site updated: `LOOT_FUNCTIONS.registerType("randomise_metaltype", Serializer::new)` → `LOOT_FUNCTIONS.register("randomise_metaltype", () -> RandomiseMetalTypeLootFunction.CODEC)`. Field type → `LootItemFunctionTypeRegistryObject<RandomiseMetalTypeLootFunction>`.
- `common/registry/LootModifiersRegistry.java` — `LOOT_MODIFIERS` / `FORTUNE_BONUS` generics tightened from the over-broad `MapCodec<? extends IGlobalLootModifier>` to `MapCodec<FortuneBonusModifier>` — matches NeoForge's own `DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<SmeltingEnchantmentModifier>>` pattern in `GlobalLootModifiersTest.java`. The `IGlobalLootModifier` import is no longer needed at this registry (it's now a transitive type only), dropped.

**Build status after Phase 9**:
- `./gradlew compileJava` — **21 errors**, all in `recipes/` + `CosmereRecipesRegistry`. Phase 9 is entirely clear.

Notes:
- The `fortuneBonus` loot-modifier JSON file under `src/datagen/main/resources/.../data/.../loot_modifiers/` (if present as a datapack entry) will need to be re-verified during Phase 10 to ensure its serialised shape matches `LootModifier.codecStart` (conditions field only). No format change — the old Codec and new MapCodec both expect the same `{"type":"cosmere:fortune_bonus","conditions":[]}` JSON.
- The `LootTableLoadEvent` path in `LootHandler` is still *deprecated-but-functional* in NeoForge 1.21.1. Future work (not Phase 9) could migrate the chest-injection to `AddTableLootModifier` GLM entries via datagen's `GlobalLootModifierProvider`.

---

### Phase 10 — Recipes + datagen (main module only)
Main source errors: **21 → 0**. Main datagen errors: **191 → 0** (2 deprecation warnings in `CosmereTagBuilder` remain; harmless — `ITagBuilderExtension.removeElement/removeTag(ResourceLocation, String)` is deprecated-for-removal in favour of data-component replacements, but still functional). Submodule datagen source sets (`src/datagen/allomancy/…` etc.) are **deliberately out of scope** — they still reference the old `BaseRecipeProvider(PackOutput, ExistingFileHelper, String)` ctor + `Consumer<FinishedRecipe>` shape and will be ported with each submodule's port pass. The same applies to submodule `*Recipes.java` registries.

**Recipe classes (`src/main/java/leaf/cosmere/common/recipes/*`)** — all 3 `CustomRecipe` subclasses ported to the 1.21.1 shape:
- Constructor: dropped `ResourceLocation` arg; now `super(CraftingBookCategory)`. 1.21 moved recipe ids out of the recipe object — they live on the enclosing `RecipeHolder<T>` instead.
- `matches(CraftingContainer, Level)` → `matches(CraftingInput, Level)`; `assemble(CraftingContainer, RegistryAccess)` → `assemble(CraftingInput, HolderLookup.Provider)`; `getRemainingItems(CraftingContainer)` → `getRemainingItems(CraftingInput)`.
- Dropped `getId()` override (method removed from `Recipe` interface).
- `inv.getContainerSize()` → `inv.size()`; `inv.getItem(i)` unchanged.
- `CosmereRecipesRegistry.java` needed no edits — the `SimpleCraftingRecipeSerializer<>(GodMetalAlloyNuggetRecipe::new)` calls now resolve to the new `(CraftingBookCategory) -> CustomRecipe` constructor.

**`src/datagen/main/**` (25 files)** — systematic NeoForge 1.21.1 port. Highlights:
- **Package moves** (pure swaps): `net.minecraftforge.common.data.{ExistingFileHelper, LanguageProvider, DatapackBuiltinEntriesProvider}` → `net.neoforged.neoforge.common.data.*`; `net.minecraftforge.client.model.generators.*` → `net.neoforged.neoforge.client.model.generators.*`; `net.minecraftforge.common.Tags` → `net.neoforged.neoforge.common.Tags`; `net.minecraftforge.common.crafting.{DifferenceIngredient}` → `net.neoforged.neoforge.common.crafting.*`; `net.minecraftforge.common.world.{BiomeModifier, StructureModifier, ForgeBiomeModifiers}` → `net.neoforged.neoforge.common.world.{BiomeModifier, StructureModifier, BiomeModifiers}` (class rename + package move); `net.minecraftforge.registries.ForgeRegistries.Keys` → `net.neoforged.neoforge.registries.NeoForgeRegistries.Keys`; `net.minecraftforge.data.event.GatherDataEvent` → `net.neoforged.neoforge.data.event.GatherDataEvent`; `@Mod.EventBusSubscriber(bus = Bus.MOD)` → `@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)`.
- **Typo fix (Mojang, not ours)**: `net.minecraft.data.worldgen.BootstapContext` → `BootstrapContext` (both signatures in `BaseDatapackRegistryProvider`).
- **`BaseRecipeProvider.java`** — rewritten for the new recipe-provider contract:
    - Ctor: `(PackOutput, ExistingFileHelper, String)` → `(PackOutput, CompletableFuture<HolderLookup.Provider>, String)` (matches `RecipeProvider`'s new 2-arg super).
    - `buildRecipes(Consumer<FinishedRecipe>)` → `buildRecipes(RecipeOutput)`. `addRecipes(Consumer<FinishedRecipe>)` → `addRecipes(RecipeOutput)`.
    - Dropped the Consumer-wrapping `trackingConsumer` that called `existingFileHelper.trackGenerated(recipe.getId(), ...)`: `RecipeOutput.accept(...)` no longer exposes the id to a simple consumer, and recipe-id tracking for existing-file validation is a datagen-quality hint, not functionality. Submodule ports can reinstate if needed via a wrapping `RecipeOutput`.
    - Dropped the 3 `createIngredient(...)` helpers and `Ingredient.fromValues(...)` calls — `Ingredient.fromValues` and the `Ingredient.ItemValue` / `Ingredient.TagValue` inner classes are gone in 1.21.1 (Ingredient now wraps `HolderSet<Item>` directly). The helpers had zero callers in the repo. Kept `difference(TagKey, ItemLike)` via the still-present `DifferenceIngredient.of(...)`.
    - `ItemStack#getOrCreateTag()` in `godMetalCompressRecipe` → `DataComponentIngredient.of(false, DataComponents.CUSTOM_DATA, CustomData.of(tag), input.asItem())` (NeoForge's 1.21.1 replacement for `PartialNBTIngredient`). The strict-mode flag is `false` (partial match) to match the old `PartialNBTIngredient` semantics. `"nuggetSize"` key preserved per Phase 0.8 contract.
    - `new ResourceLocation(modid, path)` throughout → `ResourceLocation.fromNamespaceAndPath(modid, path)`.
- **`RecipeGen.java`** — updated to match:
    - Ctor shape change (mirrors `BaseRecipeProvider`).
    - `SpecialRecipeBuilder.special(CosmereRecipesRegistry.X.get())` (passed a `RecipeSerializer`) → `SpecialRecipeBuilder.special(GodMetalAlloyNuggetRecipe::new)` etc. — the new 1.21.1 signature is `special(Function<CraftingBookCategory, Recipe<?>>)`, which matches our `(CraftingBookCategory) -> CustomRecipe` ctors directly.
    - `Tags.Items.INGOTS_COPPER` / `Tags.Items.STORAGE_BLOCKS_COPPER` remain valid (NeoForge 1.21.1 kept the constants despite the common-tag namespace moving from `forge` to `c`).
    - `IConditionBuilder` import moved: `net.minecraftforge.common.crafting.conditions.IConditionBuilder` → `net.neoforged.neoforge.common.conditions.IConditionBuilder`.
- **`BaseTagProvider.java`** — dropped the `<TYPE> getBuilder(IForgeRegistry<TYPE>, TagKey<TYPE>)` overload (the `IForgeRegistry` type is gone) and replaced it with a private `getIntrinsicBuilder(Registry<TYPE>, TagKey<TYPE>)` helper using `BuiltInRegistries.*` singulars (`ITEM`, `BLOCK`, `ENTITY_TYPE`, `FLUID`, `BLOCK_ENTITY_TYPE`, `MOB_EFFECT`, `GAME_EVENT`). This also resolved the ambiguity on `getBuilder(Registries.DAMAGE_TYPE, tag)` / `Registries.BIOME`. `GameEvent.builtInRegistryHolder().key()` was the 1.20.1 lookup — removed; now `BuiltInRegistries.GAME_EVENT.getResourceKey(gameEvent).orElseThrow()` (via the shared intrinsic helper).
- **`ForgeRegistryTagBuilder.java` — deleted.** Only `BaseTagProvider`'s now-removed `IForgeRegistry` overload instantiated it. No external callers.
- **Loot tables (`src/datagen/main/java/leaf/cosmere/loottables/*`)** — all 6 files ported to the 1.21.1 sub-provider API:
    - `LootTableProvider` ctor gained a `CompletableFuture<HolderLookup.Provider>` param: `BaseLootProvider` + `LootTableGen` updated; `Set<ResourceLocation>` → `Set<ResourceKey<LootTable>>`.
    - `SubProviderEntry`'s factory param is now `Function<HolderLookup.Provider, ? extends LootTableSubProvider>` (was `Supplier`). `BlockLootTableGen` / `EntityLootTableGen` now have explicit `(HolderLookup.Provider)` ctors that forward to super.
    - `BlockLootSubProvider` ctor: `(Set<Item>, FeatureFlagSet)` → `(Set<Item>, FeatureFlagSet, HolderLookup.Provider)`. `EntityLootSubProvider`: `(FeatureFlagSet)` → `(FeatureFlagSet, HolderLookup.Provider)`.
    - `BaseBlockLootTables` simplified heavily: deleted the static `HAS_SILK_TOUCH` field + the `createSlabItemTable` / `createSingleItemTable` / `createSingleItemTableWithSilkTouch` / `createSilkTouchDispatchTable` / `createSelfDropDispatchTable` / `dropOther` overrides. All of these existed solely to add `LootPool.Builder.name("main")` — pools are no longer named in 1.21.1 (they're indexed). Vanilla's inherited versions now do the right thing.
    - `createOreDrop` / `droppingWithFortuneOrRandomly` rewrote for the 1.21.1 Holder-based enchantment API: `ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)` → `addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE))`, where `enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT)`. `Enchantments.FORTUNE` is a `ResourceKey<Enchantment>` in 1.21.1.
- **`CosmereDatapackRegistryProvider.java`** — `ForgeBiomeModifiers.AddFeaturesBiomeModifier` → `BiomeModifiers.AddFeaturesBiomeModifier` (NeoForge renamed the enclosing class). `ForgeRegistries.Keys.BIOME_MODIFIERS` → `NeoForgeRegistries.Keys.BIOME_MODIFIERS`.
- **`BlockModelsGen.java`** — `new ResourceLocation("block/cube")` → `ResourceLocation.withDefaultNamespace("block/cube")` for vanilla-namespaced parent references.
- **`CosmereDataGenerator.java`** — wired `event.getLookupProvider()` through to both `LootTableGen` and `RecipeGen` constructors.

**Build status after Phase 10**:
- `./gradlew compileJava` — **green** (0 errors, 2 deprecation warnings from pre-Phase 7 Curios integration).
- `./gradlew compileDatagenMainJava` — **green** (0 errors, 2 deprecation warnings in `CosmereTagBuilder` on `ITagBuilderExtension.removeElement/removeTag(ResourceLocation, String)` — slated-for-removal, replacement would be instance-scoped tag entries; functional).
- `./gradlew runData` (the actual data-gen execution) was **not** run — this phase verified compilation only. Running data-gen will require the module-specific datagen source sets to be ported first, since `CosmereDataGenerator.gatherData` only registers main-module providers (submodule registrations happen in each submodule's own data-gen entrypoint).

Notes / known residuals (not Phase 10 scope):
- `AllomancyRecipeGen` / `FeruchemyRecipeGen` / `HemalurgyRecipeGen` / `SurgebindingRecipeGen` / `SandmasteryRecipeGen` / `AwakeningRecipeGen` / `AonDorRecipeGen` / `AviarRecipeGen` / `SoulforgeryRecipeGen` / `ToolsRecipeGen` / `ExampleRecipeGen` — each has the old `(PackOutput, ExistingFileHelper, String)` ctor + `Consumer<FinishedRecipe>` shape. Per-module port passes will migrate to the new `(PackOutput, CompletableFuture<HolderLookup.Provider>, String)` + `RecipeOutput` shape.
- `CosmereTagBuilder` deprecation warnings: `builder.removeElement(rl, modID)` / `builder.removeTag(tag.location(), modID)` — both slated for removal in a future NeoForge minor. Not currently called from anywhere (only present to preserve API shape for submodules). Safe to leave; swap to the replacement when callers exist and the API lands.
- The generated `neoforge:fortune_bonus` loot-modifier JSON from Phase 9 has not yet been (re-)emitted by a `GlobalLootModifierProvider`. If any such provider existed in the old codebase it would live in submodule datagen, which is out of scope here.

---

### Phase 11.1 — Allomancy foundation (registries + config + event handlers + `@Mod` ctor)
Allomancy module errors: **362 → 239** (123 cleared). All 239 residuals are in 11.2/11.3/11.4 files; zero errors in 11.1's own scope (Allomancy.java, common/registries/, common/config/, common/eventHandlers/), modulo two cascade errors in `AllomancyItems` line 24 and `AllomancyRecipes` line 19 that are blocked on 11.3 ctor signature changes in `MistcloakItem` and `VialMixingRecipe`.

- `common/Allomancy.java` — `@Mod` ctor `() → (IEventBus modBus, ModContainer modContainer)` (matches the main `Cosmere` shape from Phase 1). Drops `onAddCaps` listener entirely (the Forge `RegisterCapabilitiesEvent` is gone — Phase 11.2 will reintroduce `IScadrial` registration as a NeoForge attachment). Drops `packetHandler.initialize()` from `commonSetup` and instead calls `packetHandler.register(modBus)` from the ctor (mirrors the new `BasePacketHandler` shape from Phase 5). `AllomancyConfigs.registerConfigs(modLoadingContext.getActiveContainer())` → `registerConfigs(modContainer)` (direct, matches Phase 3 main `CosmereConfigs`). `versionNumber = new Version(modLoadingContext.getActiveContainer())` → `new Version(modContainer)`. `new ResourceLocation(MODID, path)` → `ResourceLocation.fromNamespaceAndPath(MODID, path)`. Old single-config-handler logic (`config instanceof CosmereModConfig`) replaced with a shared `handleConfigEvent` that matches `event.getConfig().getSpec()` against the `AllomancyConfigs.CLIENT`/`SERVER` singletons via `ICosmereConfig.getConfigSpec()` — same pattern as `Cosmere.handleConfigEvent` (Phase 3). Imports moved `net.minecraftforge.*` → `net.neoforged.*`.
- `common/config/AllomancyConfigs.java` — `registerConfigs(ModLoadingContext)` → `registerConfigs(ModContainer)`; `modLoadingContext.getActiveContainer()` hop dropped. Imports moved.
- `common/config/AllomancyClientConfig.java` + `AllomancyServerConfig.java` — `ForgeConfigSpec.{Builder,BooleanValue,DoubleValue}` → `ModConfigSpec.*`; imports moved (`net.minecraftforge.common.ForgeConfigSpec` → `net.neoforged.neoforge.common.ModConfigSpec`, `net.minecraftforge.fml.config.ModConfig.Type` → `net.neoforged.fml.config.ModConfig.Type`). No semantic changes.
- `common/registries/` (10 files) — only 2 needed Forge→NeoForge edits; the other 8 already compiled cleanly because they consume the wrappers (`*DeferredRegister` / `*RegistryObject`) ported in Phase 1, plus they had no direct Forge imports:
  - `AllomancyCreativeTabs` — `net.minecraftforge.event.BuildCreativeModeTabContentsEvent` → `net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent`.
  - `AllomancyMenuTypes` — `net.minecraftforge.common.extensions.IForgeMenuType.create(...)` → `net.neoforged.neoforge.common.extensions.IMenuTypeExtension.create(...)` (NeoForge renamed the interface).
  - `AllomancyAttributes`, `AllomancyDamageTypesRegistry`, `AllomancyEffects`, `AllomancyEntityTypes`, `AllomancyManifestations`, `AllomancyStats` — verified clean (no edits needed).
  - `AllomancyItems` and `AllomancyRecipes` — left as-is. The known cascade errors (`Holder<ArmorMaterial>` for `MistcloakItem(...)`, `(CraftingBookCategory)` ctor for `VialMixingRecipe::new`) are 11.3 scope (the item / recipe classes themselves need the new signatures).
- `common/eventHandlers/` (4 files) — uniform Forge→NeoForge swap (mirrors Phase 6 main):
  - `AllomancyCommonEvents` — `BasicItemListing`, `RegisterCommandsEvent`, `ServerStartedEvent`, `ServerStoppingEvent`, `VillagerTradesEvent`, `SubscribeEvent`, `Mod.EventBusSubscriber` → NeoForge package paths. `@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)` → `@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)`.
  - `AllomancyModBusEventHandler` — same package sweep + the Phase 6 `EntityAttributeModificationEvent.add(EntityType, Attribute)` → `add(EntityType<? extends LivingEntity>, Holder<Attribute>)` migration. Added a private `holder(Attribute) -> Holder<Attribute>` helper that wraps via `BuiltInRegistries.ATTRIBUTE.wrapAsHolder(...)`. Loop variable typed as `EntityType<? extends LivingEntity>` to match `ModBusEventHandler.ENTITIES_THAT_CAN_HAVE_POWERS`'s tightened type.
  - `AllomancyEntityEventHandler` — package sweep on `LivingEntityUseItemEvent.Finish` and `PlayerInteractEvent.EntityInteract` (both still exist in NeoForge 1.21.1). The `event.isCanceled()` guard on `LivingEntityUseItemEvent.Finish` was dropped (the event still has cancellation but the guard was never load-bearing — matches Phase 6 main pattern). **Three handler methods stubbed as TODO Phase 11.3 markers** — `LivingAttackEvent`, `LivingHurtEvent`, and `EntityItemPickupEvent` were all removed in NeoForge 1.21.1 (the first two collapsed into `LivingIncomingDamageEvent` / `LivingDamageEvent.Pre`; the third renamed to `ItemEntityPickupEvent.Pre` with a different cancellation surface). The dispatch lines call `AllomancyAtium.onLivingAttackEvent(event)` / `AllomancyNicrosil`/`Pewter`/`Chromium.onLivingHurtEvent(event)` / `CoinPouchItem.onPickupItem(event.getItem(), event.getEntity())` — all of those callees need their event-type signatures updated in 11.3 (manifestations + items), so it makes sense to re-introduce the dispatchers in lockstep then.
  - **Deleted `common/eventHandlers/AllomancyCapabilitiesHandler.java`.** The file was entirely Forge cap plumbing — `AttachCapabilitiesEvent<Level>` for `IScadrial` (gone — replaced by NeoForge attachments, Phase 11.2 scope) plus a `TickEvent.LevelTickEvent` handler whose body was a no-op TODO. Mirrors Phase 4 main's deletion of `CapabilitiesHandler.java`. Phase 11.2 will re-introduce a level-tick handler in lockstep with the `ScadrialCapability` → attachment migration if needed.

**Build status after Phase 11.1**:
- `./gradlew compileAllomancyJava` — **239 errors** (down from 362). All residuals are out-of-scope for this sub-phase: capabilities/networking/spiritweb (11.2), items/manifestations/effects/entities/coinpouch/recipes/compat/mixin (11.3), client (11.4). Two cascade errors in 11.1 registries (`AllomancyItems:24`, `AllomancyRecipes:19`) will clear automatically when 11.3 updates `MistcloakItem` and `VialMixingRecipe` ctor signatures.

Notes:
- The `AllomancyEntityEventHandler` TODO comments encode the original dispatch ordering (Atium attack → Nicrosil/Pewter/Chromium hurt) and event types so 11.3 has the full intent recoverable from the file alone (no need to dig git history).

### Phase 11.2 — Allomancy capabilities + networking + spiritweb submodule + DrawHelper
Allomancy module errors: **239 → 194** (45 cleared). Zero errors in 11.2's own scope (`common/capabilities/**`, `common/network/**`, `src/api/.../helpers/DrawHelper.java`). All 194 residuals are 11.3 (items/manifestations/effects/coinpouch/recipes/compat/mixin) or 11.4 (client) scope.

**Networking sub-phase** (`common/network/**`):
- `common/network/packets/PlayerShootProjectileMessage.java` — rewrote as `record PlayerShootProjectileMessage() implements ICosmerePacket`. `TYPE` uses `Allomancy.rl("player_shoot_projectile")`, `STREAM_CODEC = StreamCodec.unit(new PlayerShootProjectileMessage())`. `handle(NetworkEvent.Context)` → `handle(IPayloadContext)` with `context.player() instanceof ServerPlayer` guard + `context.enqueueWork(...)` — same shape as `DeactivateManifestationsMessage` (Phase 5). Forge `NetworkEvent`/`MinecraftServer.submitAsync` path dropped.
- `common/network/packets/EntityAllomancyActivateMessage.java` — rewrote as `record EntityAllomancyActivateMessage(int metalId, boolean isSingleTarget, int singleTargetEntityID) implements ICosmerePacket`. Dropped `CompoundTag` wrapping — typed record fields now carry the payload directly. `STREAM_CODEC = StreamCodec.composite(VAR_INT, metalId, BOOL, isSingleTarget, VAR_INT, singleTargetEntityID, ::new)`. Preserved a convenience ctor `(Metals.MetalType, boolean, int) -> this(metalType.getID(), ...)` so the `AllomancyBrass`/`AllomancyZinc` call sites compile unchanged. `FriendlyByteBuf`/`encode`/`decode` removed.
- `common/network/AllomancyPacketHandler.java` — dropped Forge `SimpleChannel`/`createChannel`/`getChannel`/`NETWORK_CHANNEL`. Now overrides `getProtocolVersion()` returning `Allomancy.instance.versionNumber.toString()` and `initialize(PayloadRegistrar registrar)` — mirrors `NetworkPacketHandler` shape (Phase 5). Registers all three packets via `registrar.playToServer(TYPE, STREAM_CODEC, ICosmerePacket::handle)`: `PlayerShootProjectileMessage`, `SyncPushPullMessage` (main-source packet but semantically allomancy, registered on this channel as in the Forge era), `EntityAllomancyActivateMessage`. `Allomancy.java` ctor already wired `packetHandler.register(modBus)` in 11.1.

**Capabilities sub-phase** (`common/capabilities/world/**` — first world-level NeoForge attachment in the repo):
- `common/capabilities/world/IScadrial.java` — swapped imports: `net.minecraftforge.common.util.INBTSerializable` → `net.neoforged.neoforge.common.util.INBTSerializable`; `net.minecraftforge.client.event.ViewportEvent` → `net.neoforged.neoforge.client.event.ViewportEvent`. Interface shape unchanged.
- `common/capabilities/world/AllomancyAttachments.java` (new) — registers `SCADRIAL` as `AttachmentType<ScadrialCapability>` against `NeoForgeRegistries.ATTACHMENT_TYPES`, using `.serializable((IAttachmentHolder holder) -> new ScadrialCapability((Level) holder)).build()`. Mirrors `SpiritwebAttachments` pattern from Phase 4 but with `Level` as the holder type instead of `LivingEntity`. NeoForge auto-attaches to every `Level` — the prior Forge `AttachCapabilitiesEvent<Level>` filter capability is lost, but the existing `AllomancyClientEvents.onRenderFog` handler already bails on non-relevant conditions, so this is acceptable (TODO in existing code already flagged that dimension filtering wasn't wired up).
- `common/capabilities/world/ScadrialCapability.java` — dropped `Capability`/`CapabilityManager`/`CapabilityToken`/`LazyOptional` (Forge cap plumbing). `get(Level)` now returns `Optional<IScadrial>` via `level.getData(AllomancyAttachments.SCADRIAL.get())` (matches `SpiritwebCapability.get` shape). `serializeNBT()`/`deserializeNBT(CompoundTag)` → `serializeNBT(HolderLookup.Provider)` / `deserializeNBT(HolderLookup.Provider, CompoundTag)` (1.21 `INBTSerializable<CompoundTag>` signature change). `@OnlyIn(Dist.CLIENT)` + `ViewportEvent` imports moved `net.minecraftforge.*` → `net.neoforged.*`. Field `level` now `final`.
- `common/Allomancy.java` — added `AllomancyAttachments.ATTACHMENT_TYPES.register(modBus)` to the ctor registration block. No other changes.
- **Deferred to 11.4**: `client/eventHandlers/AllomancyClientEvents.java` (sole caller of `ScadrialCapability.get(Level)`) still uses the Forge `LazyOptional<IScadrial>` + `.resolve()` + `.isPresent()` pattern at lines 51–60 and needs to be switched to `Optional<IScadrial>` / `.ifPresent(...)`. File is already broken on Forge `Dist`/`SubscribeEvent`/`LazyOptional` imports — full sweep happens in 11.4.

**Spiritweb submodule + DrawHelper sub-phase** (restored the Phase 0.8 API stub):
- `common/capabilities/AllomancySpiritwebSubmodule.java` — Forge imports (`Dist`, `OnlyIn`, `RenderLevelStageEvent`) → NeoForge. `Minecraft.getInstance().getFrameTime()` → `Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false)` (1.20.5 introduced `DeltaTracker`; `getFrameTime()` was removed). Mirrors the precedent at `main/.../mixin/LightTextureMixin.java:81`. No other semantic changes.
- `src/api/.../helpers/DrawHelper.java` — **restored the real render bodies** (Phase 0.8 had stubbed them as no-ops so the `api` source set could compile). 1.21 vertex-builder rewrite per the `port_status.md` cheatsheet: `vertex(matrix, x, y, z).color(...).normal(Matrix3f, ...).uv(...).overlayCoords(...).uv2(...).endVertex()` → `addVertex(matrix, x, y, z).setColor(...).setNormal(Pose, ...).setUv(...).setOverlay(...).setLight(...)` with no terminator call. `setNormal` now takes a `PoseStack.Pose` (not a `Matrix3f`), so the helper threads `poseStack.last()` through for both position and normal. `new ResourceLocation("minecraft", "textures/particle/note.png")` → `ResourceLocation.fromNamespaceAndPath(...)`. `RenderType.create(...)`, `LineStateShard(OptionalDouble.of(2.5d))`, `CompositeState.builder()`, `RENDERTYPE_LINES_SHADER`, `POSITION_COLOR_SHADER`, `POSITION_TEX_SHADER`, `NO_TEXTURE`, `VIEW_OFFSET_Z_LAYERING`, `TRANSLUCENT_TRANSPARENCY`, `TRANSLUCENT_TARGET`, `COLOR_WRITE`, `NO_CULL`, `NO_DEPTH_TEST`, `Util.memoize(...)` all verified unchanged in 1.21.1. `compileApiJava` passes.
- `common/manifestation/AllomancyAtium.java` — **left as-is**, now explicitly 11.3 scope. The Phase 4 cap-consumption port (`LazyOptional<ISpiritweb>` → `Optional<ISpiritweb>` in `onLivingAttackEvent`) already landed; the remaining 2 errors (`net.minecraftforge.event.entity.living.LivingAttackEvent` and `event.isCanceled()` / `event.setCanceled(true)`) are the `LivingAttackEvent` → `LivingIncomingDamageEvent` event-type migration. Per Phase 11.1 note on `AllomancyEntityEventHandler`, this is tracked with the other `LivingAttackEvent`/`LivingHurtEvent`/`EntityItemPickupEvent` dispatchers in 11.3. The "`AllomancyAtium` was already partly ported in Phase 4 — finish here" note in the phase table was premature; the remaining port is the event-type swap, which belongs with the other event-type swaps in 11.3.

**Build status after Phase 11.2**:
- `./gradlew compileApiJava` — **passes** (DrawHelper restoration verified).
- `./gradlew compileAllomancyJava` — **194 errors** (down from 239). All residuals are 11.3 (items/manifestations/effects/coinpouch/recipes/compat/mixin) or 11.4 (client) scope. Two known cascade errors in 11.1 files (`AllomancyItems:24`, `AllomancyRecipes:19`) persist — blocked on 11.3 ctor signature changes.

### Phase 11.3 — Allomancy manifestations + gameplay items/entities/effects/coinpouch
Allomancy module errors: **194 → 60** (134 cleared). All 60 residuals are 11.4 (client) scope — zero server-side errors remain.

**Manifestations + effects sub-phase**:
- `AllomancyManifestation.java` — `new ResourceLocation(ns, path)` → `ResourceLocation.fromNamespaceAndPath`; registry `.getValue(rl)` → `.get(rl)`.
- `api/.../helpers/EffectsHelper.java` — both `getNewEffect` overloads changed to `Holder<MobEffect>` parameter (removed internal `wrapAsHolder`).
- `api/.../cosmereEffect/CosmereEffect.java` — added `Holder<Attribute>` overload for `addAttributeModifier` (delegates to `attribute.value()`).
- `AllomancyAtium.java` — `LivingAttackEvent` → `LivingIncomingDamageEvent`; dropped stale `isCanceled()` guard.
- `AllomancyBrass.java` — wrapped raw `MobEffect` with `BuiltInRegistries.MOB_EFFECT.wrapAsHolder(...)`.
- `AllomancyBronze.java` — `Attribute` field → `Holder<Attribute>` via `BuiltInRegistries.ATTRIBUTE.wrapAsHolder(...)`.
- `AllomancyChromium.java`, `AllomancyNicrosil.java` — `LivingHurtEvent` → `LivingDamageEvent.Pre`; `getAmount()`/`setAmount()` → `getNewDamage()`/`setNewDamage()`.
- `AllomancyPewter.java` — same event migration.
- `AllomancyTin.java` — Forge `Dist`/`OnlyIn`/`PlaySoundEvent` → neoforged; `getFeetBlockState()` → `getInBlockState()`.
- `AllomancyIronSteel.java` — Forge `Dist`/`OnlyIn` → neoforged; `Collection<Recipe<?>>` → `Collection<RecipeHolder<?>>` with `.value()` unwrap.
- `BrassStunEffect.java` — removed `addAttributeModifiers(LivingEntity, AttributeMap, int)` (gone in 1.21.1); added `onEffectAdded(LivingEntity, int)` for `setNoAi(true)`; added static `clearStun(LivingEntity)` for event-wired removal.
- `CopperCloudEffect.java`, `PewterBurnEffect.java`, `AllomancyBoostEffect.java` — `ADDITION` → `ADD_VALUE`, `MULTIPLY_TOTAL` → `ADD_MULTIPLIED_TOTAL`.

**Items sub-phase**:
- `MistcloakItem.java` — constructor `ArmorMaterial` → `Holder<ArmorMaterial>`; `getArmorTexture` updated to 1.21.1 signature returning `ResourceLocation`.
- `CoinPouchItem.java` — Forge imports → neoforged; `NetworkHooks.openScreen` → `player.openMenu`; `getUseDuration(ItemStack)` → `getUseDuration(ItemStack, LivingEntity)`; `Enchantments.INFINITY_ARROWS` → registry lookup of `Enchantments.INFINITY`; removed `initCapabilities` (→ `RegisterCapabilitiesEvent`); `ForgeCapabilities.ITEM_HANDLER` → `Capabilities.ItemHandler.ITEM`; `ForgeHooks.getProjectile` replaced with direct stack return; added `shootProjectile(...)` abstract method impl (no-op; custom shoot path used).
- `MetalVialItem.java` — Forge `Dist`/`OnlyIn` → neoforged; `getUseDuration(ItemStack)` → `getUseDuration(ItemStack, LivingEntity)`; `appendHoverText(ItemStack, Level, ...)` → `appendHoverText(ItemStack, Item.TooltipContext, ...)`; `stack.getOrCreateTag()`/`getOrCreateTagElement(String)` → `DataComponents.CUSTOM_DATA` pattern (`getCustomData`/`setCustomData` helpers); write-through applied to `addMetals`/`emptyMetals`.

**CoinPouch container trio**:
- `CoinPouchInventory.java` — stripped `ICapabilityProvider`/`LazyOptional`/`ForgeCapabilities`; now a plain wrapper around `ItemStackHandler(18)` with `getHandler()` accessor; capability registration deferred to `RegisterCapabilitiesEvent`.
- `CoinPouchContainerMenu.java` — `ForgeCapabilities.ITEM_HANDLER.orElse(null)` → `Capabilities.ItemHandler.ITEM` (direct return).
- `CoinPouchSlot.java` — Forge `items.*` → neoforged.

**Entities**:
- `CoinProjectile.java` — removed `implements ItemSupplier`; constructor super call updated to `AbstractArrow(EntityType, LivingEntity, Level, ItemStack, null)` (1.21.1 signature); `getPickupItem()` → `getDefaultPickupItem()` (renamed abstract method); added plain `getItem()` method (no longer in AbstractArrow hierarchy — needed by `CoinPouchItem.onPickupItem`).

**Recipes**:
- `VialMixingRecipe.java` — Forge `Tags` → neoforged; `CustomRecipe(ResourceLocation, CraftingBookCategory)` → `CustomRecipe(CraftingBookCategory)` (ResourceLocation removed in 1.21.1); `matches`/`assemble` parameter `CraftingContainer` → `CraftingInput`, `RegistryAccess` → `HolderLookup.Provider`; `inv.getContainerSize()` → `inv.size()`; removed `getId()` override (no longer in Recipe API).

**Commands**: `AllomancyCommands.java`, `FillMetalReservesCommand.java` — no changes needed (clean MC command API).

**Compat**: `HwylaCompat.java`, `BronzeSeekerTooltip.java` — no code changes; added `compileOnly "maven.modrinth:jade:${jade_version}"` to `build.gradle` (was `runtimeOnly`-only, so Jade API wasn't on compile classpath).

**Mixin**: `EntityMixin.java` — no changes needed (clean Mixin/vanilla API).

**Event handler re-wire** (`AllomancyEntityEventHandler.java`):
- Added `onItemPickup(ItemEntityPickupEvent.Pre)` — calls `CoinPouchItem.onPickupItem`; denies via `event.setCanPickup(TriState.FALSE)`.
- Added `onLivingIncomingDamage(LivingIncomingDamageEvent)` → dispatches to `AllomancyAtium.onLivingAttackEvent`.
- Added `onLivingDamagePre(LivingDamageEvent.Pre)` → dispatches to Nicrosil, Pewter, Chromium `onLivingHurtEvent`.
- Added `onMobEffectRemoved(MobEffectEvent.Remove)` + `onMobEffectExpired(MobEffectEvent.Expired)` → call `BrassStunEffect.clearStun` when the stun effect ends.

**Build status after Phase 11.3**:
- `./gradlew compileAllomancyJava` — **60 errors**, all in `allomancy.client.*`. Zero server-side or common errors remain in allomancy. Phase 11.4 (client + datagen) is the final allomancy step.

---

## Remaining (in suggested order)

Each submodule is its own phase, covering `src/<module>/` + `src/datagen/<module>/` + `src/gameTest/<module>/` (where present). Per-module scope template: packet handler (to `BasePacketHandler`'s new shape), recipe gens (to new `RecipeProvider` shape), item/block ports (`DataComponents`, `AttributeModifier(ResourceLocation, …)`), config registrations (`ForgeConfigSpec` → `ModConfigSpec`, `ModLoadingContext` → injected `ModContainer`), capability→attachment where applicable, `@Mod` ctor to `(IEventBus, ModContainer)`, event-handler annotation swaps.

| # | Phase | Module | Notes |
|---|---|---|---|
| 11 | **Per-submodule port — allomancy** | `src/allomancy/` + `src/datagen/allomancy/` (`src/gameTest/allomancy/` does not exist) | Metal-burning (Mistborn). 72 main + 8 datagen Java files — the heaviest submodule. Split into 4 sub-phases below. |
| 11.3 | **allomancy — manifestations + gameplay items/entities/effects/coinpouch** | `common/manifestation/**`, `common/items/**`, `common/entities/**`, `common/effects/**`, `common/coinpouch/**`, `common/recipes/**`, `common/commands/**`, `common/compat/**`, `mixin/**` | 16 metal manifestation classes (`AllomancyAluminum`, `AllomancyBendalloy`, `AllomancyBrass`, `AllomancyBronze`, `AllomancyCadmium`, `AllomancyChromium`, `AllomancyCopper`, `AllomancyDuralumin`, `AllomancyElectrum`, `AllomancyGold`, `AllomancyIronSteel`, `AllomancyMalatium`, `AllomancyNicrosil`, `AllomancyPewter`, `AllomancyTin`, `AllomancyZinc`) + `AllomancyManifestation` base + `AllomancyEntityThread`; 3 items (`CoinPouchItem`, `MetalVialItem`, `MistcloakItem`); `CoinProjectile` entity; 4 effects (`AllomancyBoostEffect`, `BrassStunEffect`, `CopperCloudEffect`, `PewterBurnEffect`); CoinPouch trio (`CoinPouchContainerMenu`/`CoinPouchInventory`/`CoinPouchSlot`); `VialMixingRecipe`; `FillMetalReservesCommand` + `AllomancyCommands`; `BronzeSeekerTooltip` + `HwylaCompat` (verify Jade 1.21 surface); `EntityMixin`; `MiscHelper`. Mostly mechanical: `Holder<Attribute>`, `Holder<MobEffect>`, `DataComponents`, `AttributeModifier(ResourceLocation,…)`, Curios 9.x. Also includes `AllomancyAtium.onLivingAttackEvent` + the 3 dispatcher stubs in `AllomancyEntityEventHandler` — all gated on the `LivingAttackEvent`/`LivingHurtEvent`/`EntityItemPickupEvent` → `LivingIncomingDamageEvent`/`LivingDamageEvent.Pre`/`ItemEntityPickupEvent.Pre` migration. |
| 11.4 | **allomancy — client + datagen** | `client/**`, `src/datagen/allomancy/**`, `src/allomancy/resources/META-INF/neoforge.mods.toml` (verify) | Client: `AllomancyKeybindings`, `AllomancyClientEvents`/`AllomancyModClientEvents`, `CoinPouchContainerScreen`, `BlockScanResult`/`IronSteelLinesThread`/`ScanResult`, `AllomancyLayerDefinitions`/`AllomancyRenderers`/`MistcloakModel`/`MistcloakRenderer`. `AllomancyClientEvents.onRenderFog` specifically needs its `LazyOptional<IScadrial>` consumption at lines 51–60 swapped to `Optional<IScadrial>` — `ScadrialCapability.get(Level)` changed contract in 11.2. Datagen: all 8 files (`AllomancyDataGenerator`, `AllomancyDatapackRegistryProvider`, `AllomancyEngLangGen`, `AllomancyItemModelsGen`, `AllomancyRecipeGen`, `AllomancyTagProvider`, `AllomancyPatchouliGen`, `PatchouliAllomancyCategory`) — same shape as the main-source datagen port in Phase 10 (`(PackOutput, CompletableFuture<HolderLookup.Provider>, String)` ctor, `Consumer<FinishedRecipe>` → `RecipeOutput`, `BiomeModifiers` rename, etc.). |
| 12 | **Per-submodule port — feruchemy** | `src/feruchemy/` + `src/datagen/feruchemy/` + `src/gameTest/feruchemy/` | Metal-storing (Mistborn). `ChargeableMetalCurioItem` partially touched in Phase 7. `StackNBTHelper#serializeStack` stub (Phase 0.8) needs real `ItemStack#save(HolderLookup.Provider)` here. `FeruchemyConfig`/`FeruchemyConfigs`, `FeruchemyRecipeGen`. |
| 13 | **Per-submodule port — hemalurgy** | `src/hemalurgy/` + `src/datagen/hemalurgy/` + `src/gameTest/hemalurgy/` | Spike-based (Mistborn). World cap still Forge-era. `HemalurgyConfig`/`HemalurgyConfigs`, `HemalurgyRecipeGen`. Phase 7 already fixed copper-spike `getExperienceReward` + attribute Holder lookup in `api/Metals.java`. |
| 14 | **Per-submodule port — surgebinding** | `src/surgebinding/` + `src/datagen/surgebinding/` + `src/gameTest/surgebinding/` | Knights Radiant (Stormlight). `SurgebindingPacketHandler` still broken. `SurgeGravitation` partly ported in Phase 4. `DynamicShardbladeData` item cap. `SurgebindingConfig`/`SurgebindingConfigs`, `SurgebindingRecipeGen`. Has its own AT file listed in `build.gradle`. |
| 15 | **Per-submodule port — sandmastery** | `src/sandmastery/` + `src/datagen/sandmastery/` + `src/gameTest/sandmastery/` | Sand manipulation (White Sand). `SandmasteryPacketHandler` still broken. `SandPouch` item inventory still Forge-era. `SandmasteryConfig`/`SandmasteryConfigs`, `SandmasteryRecipeGen`. |
| 16 | **Per-submodule port — awakening** | `src/awakening/` + `src/datagen/awakening/` + `src/gameTest/awakening/` | Biochromatic Breath (Warbreaker). `AwakeningConfig`/`AwakeningConfigs`, `AwakeningRecipeGen`. |
| 17 | **Per-submodule port — aondor** | `src/aondor/` + `src/datagen/aondor/` + `src/gameTest/aondor/` | Aon-based magic (Elantris). `AonDorConfig`/`AonDorConfigs`, `AonDorRecipeGen`. |
| 18 | **Per-submodule port — aviar** | `src/aviar/` + `src/datagen/aviar/` + `src/gameTest/aviar/` | Bird companions (Sixth of Dusk). `AviarConfig`/`AviarConfigs`, `AviarRecipeGen`. |
| 19 | **Per-submodule port — soulforgery** | `src/soulforgery/` + `src/datagen/soulforgery/` + `src/gameTest/soulforgery/` | Soul manipulation. `SoulforgeryConfig`/`SoulforgeryConfigs`, `SoulforgeryRecipeGen`. |
| 20 | **Per-submodule port — cosmeretools** | `src/cosmeretools/` + `src/datagen/cosmeretools/` + `src/gameTest/cosmeretools/` | Dev commands/utilities. `ToolsRecipeGen`. Lighter touch; mostly command argument types + datagen. |
| 21 | **Per-submodule port — example** | `src/example/` + `src/datagen/example/` + `src/gameTest/example/` | Dev-only template module. `ExampleRecipeGen`. Confirm whether to keep (see Open questions: "`example` module"). |

---

## Open questions / decisions to confirm

- **Submodule platform deps.** Submodule `neoforge.mods.toml` files currently only declare `cosmere` + sibling deps. If submodules should declare `minecraft` + `neoforge` explicitly (as in modern NeoForge practice), add those blocks.
- **MDG `mods { create(name) { … } }` inside a Groovy `for` loop.** If ModDevGradle rejects the iteration pattern, rewrite with `secondaryModules.each { name -> mods.create(name) { … } }`.
- **Access transformer scope.** Only `main`, `allomancy`, `surgebinding` AT files are listed in `build.gradle`. Confirm no other module accumulated ATs.
- **Parchment version.** `2024.11.17` is reasonable; bump freely.
- **`example` module.** Still in `build.gradle` secondaryModules but not in publishing. Keep as dev-only?
- **Version API.** `new Version(ModContainer)` — confirm the `leaf.cosmere.api.Version` constructor accepts a `ModContainer` (vs the old `ModLoadingContext.get().getActiveContainer()`).
- **Submodule configs.** Each submodule has its own `*Configs.java` / `*Config.java` pair (allomancy, feruchemy, hemalurgy, surgebinding, sandmastery, awakening, aondor, aviar, cosmeretools, soulforgery, example) still on `ForgeConfigSpec` + `ModLoadingContext`. Phase 3 here covered only the main source set; submodules are deferred to their per-module port passes (and each submodule's `*Config.java` will match the same shape as `CosmereClientConfig` etc).

## Notable API migration cheatsheet (for future passes)

| Forge 1.20.1 | NeoForge 1.21.1 |
|---|---|
| `net.minecraftforge.*` | `net.neoforged.*` (mostly) |
| `net.minecraftforge.common.*` | `net.neoforged.neoforge.common.*` |
| `net.minecraftforge.registries.ForgeRegistries.X` | `net.minecraft.core.registries.BuiltInRegistries.X` / `net.neoforged.neoforge.registries.NeoForgeRegistries.X` |
| `RegistryObject<T>` | `DeferredHolder<R, T>` |
| `@Mod(MODID)` + `public MyMod()` | `@Mod(MODID)` + `public MyMod(IEventBus modBus, ModContainer modContainer)` |
| `FMLJavaModLoadingContext.get().getModEventBus()` | injected `IEventBus` param |
| `ModLoadingContext.get().getActiveContainer()` | injected `ModContainer` param |
| `new ResourceLocation(ns, path)` | `ResourceLocation.fromNamespaceAndPath(ns, path)` |
| `ForgeConfigSpec` | `ModConfigSpec` |
| `SimpleChannel` + `NetworkEvent.Context` | `PayloadRegistrar` + `CustomPacketPayload` + `StreamCodec` + `IPayloadContext` |
| `Capability<T>` + `LazyOptional<T>` + `AttachCapabilitiesEvent` | `AttachmentType<T>` + `NeoForgeRegistries.ATTACHMENT_TYPES` + auto-attach |
| `@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)` | `@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)` |
| `RegistryBuilder.hasTags().setDefaultKey(rl)` | `RegistryBuilder.defaultKey(rl)` (tags always on) |
| `DeferredRegister.makeRegistry(Supplier<RegistryBuilder<T>>)` | `DeferredRegister.makeRegistry(Consumer<RegistryBuilder<T>>)` |
| `curios-forge` / Forge curios API | `curios-neoforge` / NeoForge curios 9.x API |
| Item NBT via `ItemStack#getTag()` | `DataComponents` (`ItemStack#get(DataComponentType)`) |
| `Component.literal(...)` | unchanged |
| Tag providers: `ForgeRegistryTagsProvider` | `TagsProvider<T>` with `CompletableFuture<HolderLookup.Provider>` lookup param |
