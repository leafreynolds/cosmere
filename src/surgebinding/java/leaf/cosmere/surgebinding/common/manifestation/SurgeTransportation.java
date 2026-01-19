/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Roshar;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class SurgeTransportation extends SurgebindingManifestation
{
	public SurgeTransportation(Roshar.Surges surge)
	{
		super(surge);
	}


	//travel between realms or locations

	public static void onEmptyClick(PlayerInteractEvent.RightClickEmpty event){
		System.out.println(event.getLevel().getEntities(event.getEntity(), AABB.ofSize(event.getEntity().getEyePosition(),4,4,4)));
	}
}
