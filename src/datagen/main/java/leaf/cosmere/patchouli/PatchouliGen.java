/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.patchouli;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.patchouli.data.PatchouliProvider;
import net.minecraft.data.DataGenerator;

//
//  In-Game Documentation generator
//
public class PatchouliGen extends PatchouliProvider
{
	public PatchouliGen(DataGenerator generatorIn)
	{
		super(generatorIn, CosmereAPI.COSMERE_MODID);
	}


	@Override
	protected void collectInfoForBook()
	{
		//dynamically figure out all the things we wanna generate categories/entries for?
		//------------------------------------------//
		//              Categories                  //
		//------------------------------------------//
		//  - Basics                                //
		//  - Curios                                //
		//  - Machines ?                            //
		//  - Challenges ?                          //
		//  -                                       //
		//------------------------------------------//

		PatchouliBasics.collect(this.categories, this.entries);


	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	public String getName()
	{
		return "PatchouliGeneration";
	}

}



