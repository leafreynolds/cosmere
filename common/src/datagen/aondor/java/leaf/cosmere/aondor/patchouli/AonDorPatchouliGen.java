/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aondor.patchouli;

import leaf.cosmere.aondor.common.AonDor;
import leaf.cosmere.patchouli.data.PatchouliProvider;
import net.minecraft.data.DataGenerator;

//
//  In-Game Documentation generator
//
public class AonDorPatchouliGen extends PatchouliProvider
{
	public AonDorPatchouliGen(DataGenerator generatorIn)
	{
		super(generatorIn, AonDor.MODID);
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



