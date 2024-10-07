/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.awakening.patchouli;

import leaf.cosmere.awakening.common.Awakening;
import leaf.cosmere.patchouli.data.PatchouliProvider;
import net.minecraft.data.PackOutput;

//
//  In-Game Documentation generator
//
public class AwakeningPatchouliGen extends PatchouliProvider
{
	public AwakeningPatchouliGen(PackOutput generatorIn)
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



