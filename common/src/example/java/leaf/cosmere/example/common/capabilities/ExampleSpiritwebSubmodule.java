/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.example.common.capabilities;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.util.List;

public class ExampleSpiritwebSubmodule implements ISpiritwebSubmodule
{
	@Override
	public void serialize(ISpiritweb spiritweb)
	{

	}

	@Override
	public void deserialize(ISpiritweb spiritweb)
	{

	}

	@Override
	public void tickClient(ISpiritweb spiritweb)
	{

	}

	@Override
	public void tickServer(ISpiritweb spiritweb)
	{

	}

	@Override
	public void collectMenuInfo(List<String> m_infoText)
	{

	}

	@Override
	public void renderWorldEffects(ISpiritweb spiritweb, RenderLevelStageEvent event)
	{

	}

	@Override
	public void GiveStartingItem(Player player)
	{

	}

	@Override
	public void GiveStartingItem(Player player, Manifestation manifestation)
	{

	}
}
