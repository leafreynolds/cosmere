/*
 * File updated ~ 12 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.capabilities;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.sandmastery.common.manifestation.MasteryCushion;
import leaf.cosmere.sandmastery.common.manifestation.SandmasteryManifestation;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.LinkedList;
import java.util.List;

public class SandmasterySpiritwebSubmodule implements ISpiritwebSubmodule
{
	private int hydrationLevel = 10000;
	private int projectileCooldown = 0;
	public final int MAX_HYDRATION = 10000;
	private LinkedList<SandmasteryManifestation> ribbonsInUse= new LinkedList<>();

	@Override
	public void tickClient(ISpiritweb spiritweb)
	{
		MasteryCushion cushion = (MasteryCushion) SandmasteryManifestations.SANDMASTERY_POWERS.get(Taldain.Mastery.CUSHION).get();
		cushion.tickClient(spiritweb);
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
		//todo Localization
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

	public void tickProjectileCooldown() {
		this.projectileCooldown -= this.projectileCooldown > 0 ? 1 : 0;
	}

	public void setProjectileCooldown(int cooldown) {
		this.projectileCooldown = cooldown;
	}

	public boolean projectileReady() {
		return this.projectileCooldown == 0;
	}


	public void useRibbon(ISpiritweb data, SandmasteryManifestation manifestation) {
		int maxRibbons = (int) manifestation.getStrength(data, false);
		if(ribbonsInUse.size() >= maxRibbons) {
			SandmasteryManifestation ribbon = ribbonsInUse.getLast();
			data.setMode(ribbon, data.getMode(ribbon) - 1);
		}
		ribbonsInUse.addFirst(manifestation);
		data.syncToClients(null);
	}

	public void releaseRibbon(ISpiritweb data, SandmasteryManifestation manifestation) {
		int index = ribbonsInUse.indexOf(manifestation);
		if(index > -1) ribbonsInUse.remove(index);
		data.syncToClients(null);
	}

	public void debugRibbonUsage() {
		System.out.print("Ribbons in use ");
		System.out.println(ribbonsInUse);
	}
}
