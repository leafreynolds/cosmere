// Shared layout/render core for the inventory GUI generator (CLI and browser).
// Classic script in the browser (sets globalThis.InventoryGui), CommonJS in Node.
(function (root)
{
	'use strict';

	const COLORS = {
		K: [0, 0, 0, 255],        // outline
		W: [255, 255, 255, 255],  // highlight
		F: [198, 198, 198, 255],  // panel fill
		D: [85, 85, 85, 255],     // panel shadow
		d: [55, 55, 55, 255],     // slot shadow
		s: [139, 139, 139, 255],  // slot fill
	};
	const EMPTY_COLOR = COLORS.s; // unfilled progress widgets, as vanilla does

	const SLOT = 18;
	// Box size and item inset per slot size; large matches the vanilla furnace result slot.
	const SIZES = { normal: { box: 18, inset: 1 }, large: { box: 26, inset: 5 } };
	const PLAYER_COLS = 9;
	const PLAYER_GRID_W = PLAYER_COLS * SLOT;
	// Vanilla spacing, in pixels from the panel edge or between sections.
	const TOP_PAD = 17;       // panel top to first slot box (title sits at y=6)
	const SIDE_PAD = 7;       // panel side to slot box
	const BOTTOM_PAD = 7;     // last slot box to panel bottom
	const INV_GAP = 13;       // container grid to player inventory (label sits in here)
	const HOTBAR_GAP = 4;     // player inventory to hotbar
	const EDGE = 4;           // closest a placed slot may sit to the panel edge (3px bevel + 1)
	const DIRECTIONS = ['right', 'left', 'up', 'down'];

	// ---------- built-in sprites (original art; sizes match vanilla furnace widgets) ----------

	function spriteFromFn(w, h, fn)
	{
		const filled = new Uint8ClampedArray(w * h * 4);
		for (let y = 0; y < h; y++)
			for (let x = 0; x < w; x++)
			{
				const c = fn(x, y);
				if (c) filled.set(c, (y * w + x) * 4);
			}
		return { w, h, filled };
	}

	function spriteFromMap(rows, palette)
	{
		const w = rows[0].length;
		rows.forEach((r, i) => { if (r.length !== w) throw new Error(`sprite row ${i} is ${r.length} wide, expected ${w}`); });
		return spriteFromFn(w, rows.length, (x, y) => palette[rows[y][x]] || null);
	}

	// Silhouette in the empty colour, for sprites supplied without an empty variant.
	function silhouette(sprite)
	{
		const out = new Uint8ClampedArray(sprite.filled.length);
		for (let i = 0; i < out.length; i += 4)
			if (sprite.filled[i + 3] >= 128) out.set(EMPTY_COLOR, i);
		return out;
	}

	// Distance from point to segment, for stroked icon shapes.
	function segDist(px, py, ax, ay, bx, by)
	{
		const dx = bx - ax, dy = by - ay;
		const t = Math.max(0, Math.min(1, ((px - ax) * dx + (py - ay) * dy) / (dx * dx + dy * dy)));
		return Math.hypot(px - ax - t * dx, py - ay - t * dy);
	}

	// kind: 'progress' sprites default to a synced, cropped widget; 'icon' sprites default to static decoration.
	// Sizes follow the vanilla screens they echo; the art itself is original.
	const BUILTIN_SPRITES = {
		// 24x16, shaft plus triangular head; furnace/smoker progress, or static on crafting-style screens.
		arrow: Object.assign(spriteFromFn(24, 16, (x, y) =>
		{
			const shaft = x <= 14 && y >= 5 && y <= 10;
			const head = x >= 15 && Math.abs(y - 7.5) <= (23 - x) * 0.9 + 0.5;
			return shaft || head ? COLORS.W : null;
		}), { kind: 'progress', dir: 'right', label: 'Arrow', constant: 'PROGRESS' }),
		// 9x28 downward arrow, brewing-stand style.
		arrow_down: Object.assign(spriteFromFn(9, 28, (x, y) =>
		{
			const shaft = y <= 19 && x >= 2 && x <= 6;
			const head = y >= 20 && Math.abs(x - 4) <= (27 - y) * 0.6 + 0.5;
			return shaft || head ? COLORS.W : null;
		}), { kind: 'progress', dir: 'down', label: 'Down arrow', constant: 'BREW' }),
		// 12x29 rising bubbles, brewing-stand style.
		bubbles: Object.assign(spriteFromFn(12, 29, (x, y) =>
		{
			const rings = [[6, 25, 3], [3, 18.5, 2], [8.5, 13, 2.5], [4.5, 6.5, 2], [8.5, 2, 1.5]];
			const cx = x + 0.5, cy = y + 0.5;
			return rings.some(([rx, ry, r]) => Math.abs(Math.hypot(cx - rx, cy - ry) - r) < 0.7) ? [220, 240, 255, 255] : null;
		}), { kind: 'progress', dir: 'up', label: 'Bubbles', constant: 'BUBBLES' }),
		// 18x4 fuel bar, brewing-stand blaze-powder style.
		fuel_bar: Object.assign(spriteFromFn(18, 4, (x, y) =>
			[[255, 205, 80, 255], [240, 138, 36, 255], [222, 110, 24, 255], [168, 60, 12, 255]][y]),
		{ kind: 'progress', dir: 'right', label: 'Fuel bar', constant: 'FUEL' }),
		// 12x48 vertical gauge for energy/fluid-style readouts.
		gauge: Object.assign(spriteFromFn(12, 48, (x, y) =>
		{
			const shade = x <= 2 ? [255, 120, 110, 255] : x >= 9 ? [150, 28, 24, 255] : [214, 52, 44, 255];
			return y % 8 === 7 ? [120, 20, 18, 255] : shade;
		}), { kind: 'progress', dir: 'up', label: 'Gauge', constant: 'ENERGY' }),
		// 13x13 plus, smithing/combining style.
		plus: Object.assign(spriteFromFn(13, 13, (x, y) =>
			(x >= 5 && x <= 7) || (y >= 5 && y <= 7) ? COLORS.s : null),
		{ kind: 'icon', label: 'Plus', constant: 'PLUS' }),
		// 15x15 red cross, anvil "can't do that" style.
		cross: Object.assign(spriteFromFn(15, 15, (x, y) =>
			Math.min(segDist(x, y, 1, 1, 13, 13), segDist(x, y, 13, 1, 1, 13)) <= 1.2 ? [210, 40, 40, 255] : null),
		{ kind: 'icon', label: 'Cross', constant: 'CROSS' }),
		// 15x12 green check, beacon-confirm style.
		check: Object.assign(spriteFromFn(15, 12, (x, y) =>
			Math.min(segDist(x, y, 1.5, 6.5, 5.5, 10), segDist(x, y, 5.5, 10, 13.5, 1.5)) <= 1.2 ? [70, 190, 70, 255] : null),
		{ kind: 'icon', label: 'Check', constant: 'CHECK' }),
		// 14x14 flame; furnace fuel.
		flame: Object.assign(spriteFromMap([
			'......o.......',
			'.....oo.......',
			'.....oro......',
			'....orro......',
			'....orrro..o..',
			'...orryro.oro.',
			'...oryyrooro..',
			'..orryyyrorro.',
			'..oryyyyyrrro.',
			'.orryywyyyrro.',
			'.oryywwwyyyro.',
			'.oryywwwwyyro.',
			'.orrywwwwyrro.',
			'..oooooooooo..',
		], {
			o: [180, 56, 10, 255],
			r: [226, 90, 18, 255],
			y: [246, 162, 30, 255],
			w: [255, 232, 138, 255],
		}), { kind: 'progress', dir: 'up', label: 'Flame', constant: 'FUEL' }),
	};
	for (const s of Object.values(BUILTIN_SPRITES)) s.empty = silhouette(s);

	// sprites: custom { name: { w, h, filled, empty? } }, merged over built-ins.
	function resolveSprite(name, custom)
	{
		const s = (custom && custom[name]) || BUILTIN_SPRITES[name];
		if (!s) return null;
		if (!s.empty) s.empty = silhouette(s);
		return s;
	}

	// ---------- layout ----------

	const dims = s => s.kind === 'widget' ? { w: s.w, h: s.h } : { w: SIZES[s.size].box, h: SIZES[s.size].box };

	// opts: { slots, cols?, width?, height?, align: 'left'|'center' (last row),
	//         gridAlign: 'center'|'left', playerInv: bool, canvas,
	//         custom:  [{ x, y, size: 'normal'|'large', role: 'input'|'output' }]  (x/y = box top-left),
	//         widgets: [{ sprite, x, y, dir?, name?, mode?: 'progress'|'static', variant?: 'empty'|'filled' }],
	//         sprites: { name: { w, h, filled: RGBA, empty?: RGBA } } }
	// Throws Error with a user-facing message on invalid input.
	function layout(o)
	{
		const custom = o.custom || [];
		const widgetDefs = o.widgets || [];
		const cols = o.cols ?? Math.min(o.slots, 9);
		if (o.slots > 0 && cols === 0) throw new Error('Columns must be at least 1.');
		const rows = o.slots === 0 ? 0 : Math.ceil(o.slots / cols);
		const gridW = cols * SLOT;
		const gridH = rows * SLOT;

		const widgets = widgetDefs.map((w, i) =>
		{
			const sprite = resolveSprite(w.sprite, o.sprites);
			if (!sprite) throw new Error(`Widget #${i} uses unknown sprite "${w.sprite}".`);
			// Custom sprites default by shape: tall ones fill upward like gauges, wide ones rightward.
			const dir = w.dir || sprite.dir || (sprite.h > sprite.w ? 'up' : 'right');
			if (!DIRECTIONS.includes(dir)) throw new Error(`Widget #${i} has unknown direction "${dir}".`);
			// Static widgets are plain decoration: drawn on the panel, no atlas entry, data slot or code.
			const icon = sprite.kind === 'icon';
			const mode = w.mode || (icon ? 'static' : 'progress');
			if (mode !== 'progress' && mode !== 'static') throw new Error(`Widget #${i} has unknown mode "${mode}".`);
			const variant = w.variant || (icon ? 'filled' : 'empty');
			return { kind: 'widget', index: i, sprite: w.sprite, x: w.x, y: w.y, w: sprite.w, h: sprite.h, dir, name: w.name, mode, variant };
		});
		assignConstants(widgets.filter(w => w.mode === 'progress'));

		// Placed slots and widgets sit in the container area, so they push the player inventory down.
		let customRight = 0, customBottom = 0;
		for (const c of custom.map(c => ({ x: c.x, y: c.y, ...dims({ size: c.size || 'normal' }) })).concat(widgets))
		{
			customRight = Math.max(customRight, c.x + c.w + EDGE);
			customBottom = Math.max(customBottom, c.y + c.h);
		}

		const minW = Math.max(gridW + SIDE_PAD * 2, o.playerInv ? PLAYER_GRID_W + SIDE_PAD * 2 : 0, customRight, 32);
		const areaBottom = Math.max(TOP_PAD + gridH, customBottom);
		const playerH = o.playerInv ? INV_GAP + SLOT * 3 + HOTBAR_GAP + SLOT : 0;
		const minH = areaBottom + playerH + BOTTOM_PAD;

		const width = o.width ?? minW;
		const height = o.height ?? minH;
		const result = { cols, rows, minW, minH, width, height };
		const fail = msg => { throw Object.assign(new Error(msg), { layout: result }); };
		if (width < minW) fail(`Width ${width} is too small; this layout needs at least ${minW}.`);
		if (height < minH) fail(`Height ${height} is too small; this layout needs at least ${minH}.`);
		if (width > o.canvas || height > o.canvas)
			fail(`Panel ${width}x${height} exceeds the ${o.canvas}px canvas; use a larger canvas (and pass that texture size to blit).`);

		// Boxes are 18x18; the item sits 1px inside, which is what Slot x/y means.
		const slots = [];
		const gridLeft = o.gridAlign === 'left' ? SIDE_PAD : Math.floor((width - gridW) / 2);
		for (let i = 0; i < o.slots; i++)
		{
			const row = Math.floor(i / cols);
			const col = i % cols;
			let left = gridLeft;
			const inRow = Math.min(cols, o.slots - row * cols);
			if (o.align === 'center' && inRow < cols) left += Math.floor((cols - inRow) * SLOT / 2);
			slots.push({ x: left + col * SLOT, y: TOP_PAD + row * SLOT, size: 'normal', kind: 'container', index: i });
		}
		custom.forEach((c, i) =>
		{
			const size = c.size || 'normal';
			if (c.x < 0 || c.y < 0 || c.x + SIZES[size].box > width || c.y + SIZES[size].box > height)
				fail(`Placed slot #${o.slots + i} at ${c.x},${c.y} sticks out of the panel.`);
			slots.push({ x: c.x, y: c.y, size, role: c.role || 'input', kind: 'custom', index: o.slots + i, customIndex: i });
		});
		for (const w of widgets)
			if (w.x < 0 || w.y < 0 || w.x + w.w > width || w.y + w.h > height)
				fail(`Widget ${w.constant} at ${w.x},${w.y} sticks out of the panel.`);

		let player = null;
		if (o.playerInv)
		{
			// Anchored to the bottom like vanilla, so extra height lands above it.
			const left = Math.floor((width - PLAYER_GRID_W) / 2);
			const hotbarY = height - BOTTOM_PAD - SLOT;
			const invY = hotbarY - HOTBAR_GAP - SLOT * 3;
			player = { left, invY, hotbarY };
			for (let r = 0; r < 3; r++)
				for (let c = 0; c < PLAYER_COLS; c++)
					slots.push({ x: left + c * SLOT, y: invY + r * SLOT, size: 'normal', kind: 'player', index: c + r * 9 + 9 });
			for (let c = 0; c < PLAYER_COLS; c++)
				slots.push({ x: left + c * SLOT, y: hotbarY, size: 'normal', kind: 'hotbar', index: c });
		}

		const progress = widgets.filter(w => w.mode === 'progress');
		const atlas = packAtlas(progress, o.sprites, width, height, o.canvas, fail);
		for (const w of progress) Object.assign(w, atlas[w.sprite]);

		return Object.assign(result, {
			gridLeft, slots, player, widgets, progress, atlas,
			overlaps: findOverlaps(slots.concat(widgets)),
		});
	}

	// WIDGET_<NAME> constants; defaults come from the sprite, duplicates get a numeric suffix.
	function assignConstants(widgets)
	{
		const used = new Set();
		for (const w of widgets)
		{
			const sprite = BUILTIN_SPRITES[w.sprite];
			const base = ((w.name || (sprite ? sprite.constant : w.sprite)) + '')
				.toUpperCase().replace(/[^A-Z0-9]+/g, '_').replace(/^_+|_+$/g, '') || 'WIDGET';
			let name = base;
			for (let n = 2; used.has(name); n++) name = `${base}_${n}`;
			used.add(name);
			w.constant = name;
		}
	}

	// Places each distinct filled sprite in free canvas space: columns right of the panel, then rows below it.
	function packAtlas(widgets, customSprites, panelW, panelH, canvas, fail)
	{
		const names = [...new Set(widgets.map(w => w.sprite))];
		const out = {};
		let x = panelW, y = 0, colW = 0;          // region right of the panel
		let bx = 0, by = panelH, rowH = 0;        // region below the panel, left of panelW
		for (const name of names)
		{
			const s = resolveSprite(name, customSprites);
			if (y + s.h > canvas) { x += colW; y = 0; colW = 0; }
			if (x + s.w <= canvas && y + s.h <= canvas)
			{
				out[name] = { u: x, v: y };
				y += s.h;
				colW = Math.max(colW, s.w);
				continue;
			}
			if (bx + s.w > panelW) { bx = 0; by += rowH; rowH = 0; }
			if (bx + s.w <= panelW && by + s.h <= canvas)
			{
				out[name] = { u: bx, v: by };
				bx += s.w;
				rowH = Math.max(rowH, s.h);
				continue;
			}
			fail(`No free canvas space for the "${name}" sprite; use a larger canvas.`);
		}
		return out;
	}

	// Pairs of slots/widgets whose boxes intersect; legal to render but almost always a mistake.
	function findOverlaps(items)
	{
		const out = [];
		for (let i = 0; i < items.length; i++)
			for (let j = i + 1; j < items.length; j++)
			{
				const a = items[i], b = items[j];
				const da = dims(a), db = dims(b);
				if (a.x < b.x + db.w && b.x < a.x + da.w && a.y < b.y + db.h && b.y < a.y + da.h) out.push([a, b]);
			}
		return out;
	}

	// ---------- rendering ----------

	// Pixel-matched to vanilla generic_54.png; the shape is 180-degree rotationally symmetric.
	function panelPixel(x, y, w, h)
	{
		const rx = w - 1 - x, ry = h - 1 - y;
		if (x + y < 2 || rx + ry < 2 || rx + y < 3 || x + ry < 3) return null;
		if (x === 0 || y === 0 || rx === 0 || ry === 0 ||
			x + y === 2 || rx + ry === 2 || rx + y === 3 || x + ry === 3) return 'K';
		const light = x <= 2 || y <= 2 || (x === 3 && y === 3);
		const dark = rx <= 2 || ry <= 2 || (rx === 3 && ry === 3);
		if (light && dark) return 'F';
		if (light) return 'W';
		if (dark) return 'D';
		return 'F';
	}

	function slotPixel(x, y, box)
	{
		const last = box - 1;
		if ((x === last && y === 0) || (x === 0 && y === last)) return 's';
		if (x === 0 || y === 0) return 'd';
		if (x === last || y === last) return 'W';
		return 's';
	}

	// Returns RGBA bytes for a canvas x canvas image.
	function render(lay, canvas, customSprites)
	{
		const px = new Uint8ClampedArray(canvas * canvas * 4);
		const put = (x, y, key) => px.set(COLORS[key], (y * canvas + x) * 4);
		for (let y = 0; y < lay.height; y++)
			for (let x = 0; x < lay.width; x++)
			{
				const k = panelPixel(x, y, lay.width, lay.height);
				if (k) put(x, y, k);
			}
		for (const s of lay.slots)
		{
			const box = SIZES[s.size].box;
			for (let y = 0; y < box; y++)
				for (let x = 0; x < box; x++)
					put(s.x + x, s.y + y, slotPixel(x, y, box));
		}
		for (const w of lay.widgets || [])
		{
			const s = resolveSprite(w.sprite, customSprites);
			blend(px, canvas, w.mode === 'static' && w.variant === 'filled' ? s.filled : s.empty, s.w, s.h, w.x, w.y);
		}
		for (const [name, pos] of Object.entries(lay.atlas || {}))
		{
			const s = resolveSprite(name, customSprites);
			copy(px, canvas, s.filled, s.w, s.h, pos.u, pos.v);
		}
		return px;
	}

	// Source-over composite, for drawing onto the panel.
	function blend(dst, canvas, src, w, h, dx, dy)
	{
		for (let y = 0; y < h; y++)
			for (let x = 0; x < w; x++)
			{
				const si = (y * w + x) * 4, a = src[si + 3] / 255;
				if (!a) continue;
				const di = ((dy + y) * canvas + dx + x) * 4;
				for (let c = 0; c < 3; c++) dst[di + c] = src[si + c] * a + dst[di + c] * (1 - a);
				dst[di + 3] = Math.max(dst[di + 3], src[si + 3]);
			}
	}

	// Raw copy including alpha, for atlas regions that blit reads directly.
	function copy(dst, canvas, src, w, h, dx, dy)
	{
		for (let y = 0; y < h; y++)
			dst.set(src.subarray(y * w * 4, (y + 1) * w * 4), ((dy + y) * canvas + dx) * 4);
	}

	// Box top-left range an item of the given size can occupy without growing the panel or hitting the player inventory.
	// size: 'normal' | 'large' | { w, h }
	function placementBounds(lay, size)
	{
		const d = typeof size === 'string' ? dims({ size }) : size;
		const bottom = lay.player ? lay.player.invY - INV_GAP : lay.height - BOTTOM_PAD;
		return { minX: EDGE, minY: EDGE, maxX: lay.width - EDGE - d.w, maxY: bottom - d.h };
	}

	function javaSnippet(o, lay)
	{
		const lines = [
			'// Screen',
			`this.imageWidth = ${lay.width};`,
			`this.imageHeight = ${lay.height};`,
		];
		if (lay.player)
			lines.push(`this.inventoryLabelX = ${lay.player.left + 1};`,
				`this.inventoryLabelY = ${lay.player.invY - 11};`);
		lines.push('', '// Menu (Slot x/y is the item position, 1px inside the box)');
		if (o.slots > 0)
		{
			const uniform = o.slots % lay.cols === 0 || o.align === 'left';
			if (uniform)
				lines.push(`for (int i = 0; i < ${o.slots}; i++)`,
					`\taddSlot(new Slot(container, i, ${lay.gridLeft + 1} + (i % ${lay.cols}) * 18, ${TOP_PAD + 1} + (i / ${lay.cols}) * 18));`);
			else
				lay.slots.slice(0, o.slots).forEach((s, i) =>
					lines.push(`addSlot(new Slot(container, ${i}, ${s.x + 1}, ${s.y + 1}));`));
		}
		for (const s of lay.slots.filter(s => s.kind === 'custom'))
		{
			const inset = SIZES[s.size].inset;
			const note = s.size === 'large' ? ' // large (e.g. result) slot' : '';
			lines.push(`addSlot(new Slot(container, ${s.index}, ${s.x + inset}, ${s.y + inset}));${note}`);
		}
		if (lay.player)
		{
			const lx = lay.player.left + 1;
			lines.push(`for (int row = 0; row < 3; row++)`,
				`\tfor (int col = 0; col < 9; col++)`,
				`\t\taddSlot(new Slot(playerInv, col + row * 9 + 9, ${lx} + col * 18, ${lay.player.invY + 1} + row * 18));`,
				`for (int col = 0; col < 9; col++)`,
				`\taddSlot(new Slot(playerInv, col, ${lx} + col * 18, ${lay.player.hotbarY + 1}));`);
		}
		if (lay.progress.length)
		{
			lines.push('', '// Progress widgets: draw at (x, y) from texture (u, v), cropped by progress (see Screen export)');
			for (const w of lay.progress)
				lines.push(`// ${w.constant}: at ${w.x},${w.y}  uv ${w.u},${w.v}  size ${w.w}x${w.h}  fills ${w.dir}`);
		}
		return lines.join('\n');
	}

	const api = {
		SLOT, SIZES, DIRECTIONS, BUILTIN_SPRITES,
		layout, render, javaSnippet, placementBounds, resolveSprite, silhouette,
	};
	if (typeof module !== 'undefined' && module.exports) module.exports = api;
	else root.InventoryGui = api;
})(typeof globalThis !== 'undefined' ? globalThis : this);
