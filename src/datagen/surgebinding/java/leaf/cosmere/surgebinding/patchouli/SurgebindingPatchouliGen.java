/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.patchouli;

import leaf.cosmere.patchouli.data.PatchouliProvider;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.data.DataGenerator;

//
//  In-Game Documentation generator
//
public class SurgebindingPatchouliGen extends PatchouliProvider
{
	public SurgebindingPatchouliGen(DataGenerator generatorIn)
	{
		super(generatorIn, Surgebinding.MODID);
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

		//PatchouliBasics.collect(this.categories, this.entries);


	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	public String getName()
	{
		return "Cosmere PatchouliGeneration";
	}

}



