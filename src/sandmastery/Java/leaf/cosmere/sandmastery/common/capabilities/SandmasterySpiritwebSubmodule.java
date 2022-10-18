/*
 * File updated ~ 12 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.capabilities;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.util.List;

public class SandmasterySpiritwebSubmodule implements ISpiritwebSubmodule
{

	public int hydrationLevel = 4000;

	@Override
	public void tickClient(ISpiritweb spiritweb)
	{
	}

	@Override
	public void tickServer(ISpiritweb spiritweb)
	{
	}

	@Override
	public void deserialize(ISpiritweb spiritweb)
	{
		hydrationLevel = spiritweb.getCompoundTag().getInt("hydration_level");
	}

	@Override
	public void serialize(ISpiritweb spiritweb)
	{
		final CompoundTag compoundTag = spiritweb.getCompoundTag();

		compoundTag.putInt("hydration_level", hydrationLevel);
	}

	@Override
	public void renderWorldEffects(ISpiritweb spiritweb, RenderLevelStageEvent event)
	{
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void collectMenuInfo(List<String> m_infoText)
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
