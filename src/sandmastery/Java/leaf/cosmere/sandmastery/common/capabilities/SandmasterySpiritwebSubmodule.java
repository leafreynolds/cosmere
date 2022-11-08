/*
 * File updated ~ 12 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.capabilities;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.sandmastery.common.manifestation.SandmasteryManifestation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SandmasterySpiritwebSubmodule implements ISpiritwebSubmodule
{
	private int hydrationLevel = 10000;
	public final int MAX_HYDRATION = 10000;
	private Map<SandmasteryManifestation, Integer> ribbons = new HashMap<>();

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

	public void checkRibbons(ISpiritweb data, SandmasteryManifestation manifestation) {
		var inUse = ribbons.get(manifestation);
		int mode = data.getMode(manifestation);
		if(inUse == null) {
			data.setMode(manifestation, mode);
			ribbons.put(manifestation, mode);
		}
		int totalInUse = 0;
		for(int i : ribbons.values()) {
			totalInUse += i;
		}
		int maxRibbons = (int) manifestation.getStrength(data, false);
		if(totalInUse > maxRibbons) {
			data.setMode(manifestation, data.getMode(manifestation)-1);
		};

		ribbons.replace(manifestation, data.getMode(manifestation));
		data.syncToClients(null);
	}
}
