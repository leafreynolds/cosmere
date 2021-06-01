/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.surgebinding;

import leaf.cosmere.Cosmere;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SurgeTransformation extends SurgebindingBase
{
    public SurgeTransformation(int color)
    {
        super(color);
    }


    //soulcasting, changing one thing into another

}
