/*
 * File updated ~ 9 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.soulforgery.patchouli;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.patchouli.data.PatchouliProvider;
import leaf.cosmere.soulforgery.common.Soulforgery;
import net.minecraft.data.PackOutput;

//
//  In-Game Documentation generator
//
public class SoulforgeryPatchouliGen extends PatchouliProvider
{
	public SoulforgeryPatchouliGen(PackOutput generatorIn)
	{
		super(generatorIn, CosmereAPI.COSMERE_MODID);
	}

	@Override
	protected void collectInfoForBook()
	{
		PatchouliSoulforgeryCategory.collect(this.categories, this.entries);
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	public String getName()
	{
		return "Soulforgery PatchouliGeneration";
	}


}



