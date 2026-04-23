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
- **Build verification blocked by pre-existing Phase 0 issue**: `./gradlew compileApiJava` currently fails with 1,026 errors along the lines of `package net.minecraft.client does not exist` / `package net.minecraft.network.chat does not exist` against files in `src/api/` (e.g. `Activator.java`, `TextHelper.java`, `Constants.java`). The `api` source set is declared under `neoForge.mods.cosmere.sourceSet sourceSets.api` but isn't resolving Minecraft types at compile time. Needs a Phase 0 fix before `compileMainJava` can be reached. Phase 5 code was reviewed for correctness against the 1.21.1 PayloadRegistrar / CustomPacketPayload / StreamCodec / IPayloadContext contracts but has not been compiler-verified.

---

## Remaining (in suggested order)

| # | Phase | Scope | Complexity |
|---|---|---|---|
| 0.75 | **API source-set classpath fix** | `build.gradle` — `src/api/` not seeing Minecraft classes (1,026 compile errors: `net.minecraft.client`, `net.minecraft.network.chat` etc. reported as missing packages despite `mods.cosmere.sourceSet sourceSets.api`). | Likely a ModDevGradle classpath wiring tweak (e.g. the `extendConfigurations` block for `api*` configs vs. the mod-output classpath that NeoForge injects). Blocks all downstream compile verification. |
| 6 | **Event handlers** | `common/eventHandlers/**` ~10 files | `@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)` → `@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)`; package swaps; several events renamed/moved; `AttachCapabilitiesEvent` deleted entirely |
| 7 | **Items / blocks / entities** | `items/`, `blocks/`, entity-related registries — largest bucket | Creative tab API tweaks; `Item.Properties` changes; Curios 9.x API; attributes use `Holder<Attribute>` now; `BlockItem` / `BlockEntityType` updates; tooltip `appendHoverText` signature changed (`Item.TooltipContext` param) |
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
