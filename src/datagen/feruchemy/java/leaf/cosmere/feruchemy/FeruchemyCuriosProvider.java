/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy;

import leaf.cosmere.feruchemy.common.Feruchemy;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.concurrent.CompletableFuture;

public class FeruchemyCuriosProvider extends CuriosDataProvider
{
	public FeruchemyCuriosProvider(PackOutput output,
	                               ExistingFileHelper fileHelper,
	                               CompletableFuture<HolderLookup.Provider> registries)
	{
		super(Feruchemy.MODID, output, fileHelper, registries);
	}

	@Override
	public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper)
	{
		//https://docs.illusivesoulworks.com/curios/slots/data-generation

		this.createSlot("necklace")
				.size(2)
				.dropRule(ICurio.DropRule.ALWAYS_KEEP);
		this.createSlot("bracelet")
				.size(6)
				.dropRule(ICurio.DropRule.ALWAYS_KEEP);
		this.createSlot("ring")
				.size(8)
				.dropRule(ICurio.DropRule.ALWAYS_KEEP);

		this.createEntities("feruchemists")
				.addEntities(EntityType.PLAYER, EntityType.ARMOR_STAND) // todo inquisitors
				.addSlots("necklace", "bracelet", "ring");
	}
}
