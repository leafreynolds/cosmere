/*
* File created 12 - 7 - 2025 ~ Soar
 */
package leaf.cosmere.allomancy;

import leaf.cosmere.allomancy.common.Allomancy;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.concurrent.CompletableFuture;

public class AllomancyCuriosProvider extends CuriosDataProvider
{
	public AllomancyCuriosProvider(PackOutput output,
	                               ExistingFileHelper fileHelper,
	                               CompletableFuture<HolderLookup.Provider> registries)
	{
		super(Allomancy.MODID, output, fileHelper, registries);
	}

	@Override
	public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper)
	{
		//https://docs.illusivesoulworks.com/curios/slots/data-generation

		this.createSlot("head")
				.size(1)
				.dropRule(ICurio.DropRule.DEFAULT)
				.addCosmetic(true);

		this.createEntities("mistborn")
				.addPlayer()
				.addEntities(EntityType.ARMOR_STAND)
				.addSlots("head");
	}
}
