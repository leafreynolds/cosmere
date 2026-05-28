/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.capabilities.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IScadrial extends INBTSerializable<CompoundTag>
{
	float getMistNearDistance();

	float getMistFarDistance();

	void tickFog(ViewportEvent.RenderFog event, Player player);
}
