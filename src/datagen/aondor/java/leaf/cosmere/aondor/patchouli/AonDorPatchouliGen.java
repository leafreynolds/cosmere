/*
 * File updated ~ 9 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.aondor.patchouli;

import leaf.cosmere.aondor.common.AonDor;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.patchouli.data.PatchouliProvider;
import net.minecraft.data.PackOutput;

//
//  In-Game Documentation generator
//
public class AonDorPatchouliGen extends PatchouliProvider
{
	public AonDorPatchouliGen(PackOutput generatorIn)
	{
		super(generatorIn, CosmereAPI.COSMERE_MODID);
	}

	@Override
	protected void collectInfoForBook()
	{
		PatchouliAonDorCategory.collect(this.categories, this.entries);
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	public String getName()
	{
		return "AonDor PatchouliGeneration";
	}


}



