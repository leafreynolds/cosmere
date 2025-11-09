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

public class PowerSaveState
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

		public static Optional<PowerSaveState.PowerSaves> valueOf(int value)
		{
			return Arrays.stream(values())
					.filter(powerTypes -> powerTypes.num == value)
					.findFirst();
		}

		PowerSaves(int num)
		{
			this.num = num;

		}

		public String getName()
		{
			return "Power Save State " + (num + 1);
		}

		public int getNum()
		{
			return num;
		}



		public boolean isActive(ISpiritweb spiritweb)
		{
			Map<Manifestation, Integer> active = spiritweb.getManifestations();
			for (Map.Entry<Manifestation, Integer> entry : manifestations.entrySet())
			{
				Map.Entry<Manifestation, Integer> activeEntry = getEntry(active, entry.getKey());
				if (activeEntry == null)
				{
					return false;
				}
				if (!entry.equals(activeEntry))
				{
					return false;
				}
			}
			return true;
		}


		public boolean hasManifestation(Manifestation manifestation)
		{
			return manifestations.containsKey(manifestation);
		}

		public void activate(ISpiritweb spiritweb)
		{
			boolean toActivate = !isActive(spiritweb);

			for (Manifestation manifestation : manifestations.keySet())
			{
				int modifier = -(manifestation.getMode(spiritweb));
				if (toActivate)
				{
					modifier += manifestations.get(manifestation);
				}
				Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(manifestation, modifier));

			}
			if(CosmereConfigs.CLIENT_CONFIG.disableActivatorChatMessage.get())
			{
				return;
			}

			if(toActivate)
			{
				spiritweb.getLiving().sendSystemMessage(Component.literal("Activating " + getName()));
			}
			else
			{
				spiritweb.getLiving().sendSystemMessage(Component.literal("Deactivating " + getName()));
			}
			manifestations.keySet().forEach((manifest) ->


						spiritweb.getLiving()
							.sendSystemMessage(Component.literal(
									Component.translatable(
											manifest.getTranslationKey()
									).getString() + ": " + (toActivate ? manifestations.get(manifest) : 0)
							))
					);

		}

		public void addManifestations(ISpiritweb spiritweb)
		{
			manifestations = spiritweb.getManifestations(false, true);
			if(CosmereConfigs.CLIENT_CONFIG.disableActivatorChatMessage.get())
			{
				return;
			}
			spiritweb.getLiving().sendSystemMessage(Component.literal("Saved new " + getName()));
			manifestations.forEach((manifestation, integer) ->
					spiritweb.getLiving()
							.sendSystemMessage(Component.literal(
									Component.translatable(
											manifestation.getTranslationKey()
									).getString() + ": " + integer)));


		}

		private void setManifestations(HashMap<Manifestation,Integer> manifestations)
		{
			this.manifestations = manifestations;
		}
	}

	private static Map.@Nullable Entry<Manifestation, Integer> getEntry(@NotNull Map<Manifestation, Integer> map, Manifestation manifestation)
	{
		for (Map.Entry<Manifestation, Integer> entry : map.entrySet())
		{
			Manifestation maniType = ManifestationRegistry.fromID(manifestation.getRegistryName());
			Manifestation entryType = ManifestationRegistry.fromID(entry.getKey().getRegistryName());
			if (maniType.equals(entryType))
			{
				return entry;
			}
		}
		return null;

	}
	public static CompoundTag serialize()
	{
		CompoundTag nbt = new CompoundTag();
		for(PowerSaves saveState: PowerSaves.values())
		{
			CompoundTag data = new CompoundTag();
			for(Manifestation manifest: saveState.manifestations.keySet())
			{
				data.putInt(manifest.getRegistryName().toString(),saveState.manifestations.get(manifest));
			}
			nbt.put(Integer.toString(saveState.num), data);
		}
		return nbt;
	}


	public static void deserialize(CompoundTag nbt)
	{
		for(PowerSaves state : PowerSaves.values())
		{
			CompoundTag data = (CompoundTag) nbt.get(Integer.toString(state.num));

			HashMap<Manifestation,Integer> manifestations = new HashMap<>();

			for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
			{
				final String manifestationLoc = manifestation.getRegistryName().toString();

				if (data.contains(manifestationLoc))
				{
					manifestations.put(manifestation, data.getInt(manifestationLoc));
				}
			}

			state.setManifestations(manifestations);
		}
	}


}
