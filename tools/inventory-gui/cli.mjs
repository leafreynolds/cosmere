#!/usr/bin/env node
// Headless CLI for the inventory GUI generator. The browser version is index.html in this folder;
// both read and write the same layout JSON (--config / --save-config, "Save/Load layout" in the GUI).

import fs from 'node:fs';
import path from 'node:path';
import zlib from 'node:zlib';
import { createRequire } from 'node:module';

const require = createRequire(import.meta.url);
const { layout, render, javaSnippet, resolveSprite, BUILTIN_SPRITES, SIZES } = require('./core.js');
const { EXPORTERS, context } = require('./exporters.js');

const USAGE = `Usage: node tools/inventory-gui/cli.mjs [--config layout.json] [options]

Layout:
  --slots <n>        number of grid slots (default 0 with --config, else required)
  --cols <n>         slots per row (default: min(slots, 9))
  --width <px>       panel width  (default: smallest that fits; 176 with player inventory)
  --height <px>      panel height (default: smallest that fits); extra height goes
                     between the container area and the player inventory
  --align <mode>     last partial row: left | center (default: left)
  --grid-align <m>   grid position: center | left (default: center)
  --slot <x,y[,large][,output]>
                     individually placed slot (box top-left); repeatable.
                     Indexes continue after the grid.
  --widget <sprite,x,y[,dir][,progress|static|static-filled][,name=NAME]>
                     sprite widget; repeatable. Built-in sprites:
                       progress: ${Object.keys(BUILTIN_SPRITES).filter(k => BUILTIN_SPRITES[k].kind === 'progress').join(', ')}
                       icons:    ${Object.keys(BUILTIN_SPRITES).filter(k => BUILTIN_SPRITES[k].kind === 'icon').join(', ')}
                     or a --sprite name. Progress widgets sync a value and crop by it;
                     static ones are plain decoration (grey "empty" look, or static-filled).
                     Icons default to static-filled, the rest to progress;
                     dir is right | left | up | down (default per sprite); repeatable
  --sprite <name=filled.png[,empty.png]>
                     custom widget sprite; empty defaults to a grey silhouette; repeatable
  --no-player-inv    omit the player inventory and hotbar
  --canvas <px>      PNG size, square (default: 256, the size GuiGraphics.blit assumes)

Output:
  --out <file>       texture path (default: inventory.png)
  --preview <file>   also write an enlarged preview of the panel with widgets part-filled
  --zoom <n>         preview scale (default: 3)
  --fill <0-100>     preview widget fill percent (default: 60)
  --json             print the computed layout as JSON instead of the Java snippet
  --no-java          don't print the Java snippet
  --config <file>    read a layout JSON (as saved by the GUI); flags override it
  --save-config <f>  write the effective layout as JSON (loadable in the GUI)

Code export:
  --export <ids>     print generated code; comma-separated: ${EXPORTERS.map(e => e.id).join(', ')}
  --name <Name>      class name stem, e.g. Crusher -> CrusherMenu/CrusherScreen (default: Example)
  --module <id>      module source set, e.g. sandmastery (default: example)
  --mod-class <C>    mod main class (default: capitalized module)
  --write            write exported files to their repo paths instead of printing
  --force            let --write overwrite existing files`;

function fail(msg)
{
	console.error(`error: ${msg}\n\nRun with --help for usage.`);
	process.exit(1);
}

// ---------- args ----------

