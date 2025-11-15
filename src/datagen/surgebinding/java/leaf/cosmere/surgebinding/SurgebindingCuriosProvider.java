/*
 * File created ~ 12 - 7 - 2025 ~ Soar
 */

package leaf.cosmere.surgebinding;

import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.concurrent.CompletableFuture;

public class SurgebindingCuriosProvider extends CuriosDataProvider
{
	public SurgebindingCuriosProvider(PackOutput output,
	                                  ExistingFileHelper fileHelper,
	                                  CompletableFuture<HolderLookup.Provider> registries)
	{
		super(Surgebinding.MODID, output, fileHelper, registries);
	}

	@Override
	public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper)
	{
		//https://docs.illusivesoulworks.com/curios/slots/data-generation

		this.createSlot("shardplate")
				.size(1)
				.dropRule(ICurio.DropRule.ALWAYS_KEEP)
				.addCosmetic(true);

		this.createEntities("shardbearers")
				.addPlayer()
				.addEntities(EntityType.ARMOR_STAND, EntityType.VINDICATOR, EntityType.PILLAGER, EntityType.VILLAGER)
				.addSlots("shardplate");
	}
}