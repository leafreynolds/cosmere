/*
 * File updated ~ 5 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.api;

import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;

public interface ISpiritwebSubmodule
{
	default void deserialize(ISpiritweb spiritweb)
	{
	}

	default void serialize(ISpiritweb spiritweb)
	{
	}

	default void tickClient(ISpiritweb spiritweb)
	{
	}

	default void tickServer(ISpiritweb spiritweb)
	{
	}

	@OnlyIn(Dist.CLIENT)
	default void renderWorldEffects(ISpiritweb spiritweb, RenderLevelStageEvent event)
	{
	}

	//give a random starting item from this power set
	default void GiveStartingItem(Player player)
	{
	}

	//give a specific starting item based on the manifestation passed in
	default void GiveStartingItem(Player player, Manifestation manifestation)
	{
	}

	default void resetOnDeath(ISpiritweb spiritweb)
	{
	}

	default void registerMenu()
	{
	}

	void drainInvestiture(ISpiritweb data, double strength);
}