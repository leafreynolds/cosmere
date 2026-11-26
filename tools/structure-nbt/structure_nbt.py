#!/usr/bin/env python3
# game test templates are tiny and regular, so generating them beats saving them from a dev client

import argparse
import gzip
import json
import re
import struct
import sys
from pathlib import Path

DEFAULT_DATA_VERSION = 3955

EPILOG = """A state is a block id with optional properties, e.g.
  minecraft:oak_stairs[facing=north,half=bottom]"""

END, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BYTE_ARRAY, STRING, LIST, COMPOUND, INT_ARRAY, LONG_ARRAY = range(13)


def fail(msg):
	print(f"error: {msg}\n\nRun with --help for usage.", file=sys.stderr)
	sys.exit(1)


# ---------- nbt ----------

def enc_string(s):
	# java reads modified utf-8 but block ids and property values are plain ascii
	b = s.encode("utf-8")
	return struct.pack(">H", len(b)) + b


def enc_int(v):
	return struct.pack(">i", v)


def enc_list(elem_type, payloads):
	# vanilla writes END as the element type of an empty list
	return bytes([elem_type if payloads else END]) + enc_int(len(payloads)) + b"".join(payloads)


def enc_compound(fields):
	# fields are (type, name, payload) so field order in the file stays stable
	return b"".join(bytes([t]) + enc_string(name) + payload for t, name, payload in fields) + bytes([END])


def enc_root(payload):
	return bytes([COMPOUND]) + enc_string("") + payload


# reader covers every tag type so --dump also works on templates saved by the game
def decode_root(buf):
	p = 0

	def take(fmt):
		nonlocal p
		v = struct.unpack_from(fmt, buf, p)[0]
		p += struct.calcsize(fmt)
		return v

	def string():
		nonlocal p
		n = take(">H")
		s = buf[p:p + n].decode("utf-8")
		p += n
		return s

	def read(t):
		if t == BYTE: return take(">b")
		if t == SHORT: return take(">h")
		if t == INT: return take(">i")
		if t == LONG: return str(take(">q"))
		if t == FLOAT: return take(">f")
		if t == DOUBLE: return take(">d")
		if t == BYTE_ARRAY: return [take(">b") for _ in range(take(">i"))]
		if t == STRING: return string()
		if t == LIST:
			et = take(">B")
			return [read(et) for _ in range(take(">i"))]
		if t == COMPOUND:
			out = {}
			while (ct := take(">B")) != END:
				name = string()
				out[name] = read(ct)
			return out
		if t == INT_ARRAY: return [take(">i") for _ in range(take(">i"))]
		if t == LONG_ARRAY: return [str(take(">q")) for _ in range(take(">i"))]
		raise ValueError(f"unknown tag type {t} at byte {p - 1}")

	if take(">B") != COMPOUND:
		raise ValueError("root tag is not a compound")
	string()
	return read(COMPOUND)


# ---------- template ----------

STATE_RE = re.compile(r"^([a-z0-9_.-]+:[a-z0-9_./-]+|[a-z0-9_./-]+)(?:\[(.*)\])?$")


def parse_state(text):
	m = STATE_RE.match(text)
	if not m:
		fail(f'bad block state "{text}"')
	name = m.group(1) if ":" in m.group(1) else f"minecraft:{m.group(1)}"
	props = []
	if m.group(2):
		for kv in m.group(2).split(","):
			k, sep, v = kv.partition("=")
			if not k.strip() or not sep:
				fail(f'bad property "{kv}" in "{text}"')
			props.append((k.strip(), v.strip()))
	# sorted so one state written two ways shares a palette entry
	props.sort()
	key = name + ("[" + ",".join(f"{k}={v}" for k, v in props) + "]" if props else "")
	return name, props, key


