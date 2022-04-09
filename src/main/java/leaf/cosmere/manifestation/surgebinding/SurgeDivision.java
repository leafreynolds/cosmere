/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.surgebinding;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Roshar;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SurgeDivision extends SurgebindingBase
{
    public SurgeDivision(Roshar.Surges surge)
    {
        super(surge);
    }

    //power over destruction and decay

}
