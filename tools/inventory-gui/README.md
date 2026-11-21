# Inventory GUI Maker

Generates vanilla-style container textures (panel, slot grids, freely placed machine slots, progress
widgets, player inventory and hotbar) plus the matching Java. Output is pixel-identical to vanilla
`generic_54.png` for a 54-slot chest, and to `furnace.png` for a furnace layout apart from the
flame/arrow art (the built-in widget sprites are original, not copies).

Everything is drawn procedurally; there is no template image. No dependencies beyond Node (CLI) or a
browser (GUI).

## GUI

Browsers block scripts loaded from `file://` in some setups, so serve the folder:

```bash
python -m http.server 8765 --bind 127.0.0.1 --directory tools/inventory-gui
# then open http://127.0.0.1:8765/
```

- **Grid**: slot count, columns, grid position (centered/left), last-row alignment.
- **Placed slots**: choose *Add slot* / *Add large slot* and click the preview. Drag to move, arrow
  keys nudge (Shift = snap step), Del removes, Ctrl+Z undoes. Each slot has a role: *output* slots
  become take-only in the exported Menu and are skipped by shift-click. *Grid → placed* converts
  the grid into individually editable slots.
- **Sprites**: pick one next to *Add sprite* and click to place; edited like slots. Each sprite's
  behaviour is either *progress* (fill → ← ↑ ↓; synced and cropped, gets a `WIDGET_<NAME>` constant)
  or *static* (plain decoration in grey or colour; no texture space, data or code).
- **Custom sprites**: upload a filled PNG (and optionally an empty one; otherwise a grey silhouette
  is used). Tall sprites default to filling upward, wide ones rightward.
- **Preview**: the *Fill* slider shows widgets part-filled; hover shows slot/widget details. Overlays
  (labels, indexes, fill, selection, overlap warnings) never end up in the PNG.
- **Save / Load layout** writes and reads the layout JSON, the same format as the CLI's
  `--save-config` / `--config` (custom sprites are embedded as data URLs).
- Settings persist in the browser between visits.

## CLI (headless)

Everything the GUI does is available headless, which is also how to script it or check a layout
without a browser.

```bash
node tools/inventory-gui/cli.mjs --slots 27 --out path/to/texture.png

# furnace-style machine with progress widgets, a preview image, and code written into the repo:
node tools/inventory-gui/cli.mjs --slots 0 --height 166 \
    --slot 55,16 --slot 55,52 --slot 111,30,large,output \
    --widget flame,56,36 --widget arrow,79,34 \
    --preview crusher_preview.png --zoom 4 --fill 60 \
    --export screen,menu,registration --name Crusher --module sandmastery --write

# custom sprite (empty defaults to a silhouette), named widget, explicit direction:
node tools/inventory-gui/cli.mjs --slots 4 --sprite tank=tank.png --widget tank,150,20,up,name=water

# round-trip with the GUI, and machine-readable coordinates:
node tools/inventory-gui/cli.mjs --config crusher.layout.json --json --out crusher.png
node tools/inventory-gui/cli.mjs ... --save-config crusher.layout.json
```

`--help` lists everything. Flags override `--config` values, and list flags (`--slot`, `--widget`,
`--sprite`) add to the config's lists. `--write` never overwrites existing files without `--force`;
snippet exporters (no target path) are always printed. `--json` prints panel size, label positions,
every slot (box and item coordinates), every widget (position, size, atlas uv) and overlaps.

## Built-in sprites

Original art at the sizes of the vanilla screens they echo. Progress sprites default to progress mode
and icons to static colour; any sprite can be switched either way.

| Sprite | Size | Default | Typical use |
|--------|------|---------|-------------|
| `arrow` | 24×16 | progress → | furnace/smoker progress; static grey for crafting-style screens |
| `arrow_down` | 9×28 | progress ↓ | brewing-style progress |
| `bubbles` | 12×29 | progress ↑ | brewing-style activity |
| `fuel_bar` | 18×4 | progress → | blaze-powder-style fuel |
| `flame` | 14×14 | progress ↑ | furnace fuel |
| `gauge` | 12×48 | progress ↑ | energy/fluid level |
| `plus` | 13×13 | static | smithing/combining |
| `cross` | 15×15 | static | anvil-style "can't" marker |
| `check` | 15×12 | static | beacon-style confirm |

On the CLI the behaviour goes in the `--widget` options: a direction (`right`/`left`/`up`/`down`),
`progress`, `static` (grey) or `static-filled` (colour), e.g. `--widget arrow,90,35,static`.

## Layout conventions

- Coordinates on placed slots and widgets are the **box** top-left. `Slot(x, y)` is the item position:
  box + 1 for 18px slots, box + 5 for 26px (result) slots.
