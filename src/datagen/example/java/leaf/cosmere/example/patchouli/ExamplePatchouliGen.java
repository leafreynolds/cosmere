/*
 * File updated ~ 9 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.example.patchouli;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.example.common.Example;
import leaf.cosmere.patchouli.data.PatchouliProvider;
import net.minecraft.data.PackOutput;

//
//  In-Game Documentation generator
//
public class ExamplePatchouliGen extends PatchouliProvider
{
	public ExamplePatchouliGen(PackOutput generatorIn)
	{
		super(generatorIn, CosmereAPI.COSMERE_MODID);
	}

	@Override
	protected void collectInfoForBook()
	{
		PatchouliExampleCategory.collect(this.categories, this.entries);
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	public String getName()
	{
		return "Example PatchouliGeneration";
	}


}



