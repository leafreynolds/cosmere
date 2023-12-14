/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.awakening.patchouli;

import leaf.cosmere.awakening.common.Awakening;
import leaf.cosmere.patchouli.data.PatchouliProvider;
import net.minecraft.data.DataGenerator;

//
//  In-Game Documentation generator
//
public class AwakeningPatchouliGen extends PatchouliProvider
{
	public AwakeningPatchouliGen(DataGenerator generatorIn)
	{
		super(generatorIn, Awakening.MODID);
	}

	@Override
	protected void collectInfoForBook()
	{
		PatchouliAwakeningCategory.collect(this.categories, this.entries);
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	public String getName()
	{
		return "Awakening PatchouliGeneration";
	}


}



