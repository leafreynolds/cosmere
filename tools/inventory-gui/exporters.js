// Code exporters for the inventory GUI generator. Each exporter turns a layout into files.
// To add a target, append to EXPORTERS; see README.md "Adding an exporter".
// Classic script in the browser (sets globalThis.InventoryGuiExporters), CommonJS in Node.
(function (root)
{
	'use strict';

	const core = typeof module !== 'undefined' && module.exports ? require('./core.js') : root.InventoryGui;

	// names: { module: 'sandmastery', modClass: 'Sandmastery', name: 'Crusher' }
	function context(opts, lay, names)
	{
		const module = (names.module || 'example').toLowerCase().replace(/[^a-z0-9_]/g, '');
		const modClass = names.modClass || (module.charAt(0).toUpperCase() + module.slice(1));
		const name = (names.name || 'Example').replace(/[^A-Za-z0-9]/g, '') || 'Example';
		const snake = name.replace(/([a-z0-9])([A-Z])/g, '$1_$2').toLowerCase();
		const base = `leaf.cosmere.${module}`;
		const containerSlots = lay.slots.filter(s => s.kind === 'container' || s.kind === 'custom');
		return {
			opts, lay, module, modClass, name, snake,
			constant: snake.toUpperCase(),
			menuClass: `${name}Menu`,
			screenClass: `${name}Screen`,
			menuPkg: `${base}.common.menu`,
			screenPkg: `${base}.client.gui`,
			menuTypesClass: `${modClass}MenuTypes`,
			menuTypesPkg: `${base}.common.registries`,
			texture: `textures/gui/${snake}.png`,
			texturePath: `src/${module}/resources/assets/${module}/textures/gui/${snake}.png`,
			javaPath: pkg => `src/${module}/java/${pkg.replace(/\./g, '/')}`,
			containerSlots,
		};
	}

	// Contiguous [start, end) index ranges of container slots that accept inserts.
	function inputRanges(slots)
	{
		const ranges = [];
		slots.forEach((s, i) =>
		{
			if (s.role === 'output') return;
			const last = ranges[ranges.length - 1];
			if (last && last[1] === i) last[1] = i + 1;
			else ranges.push([i, i + 1]);
		});
		return ranges;
	}

	function slotLines(c, indent)
	{
		const { opts, lay } = c;
		const lines = [];
		const grid = lay.slots.filter(s => s.kind === 'container');
		if (grid.length)
		{
			lines.push('// Container grid');
			if (opts.slots % lay.cols === 0 || opts.align === 'left')
				lines.push(`for (int i = 0; i < ${grid.length}; i++)`,
					`\taddSlot(new SlotItemHandler(items, i, ${lay.gridLeft + 1} + (i % ${lay.cols}) * 18, 18 + (i / ${lay.cols}) * 18));`);
			else
				grid.forEach(s => lines.push(`addSlot(new SlotItemHandler(items, ${s.index}, ${s.x + 1}, ${s.y + 1}));`));
		}
		const placed = lay.slots.filter(s => s.kind === 'custom');
		if (placed.length)
		{
			lines.push('// Placed slots');
			for (const s of placed)
			{
				const inset = core.SIZES[s.size].inset;
				const cls = s.role === 'output' ? 'OutputSlot' : 'SlotItemHandler';
				lines.push(`addSlot(new ${cls}(items, ${s.index}, ${s.x + inset}, ${s.y + inset}));`);
			}
		}
		if (lay.player)
		{
			const lx = lay.player.left + 1;
			lines.push('// Player inventory',
				'for (int row = 0; row < 3; row++)',
				'\tfor (int col = 0; col < 9; col++)',
				`\t\taddSlot(new Slot(playerInv, col + row * 9 + 9, ${lx} + col * 18, ${lay.player.invY + 1} + row * 18));`,
				'// Hotbar',
				'for (int col = 0; col < 9; col++)',
				`\taddSlot(new Slot(playerInv, col, ${lx} + col * 18, ${lay.player.hotbarY + 1}));`);
		}
		return lines.map(l => indent + l).join('\n');
	}

	function quickMove(c)
	{
		if (!c.lay.player)
			return [
				'\t@Override',
				'\tpublic ItemStack quickMoveStack(Player player, int index)',
				'\t{',
				'\t\treturn ItemStack.EMPTY;',
				'\t}',
			].join('\n');
		const toInputs = inputRanges(c.containerSlots)
			.map(([a, b]) => `!this.moveItemStackTo(stack, ${a}, ${b}, false)`).join(' && ') || 'true';
		return [
			'\t@Override',
			'\tpublic ItemStack quickMoveStack(Player player, int index)',
			'\t{',
			'\t\tSlot slot = this.slots.get(index);',
			'\t\tif (!slot.hasItem())',
			'\t\t\treturn ItemStack.EMPTY;',
			'',
			'\t\tItemStack stack = slot.getItem();',
			'\t\tItemStack original = stack.copy();',
			'\t\tif (index < CONTAINER_SLOTS)',
			'\t\t{',
			'\t\t\tif (!this.moveItemStackTo(stack, CONTAINER_SLOTS, this.slots.size(), true))',
			'\t\t\t\treturn ItemStack.EMPTY;',
			'\t\t}',
			`\t\telse if (${toInputs})`,
			'\t\t{',
			'\t\t\treturn ItemStack.EMPTY;',
			'\t\t}',
			'',
			'\t\tif (stack.isEmpty())',
			'\t\t\tslot.set(ItemStack.EMPTY);',
			'\t\telse',
			'\t\t\tslot.setChanged();',
			'\t\treturn original;',
			'\t}',
		].join('\n');
	}

	function menuFile(c)
	{
		const hasOutput = c.containerSlots.some(s => s.role === 'output');
		const widgets = c.lay.progress;
		const data = widgets.length > 0;
		const imports = [
			`import ${c.menuTypesPkg}.${c.menuTypesClass};`,
			'import net.minecraft.core.BlockPos;',
			'import net.minecraft.network.RegistryFriendlyByteBuf;',
			'import net.minecraft.world.entity.player.Inventory;',
			'import net.minecraft.world.entity.player.Player;',
			'import net.minecraft.world.inventory.AbstractContainerMenu;',
			data && 'import net.minecraft.world.inventory.ContainerData;',
			'import net.minecraft.world.inventory.ContainerLevelAccess;',
			data && 'import net.minecraft.world.inventory.SimpleContainerData;',
			'import net.minecraft.world.inventory.Slot;',
			'import net.minecraft.world.item.ItemStack;',
			'import net.neoforged.neoforge.items.IItemHandler;',
			'import net.neoforged.neoforge.items.ItemStackHandler;',
			'import net.neoforged.neoforge.items.SlotItemHandler;',
		].filter(Boolean);
		const out = [
			`package ${c.menuPkg};`,
			'',
			...imports,
			'',
			`public class ${c.menuClass} extends AbstractContainerMenu`,
			'{',
			`\tpublic static final int CONTAINER_SLOTS = ${c.containerSlots.length};`,
		];
		if (data)
		{
			out.push('\t// Each widget reads two data slots: progress at 2*i, maximum at 2*i+1.');
			widgets.forEach((w, i) => out.push(`\tpublic static final int WIDGET_${w.constant} = ${i};`));
			out.push(`\tpublic static final int DATA_SLOTS = ${widgets.length * 2};`);
		}
		out.push('\tprivate final ContainerLevelAccess access;');
		if (data) out.push('\tprivate final ContainerData data;');
		const clientData = data ? ', new SimpleContainerData(DATA_SLOTS)' : '';
		const dataParam = data ? ', ContainerData data' : '';
		out.push('',
			'\t// Client side: contents arrive through menu sync, so empty containers are enough.',
			`\tpublic static ${c.menuClass} fromNetwork(int windowId, Inventory playerInv, RegistryFriendlyByteBuf buf)`,
			'\t{',
			'\t\tBlockPos pos = buf.readBlockPos();',
			`\t\treturn new ${c.menuClass}(windowId, playerInv, new ItemStackHandler(CONTAINER_SLOTS)${clientData}, ContainerLevelAccess.create(playerInv.player.level(), pos));`,
			'\t}',
			'',
			`\tpublic ${c.menuClass}(int windowId, Inventory playerInv, IItemHandler items${dataParam}, ContainerLevelAccess access)`,
			'\t{',
			`\t\tsuper(${c.menuTypesClass}.${c.constant}.get(), windowId);`,
			'\t\tthis.access = access;');
		if (data)
			out.push('\t\tcheckContainerDataCount(data, DATA_SLOTS);',
				'\t\tthis.data = data;',
				'\t\taddDataSlots(data);');
		out.push('',
			slotLines(c, '\t\t'),
			'\t}');
		if (data)
			out.push('',
				'\t// Filled size in pixels; for fuel-style widgets pass remaining time as progress.',
				'\tpublic int getScaledProgress(int widget, int pixels)',
				'\t{',
				'\t\tint progress = data.get(widget * 2);',
				'\t\tint max = data.get(widget * 2 + 1);',
				'\t\treturn max > 0 && progress > 0 ? Math.min(pixels, progress * pixels / max) : 0;',
				'\t}');
		out.push('',
			quickMove(c),
			'',
			'\t@Override',
			'\tpublic boolean stillValid(Player player)',
			'\t{',
			'\t\treturn access.evaluate((level, pos) -> player.canInteractWithBlock(pos, 4.0), true);',
			'\t}');
		if (hasOutput)
			out.push('',
				'\tprivate static class OutputSlot extends SlotItemHandler',
				'\t{',
				'\t\tOutputSlot(IItemHandler items, int index, int x, int y)',
				'\t\t{',
				'\t\t\tsuper(items, index, x, y);',
				'\t\t}',
				'',
				'\t\t@Override',
				'\t\tpublic boolean mayPlace(ItemStack stack)',
				'\t\t{',
				'\t\t\treturn false;',
				'\t\t}',
				'\t}');
		out.push('}', '');
		return out.join('\n');
	}

	// Draws each widget's filled sprite from the atlas, cropped to progress along its fill direction.
	function widgetBlits(c)
	{
		const tex = c.opts.canvas === 256 ? '' : `, ${c.opts.canvas}, ${c.opts.canvas}`;
		const lines = [];
		for (const w of c.lay.progress)
		{
			const v = w.constant.toLowerCase().replace(/_([a-z0-9])/g, (m, ch) => ch.toUpperCase()) + 'Px';
			const horizontal = w.dir === 'right' || w.dir === 'left';
			const full = horizontal ? w.w : w.h;
			const X = `leftPos + ${w.x}`, Y = `topPos + ${w.y}`;
			let call;
			if (w.dir === 'right') call = [X, Y, w.u, w.v, v, w.h];
			else if (w.dir === 'left') call = [`${X} + ${w.w} - ${v}`, Y, `${w.u} + ${w.w} - ${v}`, w.v, v, w.h];
			else if (w.dir === 'up') call = [X, `${Y} + ${w.h} - ${v}`, w.u, `${w.v} + ${w.h} - ${v}`, w.w, v];
			else call = [X, Y, w.u, w.v, w.w, v];
			lines.push('',
				`// ${w.constant}: fills ${w.dir}`,
				`int ${v} = menu.getScaledProgress(${c.menuClass}.WIDGET_${w.constant}, ${full});`,
				`if (${v} > 0)`,
				`\tguiGraphics.blit(TEXTURE, ${call.join(', ')}${tex});`);
		}
		return lines;
	}

	function screenFile(c)
	{
		const { lay, opts } = c;
		const blit = opts.canvas === 256
			? 'guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);'
			: `guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, ${opts.canvas}, ${opts.canvas});`;
		const out = [
			`package ${c.screenPkg};`,
			'',
			`import ${c.menuPkg}.${c.menuClass};`,
			`import leaf.cosmere.${c.module}.common.${c.modClass};`,
			'import net.minecraft.client.gui.GuiGraphics;',
			'import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;',
			'import net.minecraft.network.chat.Component;',
			'import net.minecraft.resources.ResourceLocation;',
			'import net.minecraft.world.entity.player.Inventory;',
			'',
			`public class ${c.screenClass} extends AbstractContainerScreen<${c.menuClass}>`,
			'{',
			`\tprivate static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(${c.modClass}.MODID, "${c.texture}");`,
			'',
			`\tpublic ${c.screenClass}(${c.menuClass} menu, Inventory playerInv, Component title)`,
			'\t{',
			'\t\tsuper(menu, playerInv, title);',
			`\t\tthis.imageWidth = ${lay.width};`,
			`\t\tthis.imageHeight = ${lay.height};`,
		];
		if (lay.player)
			out.push(`\t\tthis.inventoryLabelX = ${lay.player.left + 1};`,
				`\t\tthis.inventoryLabelY = ${lay.player.invY - 11};`);
		out.push('\t}',
			'',
			'\t// Screen.render already draws the blurred background; calling renderBackground again doubles it.',
			'\t@Override',
			'\tpublic void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)',
			'\t{',
			'\t\tsuper.render(guiGraphics, mouseX, mouseY, partialTick);',
			'\t\tthis.renderTooltip(guiGraphics, mouseX, mouseY);',
			'\t}',
			'',
			'\t@Override',
			'\tprotected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)',
			'\t{',
			`\t\t${blit}`,
			...widgetBlits(c).map(l => l ? '\t\t' + l : ''),
			'\t}');
		if (!lay.player)
			out.push('',
				'\t@Override',
				'\tprotected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)',
				'\t{',
				'\t\tguiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);',
				'\t}');
		out.push('}', '');
		return out.join('\n');
	}

	function registrationFile(c)
	{
		return [
			`// ${c.javaPath(c.menuTypesPkg)}/${c.menuTypesClass}.java  (add to MENU_TYPES)`,
			`public static final MenuTypeRegistryObject<MenuType<?>> ${c.constant} = MENU_TYPES.register(`,
			`\t\t"${c.snake}",`,
			`\t\t() -> IMenuTypeExtension.create(${c.menuClass}::fromNetwork));`,
			'',
			`// ${c.modClass}ModClientEvents (client-only @EventBusSubscriber)`,
			'@SubscribeEvent',
			'public static void registerMenuScreens(RegisterMenuScreensEvent event)',
			'{',
			`\tevent.register((MenuType<${c.menuClass}>) ${c.menuTypesClass}.${c.constant}.get(), ${c.screenClass}::new);`,
			'}',
			'',
			'// Your block, server side: open the menu and send the position to the client.',
			'@Override',
			'protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)',
			'{',
			'\tif (!level.isClientSide && level.getBlockEntity(pos) instanceof YourBlockEntity be)',
			'\t\tplayer.openMenu(new SimpleMenuProvider(',
			`\t\t\t\t(id, inv, p) -> new ${c.menuClass}(id, inv, be.getItemHandler()${c.lay.progress.length ? ', be.getContainerData()' : ''}, ContainerLevelAccess.create(level, pos)),`,
			`\t\t\t\tComponent.translatable("container.${c.module}.${c.snake}")), pos);`,
			'\treturn InteractionResult.sidedSuccess(level.isClientSide);',
			'}',
			'',
		].join('\n');
	}

	const EXPORTERS = [
		{
			id: 'snippet', label: 'Slot snippet',
			files: c => [{ title: 'Slot code', path: null, content: core.javaSnippet(c.opts, c.lay) + '\n' }],
		},
		{
			id: 'screen', label: 'Screen class',
			files: c => [{ title: `${c.screenClass}.java`, path: `${c.javaPath(c.screenPkg)}/${c.screenClass}.java`, content: screenFile(c) }],
		},
		{
			id: 'menu', label: 'Menu class',
			files: c => [{ title: `${c.menuClass}.java`, path: `${c.javaPath(c.menuPkg)}/${c.menuClass}.java`, content: menuFile(c) }],
		},
		{
			id: 'registration', label: 'Registration',
			files: c => [{ title: 'Registration snippets', path: null, content: registrationFile(c) }],
		},
	];

	const api = { EXPORTERS, context };
	if (typeof module !== 'undefined' && module.exports) module.exports = api;
	else root.InventoryGuiExporters = api;
})(typeof globalThis !== 'undefined' ? globalThis : this);