def build_template(size, floor, fill, blocks, data_version):
	sx, sy, sz = size
	if not (sx > 0 and sy > 0 and sz > 0):
		fail("--size must be positive in every axis")
	grid = {}
	for x in range(sx):
		for y in range(sy):
			for z in range(sz):
				s = floor if y == 0 else fill
				if s != "none":
					grid[(x, y, z)] = s
	for b in blocks:
		x, y, z = b["pos"]
		if not (0 <= x < sx and 0 <= y < sy and 0 <= z < sz):
			fail(f"block {x},{y},{z} is outside the {sx},{sy},{sz} template")
		grid[(x, y, z)] = b["state"]

	palette, index, block_tags = [], {}, []
	# x then y then z order matches the block order of the checked in floor_7x4x7
	for x in range(sx):
		for y in range(sy):
			for z in range(sz):
				raw = grid.get((x, y, z))
				if raw is None:
					continue
				name, props, key = parse_state(raw)
				if key not in index:
					index[key] = len(palette)
					fields = [(STRING, "Name", enc_string(name))]
					if props:
						fields.append((COMPOUND, "Properties", enc_compound([(STRING, k, enc_string(v)) for k, v in props])))
					palette.append(enc_compound(fields))
				block_tags.append(enc_compound([
					(LIST, "pos", enc_list(INT, [enc_int(x), enc_int(y), enc_int(z)])),
					(INT, "state", enc_int(index[key])),
				]))

	return enc_root(enc_compound([
		(INT, "DataVersion", enc_int(data_version)),
		(LIST, "size", enc_list(INT, [enc_int(v) for v in size])),
		(LIST, "palette", enc_list(COMPOUND, palette)),
		(LIST, "blocks", enc_list(COMPOUND, block_tags)),
		(LIST, "entities", enc_list(COMPOUND, [])),
	]))


# ---------- main ----------

def triple(text):
	try:
		parts = [int(v) for v in text.split(",")]
	except ValueError:
		parts = []
	if len(parts) != 3:
		raise argparse.ArgumentTypeError(f'expects x,y,z integers, got "{text}"')
	return parts


def block(text):
	# split only the first three commas since state properties contain commas too
	m = re.match(r"^(-?\d+),(-?\d+),(-?\d+),(.+)$", text)
	if not m:
		raise argparse.ArgumentTypeError("expects x,y,z,state")
	return {"pos": [int(m.group(1)), int(m.group(2)), int(m.group(3))], "state": m.group(4)}


def main():
	ap = argparse.ArgumentParser(
		prog="python tools/structure-nbt/structure_nbt.py",
		description="Generate game test structure templates (.nbt), or dump one as JSON.",
		epilog=EPILOG, formatter_class=argparse.RawDescriptionHelpFormatter)
	ap.add_argument("--size", type=triple, metavar="X,Y,Z", help="template size in blocks (required unless set by --spec)")
	ap.add_argument("--floor", metavar="STATE", help='block for the y=0 layer, or "none" (default: minecraft:stone)')
	ap.add_argument("--fill", metavar="STATE", help='block above the floor, or "none" for structure void (default: minecraft:air)')
	ap.add_argument("--block", type=block, action="append", default=[], metavar="X,Y,Z,STATE", help="place one block over floor/fill, repeatable")
	ap.add_argument("--spec", metavar="FILE", help="JSON {size, floor, fill, blocks:[{pos, state}], dataVersion}, flags override it and --block adds to its list")
	ap.add_argument("--data-version", type=int, metavar="N", help=f"DataVersion written to the file (default: {DEFAULT_DATA_VERSION}, Minecraft 1.21.1)")
	ap.add_argument("--out", metavar="FILE", help="where to write the gzipped .nbt")
	ap.add_argument("--dump", metavar="FILE", help="print an existing .nbt as JSON and exit")
	a = ap.parse_args()

	if a.dump:
		raw = Path(a.dump).read_bytes()
		# accept uncompressed nbt too since some editors save it that way
		data = gzip.decompress(raw) if raw[:2] == b"\x1f\x8b" else raw
		print(json.dumps(decode_root(data), indent=2))
		return

	spec = json.loads(Path(a.spec).read_text(encoding="utf-8")) if a.spec else {}
	size = a.size or spec.get("size")
	if not isinstance(size, list) or len(size) != 3:
		fail("--size x,y,z is required")
	if not a.out:
		fail("--out is required")

	nbt = build_template(
		size,
		a.floor or spec.get("floor", "minecraft:stone"),
		a.fill or spec.get("fill", "minecraft:air"),
		spec.get("blocks", []) + a.block,
		a.data_version if a.data_version is not None else spec.get("dataVersion", DEFAULT_DATA_VERSION))
	# mtime 0 so regenerating an unchanged template gives the same bytes
	out = gzip.compress(nbt, mtime=0)
	path = Path(a.out)
	path.parent.mkdir(parents=True, exist_ok=True)
	path.write_bytes(out)
	print(f"wrote {a.out} ({'x'.join(map(str, size))}, {len(out)} bytes)")


if __name__ == "__main__":
	main()
