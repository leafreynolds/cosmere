/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api;

import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.util.List;

public interface ISpiritwebSubmodule
{
	void deserialize(ISpiritweb spiritweb);

	void serialize(ISpiritweb spiritweb);

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

	default void collectMenuInfo(List<String> m_infoText)
	{
	}
}
