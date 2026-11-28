# Structure NBT

Generates game test structure templates (`.nbt`) without opening a client. It makes a box of a given
size with a floor layer, a fill block above it, and any extra blocks you place. It can also dump any
`.nbt` file as JSON for checking. Python 3.8+ standard library only, no packages to install.

## Examples

```bash
# 7x4x7 stone floor with air above, the template the cosmere game tests use
python tools/structure-nbt/structure_nbt.py --size 7,4,7 \
  --out src/gameTest/main/resources/data/cosmere/structure/floor_7x4x7.nbt

# grass floor with a metalworking table and a stair placed in it
python tools/structure-nbt/structure_nbt.py --size 5,3,5 --floor minecraft:grass_block \
  --block 2,1,2,cosmere:metalworking_table \
  --block "0,1,0,minecraft:oak_stairs[facing=north,half=bottom]" \
  --out build/tmp/table_room.nbt

# inspect a template, including ones saved in game with a structure block
python tools/structure-nbt/structure_nbt.py --dump build/tmp/table_room.nbt
```

## Spec files

Anything beyond a few blocks reads better as JSON. Flags override the spec's values, and `--block`
entries are added after the spec's own list.

```json
{
  "size": [3, 2, 3],
  "floor": "minecraft:smooth_stone",
  "fill": "none",
  "blocks": [ { "pos": [1, 1, 1], "state": "cosmere:metalworking_table" } ]
}
```

```bash
python tools/structure-nbt/structure_nbt.py --spec table.json --out build/tmp/table.nbt
```

## Notes

- Positions are template coordinates, so y=0 is the floor. In a game test the floor is at relative
  y=1, because relative y=0 is the structure block under the template.
- `"none"` for floor or fill leaves those cells out (structure void), so whatever is already in the
  world stays there.
- Block ids without a namespace get `minecraft:`. Properties are sorted, so the same state written in
  two orders shares one palette entry.
- `DataVersion` defaults to 3955 (1.21.1). Pass `--data-version` when targeting another version.
- Output matches what the game writes, including `0` as the element type of an empty list.
- gzip is written with mtime 0, so regenerating an unchanged template gives identical bytes.
