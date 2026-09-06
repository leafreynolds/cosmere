/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.hemalurgy;

import leaf.cosmere.hemalurgy.common.Hemalurgy;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class HemalurgyCuriosProvider extends CuriosDataProvider
{
	public static final ResourceLocation EMPTY_SPIKE_SLOT = ResourceLocation.parse("curios:slot/spike_icon");

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

		this.createEntities("hemalurgists")
				.addPlayer()
				.addEntities(EntityType.ARMOR_STAND) // todo inquisitors
				.addSlots("eyes", "linchpin", "physical", "mental", "spiritual", "temporal");
	}
}