function parseArgs(argv)
{
	const a = { custom: [], widgets: [], sprites: {}, names: {} };
	for (let i = 0; i < argv.length; i++)
	{
		const flag = argv[i];
		const next = () =>
		{
			if (i + 1 >= argv.length) fail(`missing value for ${flag}`);
			return argv[++i];
		};
		const int = () =>
		{
			const v = next();
			if (!/^\d+$/.test(v)) fail(`${flag} expects a non-negative integer, got "${v}"`);
			return parseInt(v, 10);
		};
		switch (flag)
		{
			case '--slots': a.slots = int(); break;
			case '--cols': a.cols = int(); break;
			case '--width': a.width = int(); break;
			case '--height': a.height = int(); break;
			case '--canvas': a.canvas = int(); break;
			case '--align': a.align = next(); break;
			case '--grid-align': a.gridAlign = next(); break;
			case '--no-player-inv': a.playerInv = false; break;
			case '--slot':
			{
				const parts = next().split(',');
				const [x, y] = parts.map(Number);
				if (!Number.isInteger(x) || !Number.isInteger(y)) fail('--slot expects x,y[,large][,output]');
				a.custom.push({ x, y, size: parts.includes('large') ? 'large' : 'normal', role: parts.includes('output') ? 'output' : 'input' });
				break;
			}
			case '--widget':
			{
				const parts = next().split(',');
				const [sprite, xs, ys] = parts;
				const x = Number(xs), y = Number(ys);
				if (!sprite || !Number.isInteger(x) || !Number.isInteger(y)) fail('--widget expects sprite,x,y[,dir][,name=NAME]');
				const w = { sprite, x, y };
				for (const p of parts.slice(3))
					if (p.startsWith('name=')) w.name = p.slice(5);
					else if (p === 'progress') w.mode = 'progress';
					else if (p === 'static' || p === 'static-empty') { w.mode = 'static'; w.variant = 'empty'; }
					else if (p === 'static-filled') { w.mode = 'static'; w.variant = 'filled'; }
					else w.dir = p;
				a.widgets.push(w);
				break;
			}
			case '--sprite':
			{
				const m = /^([^=]+)=([^,]+)(?:,(.+))?$/.exec(next());
				if (!m) fail('--sprite expects name=filled.png[,empty.png]');
				a.sprites[m[1]] = { filled: m[2], empty: m[3] };
				break;
			}
			case '--out': a.out = next(); break;
			case '--preview': a.preview = next(); break;
			case '--zoom': a.zoom = int(); break;
			case '--fill': a.fill = int(); break;
			case '--json': a.json = true; break;
			case '--no-java': a.java = false; break;
			case '--config': a.config = next(); break;
			case '--save-config': a.saveConfig = next(); break;
			case '--export': a.export = next().split(','); break;
			case '--name': a.names.name = next(); break;
			case '--module': a.names.module = next(); break;
			case '--mod-class': a.names.modClass = next(); break;
			case '--write': a.write = true; break;
			case '--force': a.force = true; break;
			case '-h': case '--help': console.log(USAGE); process.exit(0);
			default: fail(`unknown option ${flag}`);
		}
	}
	return a;
}

// Defaults < config file < flags. List options append to the config's lists.
function mergeOptions(a)
{
	let cfg = {}, cfgDir = process.cwd();
	if (a.config)
	{
		try { cfg = JSON.parse(fs.readFileSync(a.config, 'utf8')); }
		catch (e) { fail(`can't read --config ${a.config}: ${e.message}`); }
		cfgDir = path.dirname(path.resolve(a.config));
	}
	else if (a.slots === undefined && !a.custom.length && !a.widgets.length) fail('--slots is required (or pass --config)');

	const pick = (k, d) => a[k] ?? cfg[k] ?? d;
	const o = {
		slots: pick('slots', 0),
		cols: pick('cols', undefined),
		width: pick('width', undefined),
		height: pick('height', undefined),
		align: pick('align', 'left'),
		gridAlign: pick('gridAlign', 'center'),
		playerInv: pick('playerInv', true),
		canvas: pick('canvas', 256),
		custom: (cfg.custom || []).concat(a.custom),
		widgets: (cfg.widgets || []).concat(a.widgets),
		names: { ...(cfg.names || {}), ...a.names },
		spriteSources: {},
	};
	for (const k of ['cols', 'width', 'height']) if (o[k] === null) o[k] = undefined;
	for (const [name, src] of Object.entries(cfg.sprites || {})) o.spriteSources[name] = { ...src, base: cfgDir };
	for (const [name, src] of Object.entries(a.sprites)) o.spriteSources[name] = { ...src, base: process.cwd() };
	if (!['left', 'center'].includes(o.align)) fail('--align must be left or center');
	if (!['left', 'center'].includes(o.gridAlign)) fail('--grid-align must be left or center');
	for (const id of a.export || [])
		if (!EXPORTERS.some(e => e.id === id)) fail(`unknown exporter "${id}"`);
	return o;
}

// ---------- PNG ----------

