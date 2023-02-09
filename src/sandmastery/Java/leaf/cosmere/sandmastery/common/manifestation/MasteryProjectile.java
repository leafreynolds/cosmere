/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.client.SandmasteryKeybindings;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.network.packets.PlayerShootSandProjectileMessage;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import leaf.cosmere.sandmastery.common.utils.SandmasteryConstants;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

public class MasteryProjectile extends SandmasteryManifestation
{
	public MasteryProjectile(Taldain.Mastery mastery)
	{
		super(mastery);
	}

	@Override
	public void tick(ISpiritweb data) {
		SpiritwebCapability playerSpiritweb = (SpiritwebCapability) data;
		SandmasterySpiritwebSubmodule submodule = (SandmasterySpiritwebSubmodule) playerSpiritweb.spiritwebSubmodules.get(Manifestations.ManifestationTypes.SANDMASTERY);
		submodule.tickProjectileCooldown();
		if (!submodule.projectileReady()) return;

		if(MiscHelper.isClient(data)) performEffectClient(data);

		int hotkeyFlags = CompoundNBTHelper.getOrCreate(data.getCompoundTag(), "sandmastery").getInt("hotkeys");
		boolean enabledViaHotkey = false;
		if((hotkeyFlags & SandmasteryConstants.launchFlagVal) != 0) enabledViaHotkey = true;
		if((hotkeyFlags & 1) != 0) enabledViaHotkey = true;
		if(getMode(data) > 0 && enabledViaHotkey) performEffectServer(data);
	}

	protected void performEffectServer(ISpiritweb data)
	{
		SpiritwebCapability playerSpiritweb = (SpiritwebCapability) data;
		ServerPlayer player = (ServerPlayer) data.getLiving();
		SandmasterySpiritwebSubmodule submodule = (SandmasterySpiritwebSubmodule) playerSpiritweb.spiritwebSubmodules.get(Manifestations.ManifestationTypes.SANDMASTERY);
		if(!submodule.adjustHydration(-10, false)) return;
		if(!enoughChargedSand(data)) return;
		for (int i = 0; i < player.getInventory().getContainerSize(); i++){
			ItemStack pouch = player.getInventory().getItem(i);
			if (!pouch.isEmpty() && pouch.is(SandmasteryItems.SAND_POUCH_ITEM.get()))
			{
				SandmasteryItems.SAND_POUCH_ITEM.get().shoot(pouch, player);

				return;
			}
		}

		submodule.adjustHydration(-10, true);
		useChargedSand(data);
		submodule.setProjectileCooldown(15);
	}

}