- Panel height is the real drawn height. Vanilla `ChestScreen` reports one pixel more because its
  texture has a seam it blits around; this tool emits one continuous image for a single `blit`.
- The player inventory is anchored to the bottom; extra height opens space above it. Inventory
  label sits 12px above the first inventory row (`imageHeight - 94` at minimum height).
- Canvas defaults to 256, the size `GuiGraphics.blit` assumes. Larger canvases export the
  overload that takes the texture size.
- Widgets: the empty sprite is drawn onto the panel; each distinct filled sprite is packed once into
  free canvas space (columns right of the panel, then rows below it). The Screen blits a cropped part
  of it: `right`/`left` crop width, `up`/`down` crop height. With built-ins at vanilla's furnace
  positions the atlas lands at u=176 (flame at v=0, arrow at v=14).
- Vanilla 1.21 draws furnace progress with `blitSprite` from separate sprite files; this tool keeps
  everything on one texture instead.

## Files

| File | Role |
|------|------|
| `core.js` | Layout, built-in sprites, atlas packing, pixel rendering, slot snippet. Shared by CLI and GUI. |
| `exporters.js` | Code generators (Screen, Menu, registration, snippet). |
| `cli.mjs` | Headless front end: arguments, layout JSON, PNG encode/decode, preview. |
| `index.html` | Browser GUI. |

Both `.js` files are classic scripts in the browser and CommonJS in Node, so keep them free of
`import`/`export` and browser- or Node-only APIs.

## Adding an exporter

Append to `EXPORTERS` in `exporters.js`:

```js
{
	id: 'blockentity', label: 'Block entity',
	files: c => [{ title: `${c.name}BlockEntity.java`, path: `${c.javaPath(`leaf.cosmere.${c.module}.common.blocks.entities`)}/${c.name}BlockEntity.java`, content: '...' }],
}
```

It appears as a GUI tab and a CLI `--export` id automatically. `c` (from `context()`) carries the
options, the computed layout (`c.lay.slots`, each with `kind`, `index`, `size`, `role`;
`c.lay.widgets`, each with `constant`, `x`, `y`, `w`, `h`, `u`, `v`, `dir`), naming (`name`, `snake`,
`constant`, `modClass`, packages) and `containerSlots`. A file with `path: null` is treated as a
paste-in snippet.

Before trusting a new exporter, compile its output: the Screen/Menu exporters were checked with
`javac` against `build/moddev/artifacts/neoforge-*-merged.jar` plus small stubs for the module's
`<Mod>` and `<Mod>MenuTypes` classes, across all four fill directions and both blit overloads.

## Adding a built-in sprite

Add an entry to `BUILTIN_SPRITES` in `core.js`, either procedurally (`spriteFromFn(w, h, (x, y) => rgba)`,
with `segDist` for stroked shapes) or as a character map (`spriteFromMap(rows, palette)`), plus `kind`
(`'progress'` or `'icon'`), a default `dir` for progress sprites, a `label` for the GUI picker, and a
default `constant`. It shows up in the GUI picker and as a CLI `--widget` sprite automatically. Keep the
art original rather than tracing vanilla textures.

## What a working machine still needs

The exporters cover the texture, Screen, Menu (including progress sync plumbing) and menu/screen
registration. A real block also needs:

1. **Block entity** holding an `ItemStackHandler(CONTAINER_SLOTS)`, saving it in
   `saveAdditional`/`loadAdditional`, exposing `getItemHandler()` (the registration snippet's
   `useWithoutItem` calls it), and registered via the module's block entity registry.
2. **Progress data**: with widgets, the Menu takes a `ContainerData` of `DATA_SLOTS` ints: widget *i*
   reads progress at `2*i` and its maximum at `2*i+1`. The block entity implements that
   `ContainerData` over its own fields (see `SandSpreaderBE`) and returns it from
   `getContainerData()`; the registration snippet already passes it. For fuel-style widgets, pass the
   remaining burn time as progress.
3. **Item handler capability** for hoppers/pipes: `RegisterCapabilitiesEvent` →
   `event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, (be, side) -> ...)`. Usually a
   side-aware wrapper that blocks inserting into output slots.
4. **Lang key** for the title: `container.<module>.<name>` (as emitted by the registration snippet),
   added through the module's language provider in `src/datagen`.
5. **Block, block item, loot table, model** via the usual registries and datagen.

Other natural extensions: a block entity exporter, per-slot item filters (`mayPlace` overrides on input
slots), widget tooltips (hover regions showing e.g. "120 / 200"), armor/offhand slot columns, and
importing an existing texture to edit.
