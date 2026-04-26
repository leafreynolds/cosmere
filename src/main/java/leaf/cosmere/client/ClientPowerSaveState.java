/*
 * File updated ~ 10 - 6 - 2025 ~ SoaringEaqle
 */

package leaf.cosmere.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.CosmereConfigs;
import leaf.cosmere.common.network.packets.ChangeManifestationModeMessage;
import leaf.cosmere.common.registry.ManifestationRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ClientPowerSaveState
{

	public enum PowerSaves
	{
		POWER_SAVE_1(0),
		POWER_SAVE_2(1),
		POWER_SAVE_3(2),
		POWER_SAVE_4(3),
		POWER_SAVE_5(4),
		POWER_SAVE_6(5),
		POWER_SAVE_7(6),
		POWER_SAVE_8(7),
		POWER_SAVE_9(8);

		private HashMap<Manifestation, Integer> manifestations = new HashMap<>();
		private final int num;

		public static Optional<ClientPowerSaveState.PowerSaves> valueOf(int value)
		{
			return Arrays.stream(values())
					.filter(powerTypes -> powerTypes.num == value)
					.findFirst();
		}

		PowerSaves(int num)
		{
			this.num = num;

		}



		public int getNum()
		{
			return num;
		}

	}



}
