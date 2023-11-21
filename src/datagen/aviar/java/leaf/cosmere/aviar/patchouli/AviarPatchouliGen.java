/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.patchouli;

import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.patchouli.data.PatchouliProvider;
import net.minecraft.data.DataGenerator;

//
//  In-Game Documentation generator
//
public class AviarPatchouliGen extends PatchouliProvider
{
	public AviarPatchouliGen(DataGenerator generatorIn)
	{
		super(generatorIn, Aviar.MODID);
	}

	@Override
	protected void collectInfoForBook()
	{
		PatchouliAviarCategory.collect(this.categories, this.entries);
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	public String getName()
	{
		return "Aviar PatchouliGeneration";
	}


}



