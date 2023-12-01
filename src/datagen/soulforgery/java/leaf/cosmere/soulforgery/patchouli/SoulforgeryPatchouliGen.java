/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.soulforgery.patchouli;

import leaf.cosmere.patchouli.data.PatchouliProvider;
import leaf.cosmere.soulforgery.common.Soulforgery;
import net.minecraft.data.DataGenerator;

//
//  In-Game Documentation generator
//
public class SoulforgeryPatchouliGen extends PatchouliProvider
{
	public SoulforgeryPatchouliGen(DataGenerator generatorIn)
	{
		super(generatorIn, Soulforgery.MODID);
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