const CRC_TABLE = (() =>
{
	const t = new Uint32Array(256);
	for (let n = 0; n < 256; n++)
	{
		let c = n;
		for (let k = 0; k < 8; k++) c = c & 1 ? 0xEDB88320 ^ (c >>> 1) : c >>> 1;
		t[n] = c >>> 0;
	}
	return t;
})();

function crc32(buf)
{
	let c = 0xFFFFFFFF;
	for (const b of buf) c = CRC_TABLE[(c ^ b) & 0xFF] ^ (c >>> 8);
	return (c ^ 0xFFFFFFFF) >>> 0;
}

function chunk(type, data)
{
	const len = Buffer.alloc(4);
	len.writeUInt32BE(data.length);
	const body = Buffer.concat([Buffer.from(type, 'ascii'), data]);
	const crc = Buffer.alloc(4);
	crc.writeUInt32BE(crc32(body));
	return Buffer.concat([len, body, crc]);
}

function encodePng(rgba, w, h)
{
	const ihdr = Buffer.alloc(13);
	ihdr.writeUInt32BE(w, 0);
	ihdr.writeUInt32BE(h, 4);
	ihdr[8] = 8; ihdr[9] = 6; // 8-bit RGBA
	const src = Buffer.from(rgba.buffer, rgba.byteOffset, rgba.byteLength);
	const raw = Buffer.alloc(h * (w * 4 + 1));
	for (let y = 0; y < h; y++) src.copy(raw, y * (w * 4 + 1) + 1, y * w * 4, (y + 1) * w * 4);
	return Buffer.concat([
		Buffer.from([0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A]),
		chunk('IHDR', ihdr),
		chunk('IDAT', zlib.deflateSync(raw, { level: 9 })),
		chunk('IEND', Buffer.alloc(0)),
	]);
}

// Non-interlaced PNG of any colour type at bit depth 8 (or 1/2/4 for greyscale/palette).
function decodePng(buf, label)
{
	if (buf.readUInt32BE(0) !== 0x89504E47) fail(`${label} is not a PNG`);
	let p = 8, ihdr, plte, trns;
	const idat = [];
	while (p < buf.length)
	{
		const len = buf.readUInt32BE(p), type = buf.toString('ascii', p + 4, p + 8), d = buf.subarray(p + 8, p + 8 + len);
		if (type === 'IHDR') ihdr = d;
		else if (type === 'PLTE') plte = d;
		else if (type === 'tRNS') trns = d;
		else if (type === 'IDAT') idat.push(d);
		p += 12 + len;
	}
	const w = ihdr.readUInt32BE(0), h = ihdr.readUInt32BE(4), depth = ihdr[8], ct = ihdr[9];
	if (ihdr[12] !== 0) fail(`${label}: interlaced PNGs aren't supported`);
	const ch = { 0: 1, 2: 3, 3: 1, 4: 2, 6: 4 }[ct];
	if (!ch || (depth !== 8 && !(depth < 8 && (ct === 0 || ct === 3)))) fail(`${label}: unsupported PNG format (type ${ct}, depth ${depth})`);
	const bpp = Math.max(1, ch * depth / 8), stride = Math.ceil(w * ch * depth / 8);
	const raw = zlib.inflateSync(Buffer.concat(idat));
	const lines = Buffer.alloc(h * stride);
	let prev = Buffer.alloc(stride);
	for (let y = 0; y < h; y++)
	{
		const f = raw[y * (stride + 1)];
		const line = Buffer.from(raw.subarray(y * (stride + 1) + 1, (y + 1) * (stride + 1)));
		for (let i = 0; i < stride; i++)
		{
			const left = i >= bpp ? line[i - bpp] : 0, up = prev[i], ul = i >= bpp ? prev[i - bpp] : 0;
			let v = line[i];
			if (f === 1) v += left;
			else if (f === 2) v += up;
			else if (f === 3) v += (left + up) >> 1;
			else if (f === 4)
			{
				const pp = left + up - ul, pa = Math.abs(pp - left), pb = Math.abs(pp - up), pc = Math.abs(pp - ul);
				v += pa <= pb && pa <= pc ? left : pb <= pc ? up : ul;
			}
			line[i] = v & 255;
		}
		line.copy(lines, y * stride);
		prev = line;
	}
	const out = new Uint8ClampedArray(w * h * 4);
	for (let y = 0; y < h; y++)
		for (let x = 0; x < w; x++)
		{
			const o = (y * w + x) * 4, row = y * stride;
			let r, g, b, al = 255;
			if (depth < 8)
			{
				const per = 8 / depth, byte = lines[row + Math.floor(x / per)];
				const idx = (byte >> (8 - depth * (x % per + 1))) & ((1 << depth) - 1);
				if (ct === 3) { r = plte[idx * 3]; g = plte[idx * 3 + 1]; b = plte[idx * 3 + 2]; al = trns && idx < trns.length ? trns[idx] : 255; }
				else { r = g = b = Math.round(idx * 255 / ((1 << depth) - 1)); }
			}
			else if (ct === 3)
			{
				const idx = lines[row + x];
				r = plte[idx * 3]; g = plte[idx * 3 + 1]; b = plte[idx * 3 + 2];
				al = trns && idx < trns.length ? trns[idx] : 255;
			}
			else
			{
				const s = row + x * ch;
				if (ct === 0) r = g = b = lines[s];
				else if (ct === 4) { r = g = b = lines[s]; al = lines[s + 1]; }
				else { r = lines[s]; g = lines[s + 1]; b = lines[s + 2]; if (ct === 6) al = lines[s + 3]; }
			}
			out[o] = r; out[o + 1] = g; out[o + 2] = b; out[o + 3] = al;
		}
	return { w, h, data: out };
}

