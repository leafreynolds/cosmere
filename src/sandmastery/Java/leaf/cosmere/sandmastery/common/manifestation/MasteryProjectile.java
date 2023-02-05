/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.client.SandmasteryKeybindings;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.network.packets.PlayerShootSandProjectileMessage;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;

public class MasteryProjectile extends SandmasteryManifestation
{
	public MasteryProjectile(Taldain.Mastery mastery)
	{
		super(mastery);
	}

	@Override
	public void tick(ISpiritweb data) {
		int mode = getMode(data);
		if (mode <= 0) return;
		SpiritwebCapability playerSpiritweb = (SpiritwebCapability) data;
		SandmasterySpiritwebSubmodule submodule = (SandmasterySpiritwebSubmodule) playerSpiritweb.spiritwebSubmodules.get(Manifestations.ManifestationTypes.SANDMASTERY);
		submodule.tickProjectileCooldown();
		if (!submodule.projectileReady()) return;
		if ((MiscHelper.isActivatedAndActive(data, this) || SandmasteryKeybindings.SANDMASTERY_PROJECTILE.isDown())) {
			applyEffectTick(data);
		}
	}

	@Override
	public void applyEffectTick(ISpiritweb data)
	{
		performEffectServer(data);
	}

	private void performEffectServer(ISpiritweb data)
	{
		SpiritwebCapability playerSpiritweb = (SpiritwebCapability) data;
		SandmasterySpiritwebSubmodule submodule = (SandmasterySpiritwebSubmodule) playerSpiritweb.spiritwebSubmodules.get(Manifestations.ManifestationTypes.SANDMASTERY);
		if(!submodule.adjustHydration(-10, false)) return;
		if(!enoughChargedSand(data)) return;

		Sandmastery.packetHandler().sendToServer(new PlayerShootSandProjectileMessage());

		submodule.adjustHydration(-10, true);
		useChargedSand(data);
		submodule.setProjectileCooldown(15);
	}

}
