/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.hemalurgy;

import leaf.cosmere.hemalurgy.common.Hemalurgy;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.concurrent.CompletableFuture;

public class HemalurgyCuriosProvider extends CuriosDataProvider
{
	public static final ResourceLocation EMPTY_SPIKE_SLOT = new ResourceLocation("curios:slot/spike_icon");

	public HemalurgyCuriosProvider(PackOutput output,
	                               ExistingFileHelper fileHelper,
	                               CompletableFuture<HolderLookup.Provider> registries)
	{
		super(Hemalurgy.MODID, output, fileHelper, registries);
	}

	@Override
	public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper)
	{
		//https://docs.illusivesoulworks.com/curios/slots/data-generation

		this.createSlot("eyes")
				.size(2)
				.dropRule(ICurio.DropRule.ALWAYS_KEEP)
				.icon(InventoryMenu.EMPTY_ARMOR_SLOT_HELMET)
				.order(0)
				.addCosmetic(true);
		this.createSlot("linchpin")
				.size(1)
				.dropRule(ICurio.DropRule.ALWAYS_KEEP)
				.icon(EMPTY_SPIKE_SLOT)
				.order(1)
				.addCosmetic(true);

		this.createSlot("physical")
				.size(8)
				.dropRule(ICurio.DropRule.ALWAYS_KEEP)
				.icon(EMPTY_SPIKE_SLOT)
				.order(501)
				.addCosmetic(true);

		this.createSlot("mental")
				.size(10)
				.dropRule(ICurio.DropRule.ALWAYS_KEEP)
				.icon(EMPTY_SPIKE_SLOT)
				.order(502)
				.addCosmetic(true);

		this.createSlot("spiritual")
				.size(4)
				.dropRule(ICurio.DropRule.ALWAYS_KEEP)
				.icon(EMPTY_SPIKE_SLOT)
				.order(503)
				.addCosmetic(true);

		this.createSlot("temporal")
				.size(16)
				.dropRule(ICurio.DropRule.ALWAYS_KEEP)
				.icon(EMPTY_SPIKE_SLOT)
				.order(504)
				.addCosmetic(true);

		this.createEntities("hemalurgists")
				.addEntities(EntityType.PLAYER, EntityType.ARMOR_STAND) // todo inquisitors
				.addSlots("eyes", "linchpin", "physical", "mental", "spiritual", "temporal");
	}
}
