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

---

## Remaining (in suggested order)

| # | Phase | Scope | Complexity |
|---|---|---|---|
| 8 | **Commands / argument types** | `common/commands/**` — partial cleanup already done in Phase 7; remaining issues are limited | Minor; most errors are cleared. Check any remaining permission/suggestion API tweaks. |
| 8 | **Commands / argument types** | `common/commands/**` ~8 files | `ResourceLocation` construction; argument-type registration pattern is the same shape but package moved; permission level API unchanged |
| 9 | **Loot / world / features / biomes** | `loot/`, `world/`, feature+biome modifier registries | Loot function `Codec` → `MapCodec`; biome modifier codecs use `MapCodec`; global loot modifier serializer shape changed |
| 10 | **Datagen** | `src/datagen/main/**` 25 files | `GatherDataEvent` + `PackOutput` pattern; provider constructors updated; `DatapackBuiltinEntriesProvider` for worldgen; tag providers take `CompletableFuture<HolderLookup.Provider>` lookup; recipe provider uses `RecipeOutput` |

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
