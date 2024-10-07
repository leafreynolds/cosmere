/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.aviar.patchouli;

import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.patchouli.data.PatchouliProvider;
import net.minecraft.data.PackOutput;

//
//  In-Game Documentation generator
//
public class AviarPatchouliGen extends PatchouliProvider
{
	public AviarPatchouliGen(PackOutput generatorIn)
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



