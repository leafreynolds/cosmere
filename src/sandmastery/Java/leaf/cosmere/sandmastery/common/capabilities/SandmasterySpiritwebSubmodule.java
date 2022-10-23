/*
 * File updated ~ 12 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.capabilities;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.DrawHelper;
import leaf.cosmere.api.helpers.PlayerHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SandmasterySpiritwebSubmodule implements ISpiritwebSubmodule
{
	private int hydrationLevel = 1000;

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
		final String text = "Hydration: " + getHydrationLevel();
		m_infoText.add(text);
	}

	@Override
	public void GiveStartingItem(Player player)
	{
	}

	@Override
	public void GiveStartingItem(Player player, Manifestation manifestation)
	{
	}

	public int getHydrationLevel() { return hydrationLevel; }

	public boolean adjustHydration(int amountToAdjust, boolean doAdjust)
	{
		int hydration = getHydrationLevel();
		final int newHydrationValue = hydration + amountToAdjust;
		if (newHydrationValue >= 0)
		{
			if (doAdjust)
			{
				hydrationLevel = newHydrationValue;
			}
			return true;
		}

		return false;
	}
}
