/*
 * File updated ~ 9 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.patchouli;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.patchouli.data.PatchouliProvider;
import net.minecraft.data.PackOutput;

//
//  In-Game Documentation generator
//
public class FeruchemyPatchouliGen extends PatchouliProvider
{
	public FeruchemyPatchouliGen(PackOutput generatorIn)
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
		//  - Feruchemy                             //
		//  - Curios                                //
		//  - Machines ?                            //
		//  - Challenges ?                          //
		//  -                                       //
		//  -                                       //
		//  -                                       //
		//  -                                       //
		//  -                                       //
		//------------------------------------------//

		PatchouliFeruchemyCategory.collect(this.categories, this.entries);

	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	public String getName()
	{
		return "PatchouliGeneration";
	}

}
