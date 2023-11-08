/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.capabilities.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.util.INBTSerializable;

public interface IScadrial extends INBTSerializable<CompoundTag>
{
	float getMistNearDistance();

	float getMistFarDistance();

	void tickFog(ViewportEvent.RenderFog event, Player player);
}
