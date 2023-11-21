/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.example.patchouli;

import leaf.cosmere.example.common.Example;
import leaf.cosmere.patchouli.data.PatchouliProvider;
import net.minecraft.data.DataGenerator;

//
//  In-Game Documentation generator
//
public class ExamplePatchouliGen extends PatchouliProvider
{
	public ExamplePatchouliGen(DataGenerator generatorIn)
	{
		super(generatorIn, Example.MODID);
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



