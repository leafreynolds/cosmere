/*
 * File updated ~ 9 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.allomancy.patchouli;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.patchouli.data.PatchouliProvider;
import net.minecraft.data.PackOutput;

//
//  In-Game Documentation generator
//
public class AllomancyPatchouliGen extends PatchouliProvider
{
	public AllomancyPatchouliGen(PackOutput generatorIn)
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
		//  - Manifestations (parent category?)     //
		//  - Feruchemy                             //
		//  - Allomancy                             //
		//  - Hemalurgy                             //
		//  - Curios                                //
		//  - Machines ?                            //
		//  - Challenges ?                          //
		//  -                                       //
		//  -                                       //
		//  -                                       //
		//  -                                       //
		//  -                                       //
		//------------------------------------------//

		PatchouliAllomancyCategory.collect(this.categories, this.entries);


	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	public String getName()
	{
		return "PatchouliGeneration";
	}


}



