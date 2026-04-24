/*
 * File updated ~ 2026-04-25 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
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