// Sprite sources are file paths (relative to their config or cwd) or data: URLs (as the GUI saves them).
function loadImage(src, base, label)
{
	if (src.startsWith('data:'))
		return decodePng(Buffer.from(src.slice(src.indexOf(',') + 1), 'base64'), label);
	const file = path.resolve(base, src);
	if (!fs.existsSync(file)) fail(`${label}: ${file} not found`);
	return decodePng(fs.readFileSync(file), label);
}

function loadSprites(sources)
{
	const out = {};
	for (const [name, src] of Object.entries(sources))
	{
		const filled = loadImage(src.filled, src.base, `sprite "${name}"`);
		const sprite = { w: filled.w, h: filled.h, filled: filled.data };
		if (src.empty)
		{
			const empty = loadImage(src.empty, src.base, `sprite "${name}" (empty)`);
			if (empty.w !== filled.w || empty.h !== filled.h) fail(`sprite "${name}": empty image must match the filled size ${filled.w}x${filled.h}`);
			sprite.empty = empty.data;
		}
		out[name] = sprite;
	}
	return out;
}

// ---------- preview ----------

function preview(lay, pixels, canvas, sprites, zoom, fill)
{
	const w = lay.width, h = lay.height;
	const img = new Uint8ClampedArray(w * h * 4);
	for (let y = 0; y < h; y++) img.set(pixels.subarray((y * canvas) * 4, (y * canvas + w) * 4), y * w * 4);
	for (const wd of lay.progress)
	{
		const s = resolveSprite(wd.sprite, sprites);
		const horiz = wd.dir === 'right' || wd.dir === 'left';
		const n = Math.round((horiz ? s.w : s.h) * fill / 100);
		for (let y = 0; y < s.h; y++)
			for (let x = 0; x < s.w; x++)
			{
				const shown = wd.dir === 'right' ? x < n : wd.dir === 'left' ? x >= s.w - n : wd.dir === 'up' ? y >= s.h - n : y < n;
				const si = (y * s.w + x) * 4, a = s.filled[si + 3] / 255;
				if (!shown || !a) continue;
				const di = ((wd.y + y) * w + wd.x + x) * 4;
				for (let c = 0; c < 3; c++) img[di + c] = s.filled[si + c] * a + img[di + c] * (1 - a);
			}
	}
	const out = new Uint8ClampedArray(w * zoom * h * zoom * 4);
	for (let y = 0; y < h * zoom; y++)
		for (let x = 0; x < w * zoom; x++)
			out.set(img.subarray(((Math.floor(y / zoom) * w) + Math.floor(x / zoom)) * 4, ((Math.floor(y / zoom) * w) + Math.floor(x / zoom)) * 4 + 4), (y * w * zoom + x) * 4);
	return { w: w * zoom, h: h * zoom, data: out };
}

