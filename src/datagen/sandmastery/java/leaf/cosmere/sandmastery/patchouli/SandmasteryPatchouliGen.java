/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.patchouli;

import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.patchouli.data.PatchouliProvider;
import net.minecraft.data.DataGenerator;

//
//  In-Game Documentation generator
//
public class SandmasteryPatchouliGen extends PatchouliProvider
{
	public SandmasteryPatchouliGen(DataGenerator generatorIn)
	{
		super(generatorIn, Sandmastery.MODID);
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
		//  - SandMastery                           //
		//  - Curios                                //
		//  - Machines ?                            //
		//  - Challenges ?                          //
		//  -                                       //
		//  -                                       //
		//  -                                       //
		//  -                                       //
		//  -                                       //
		//------------------------------------------//

		PatchouliSandmasteryCategory.collect(this.categories, this.entries);


	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	public String getName()
	{
		return "PatchouliGeneration";
	}


}



