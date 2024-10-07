/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.patchouli;

import leaf.cosmere.patchouli.data.PatchouliProvider;
import leaf.cosmere.tools.common.CosmereTools;
import net.minecraft.data.PackOutput;

//
//  In-Game Documentation generator
//
public class ToolsPatchouliGen extends PatchouliProvider
{
	public ToolsPatchouliGen(PackOutput generatorIn)
	{
		super(generatorIn, CosmereTools.MODID);
	}

	@Override
	protected void collectInfoForBook()
	{
		PatchouliToolsCategory.collect(this.categories, this.entries);
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	public String getName()
	{
		return "Tools PatchouliGeneration";
	}


}