function layoutJson(o, lay)
{
	const slot = s => ({ kind: s.kind, index: s.index, size: s.size, role: s.role, box: { x: s.x, y: s.y }, item: { x: s.x + SIZES[s.size].inset, y: s.y + SIZES[s.size].inset } });
	return {
		panel: { width: lay.width, height: lay.height, minWidth: lay.minW, minHeight: lay.minH, canvas: o.canvas },
		labels: { title: { x: 8, y: 6 }, inventory: lay.player ? { x: lay.player.left + 1, y: lay.player.invY - 11 } : null },
		slots: lay.slots.map(slot),
		widgets: lay.widgets.map(w => ({ constant: w.constant ?? null, sprite: w.sprite, mode: w.mode, variant: w.mode === "static" ? w.variant : undefined, dir: w.mode === "progress" ? w.dir : undefined, x: w.x, y: w.y, w: w.w, h: w.h, u: w.u, v: w.v })),
		overlaps: lay.overlaps.map(([a, b]) => [`${a.kind} #${a.index}`, `${b.kind} #${b.index}`]),
	};
}

// ---------- main ----------

const args = parseArgs(process.argv.slice(2));
const opts = mergeOptions(args);
opts.sprites = loadSprites(opts.spriteSources);

let lay;
try { lay = layout(opts); }
catch (e) { fail(e.message); }

for (const [a, b] of lay.overlaps) console.warn(`warning: ${a.kind} #${a.index} overlaps ${b.kind} #${b.index}`);

const pixels = render(lay, opts.canvas, opts.sprites);
const out = path.resolve(args.out ?? 'inventory.png');
fs.mkdirSync(path.dirname(out), { recursive: true });
fs.writeFileSync(out, encodePng(pixels, opts.canvas, opts.canvas));
const log = args.json ? console.error : console.log;
log(`wrote ${out}  (panel ${lay.width}x${lay.height}, ${opts.slots} grid + ${opts.custom.length} placed slots, ${lay.widgets.length} widgets, canvas ${opts.canvas})`);

if (args.preview)
{
	const p = preview(lay, pixels, opts.canvas, opts.sprites, args.zoom ?? 3, Math.max(0, Math.min(100, args.fill ?? 60)));
	const file = path.resolve(args.preview);
	fs.mkdirSync(path.dirname(file), { recursive: true });
	fs.writeFileSync(file, encodePng(p.data, p.w, p.h));
	log(`wrote ${file}  (preview ${p.w}x${p.h})`);
}

if (args.saveConfig)
{
	const sprites = {};
	for (const [name, src] of Object.entries(opts.spriteSources))
	{
		const toUrl = s => s && (s.startsWith('data:') ? s : 'data:image/png;base64,' + fs.readFileSync(path.resolve(src.base, s)).toString('base64'));
		sprites[name] = { filled: toUrl(src.filled), ...(src.empty ? { empty: toUrl(src.empty) } : {}) };
	}
	const cfg = {
		version: 1,
		slots: opts.slots, cols: opts.cols ?? null, width: opts.width ?? null, height: opts.height ?? null,
		align: opts.align, gridAlign: opts.gridAlign, playerInv: opts.playerInv, canvas: opts.canvas,
		custom: opts.custom, widgets: opts.widgets, sprites, names: opts.names,
	};
	fs.writeFileSync(path.resolve(args.saveConfig), JSON.stringify(cfg, null, '\t') + '\n');
	log(`wrote ${path.resolve(args.saveConfig)}  (layout config)`);
}

if (args.json) console.log(JSON.stringify(layoutJson(opts, lay), null, 2));

if (args.export)
{
	const c = context(opts, lay, opts.names);
	for (const id of args.export)
		for (const f of EXPORTERS.find(e => e.id === id).files(c))
		{
			if (args.write && f.path)
			{
				const dest = path.resolve(f.path);
				if (fs.existsSync(dest) && !args.force) { log(`skipped ${f.path} (exists; --force to overwrite)`); continue; }
				fs.mkdirSync(path.dirname(dest), { recursive: true });
				fs.writeFileSync(dest, f.content);
				log(`wrote ${f.path}`);
			}
			else log(`\n===== ${f.title}${f.path ? ` -> ${f.path}` : ''} =====\n${f.content}`);
		}
}
else if (!args.json && args.java !== false) console.log('\n' + javaSnippet(opts, lay));
