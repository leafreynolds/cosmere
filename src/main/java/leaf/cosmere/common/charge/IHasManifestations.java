package leaf.cosmere.common.charge;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.UUID;

public interface IHasManifestations
{
	int getMaxCapacity();

	default void addManifestation(ItemStack itemStack, Manifestation manifestation, int strength, byte manifestationSlot)
	{
		if (manifestation == null)
		{
			return;
		}
		Manifestation[] manifestations = getManifestations(itemStack);
		Integer[] manifestationStrengths = getManifestationStrengths(itemStack);

		if (manifestations == null || manifestations.length == 0)
		{
			manifestations = new Manifestation[getMaxCapacity()];
			manifestationStrengths = new Integer[getMaxCapacity()];
		}

		int newStrength = strength;
		if (manifestations[manifestationSlot] != null && manifestationStrengths[manifestationSlot] != null)
		{
			newStrength += manifestationStrengths[manifestationSlot];
		}

		manifestations[manifestationSlot] = manifestation;
		manifestationStrengths[manifestationSlot] = newStrength;
		setManifestations(itemStack, manifestations);
		setManifestationStrengths(itemStack, manifestationStrengths);
	}

	default void removeManifestation(ItemStack itemStack, Manifestation manifestation)
	{
		if (manifestation == null)
		{
			return;
		}
		Manifestation[] manifestations = getManifestations(itemStack);
		Integer[] manifestationStrengths = getManifestationStrengths(itemStack);

		for (int i = 0; i < manifestations.length; i++)
		{
			if (manifestations[i] == manifestation)
			{
				manifestations[i] = null;
				manifestationStrengths[i] = null;
				setManifestations(itemStack, manifestations);
				setManifestationStrengths(itemStack, manifestationStrengths);
				break;
			}
		}

		for(Manifestation mani : manifestations) if(mani != null) return;
		//if no manifestations left, clear attuned player
		StackNBTHelper.removeEntry(itemStack, Constants.NBT.ATTUNED_PLAYER);
		StackNBTHelper.removeEntry(itemStack, Constants.NBT.ATTUNED_PLAYER_NAME);
	}

	default Manifestation[] getManifestations(ItemStack itemStack)
	{
		CompoundTag nbt = itemStack.getOrCreateTag();
		Manifestation[] manifestations;
		if (!nbt.contains("manifestationIds"))
		{
			return new Manifestation[getMaxCapacity()];
		}


		ListTag manifestationListTag = (ListTag) nbt.get("manifestationIds");

		manifestations = new Manifestation[manifestationListTag.size()];
		for (int i = 0; i < manifestationListTag.size(); i++)
		{
			CompoundTag tag = (CompoundTag) manifestationListTag.get(i);
			if (tag.getString("manifestation").equals("null"))
			{
				manifestations[i] = null;
			}
			else
			{
				manifestations[i] = CosmereAPI.manifestationRegistry().getValue(new ResourceLocation(tag.getString("manifestation")));
			}
		}

		return manifestations;
	}

	default boolean setManifestations(ItemStack itemStack, Manifestation[] manifestations)
	{
		if (manifestations.length == 0)
		{
			return false;
		}

		CompoundTag nbt = itemStack.getOrCreateTag();
		ListTag manifestationListTag = new ListTag();
		for (Manifestation manifestation : manifestations)
		{

			CompoundTag tag = new CompoundTag();
			if (manifestation == null)
			{
				tag.putString("manifestation", "null");
			}
			else
			{
				tag.putString("manifestation", manifestation.getRegistryName().toString());
			}
			manifestationListTag.add(tag);
		}

		nbt.put("manifestationIds", manifestationListTag);
		return true;
	}

	default Integer[] getManifestationStrengths(ItemStack itemStack)
	{
		CompoundTag nbt = itemStack.getOrCreateTag();

		if (!nbt.contains("manifestationStrengths"))
		{
			return new Integer[getMaxCapacity()];
		}
		int[] strengths = nbt.getIntArray("manifestationStrengths");
		Integer[] newStrengths = new Integer[strengths.length];
		for (int i = 0; i < strengths.length; i++)
		{
			if (strengths[i] == 0)
			{
				newStrengths[i] = null;
			}
			else
			{
				newStrengths[i] = strengths[i];
			}
		}

		if (newStrengths.length == 0)
		{
			return new Integer[getMaxCapacity()];
		}
		return newStrengths;
	}

	default boolean setManifestationStrengths(ItemStack itemStack, Integer[] strengths)
	{
		if (strengths.length == 0)
		{
			return false;
		}

		CompoundTag nbt = itemStack.getOrCreateTag();
		int[] newStrengths = new int[getMaxCapacity()];
		for (int i = 0; i < strengths.length; i++)
		{
			if (strengths[i] == null)
			{
				newStrengths[i] = 0;
			}
			else
			{
				newStrengths[i] = strengths[i];
			}
		}

		nbt.putIntArray("manifestationStrengths", newStrengths);
		return true;
	}

	default boolean trySetAttunedPlayer(ItemStack itemStack, Player entity)
	{
		if (itemStack.getItem() instanceof IHasMetalType metalType && metalType.getMetalType() == Metals.MetalType.NICROSIL)
		{
			UUID attunedPlayerID = getAttunedPlayer(itemStack);
			UUID playerID = entity.getUUID();
			boolean noAttunedPlayer = attunedPlayerID == null;

			if (noAttunedPlayer)
			{
				boolean isStoringIdentity = false;
				{
					Optional<ISpiritweb> data = SpiritwebCapability.get(entity).filter(obj -> true);
					if (data.isPresent()) {
						isStoringIdentity = Manifestations.ManifestationTypes.FERUCHEMY.getManifestation(Metals.MetalType.ALUMINUM.getID()).getMode(data.get()) > 0;
					}
				}

				if (isStoringIdentity)
				{
					// Then set the metalmind to "unsealed". Any feruchemist with access to that power can use the metalmind
					StackNBTHelper.setUuid(itemStack, Constants.NBT.ATTUNED_PLAYER, Constants.NBT.UNKEYED_UUID);
					StackNBTHelper.setString(itemStack, Constants.NBT.ATTUNED_PLAYER_NAME, "Unkeyed"); // todo translation
					return true;

				}
			}

			if (noAttunedPlayer || attunedPlayerID.compareTo(playerID) == 0 || attunedPlayerID.compareTo(Constants.NBT.UNKEYED_UUID) == 0)
			{
				if (noAttunedPlayer && getManifestations(itemStack).length > 0)
				{
					setAttunedPlayer(itemStack, entity);
					setAttunedPlayerName(itemStack, entity);
				}
				return true;
			}

		}
		return false;
	}

	default void setAttunedPlayer(ItemStack itemStack, Player entity)
	{
		StackNBTHelper.setUuid(itemStack, Constants.NBT.ATTUNED_PLAYER, entity.getUUID());
	}

	default UUID getAttunedPlayer(ItemStack itemStack)
	{
		return StackNBTHelper.getUuid(itemStack, Constants.NBT.ATTUNED_PLAYER);
	}

	default void setAttunedPlayerName(ItemStack itemStack, Player entity)
	{
		String playerName = entity.getDisplayName().getString();
		StackNBTHelper.setString(itemStack, Constants.NBT.ATTUNED_PLAYER_NAME, playerName);
	}

	default String getAttunedPlayerName(ItemStack itemStack)
	{
		return StackNBTHelper.getString(itemStack, Constants.NBT.ATTUNED_PLAYER_NAME, "");
	}

	default boolean getPlayerIsAttuned(ItemStack itemStack, Player entity)
	{
		//todo clean up
		final MobEffect aluminumStoreEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("feruchemy", "storing_" + Metals.MetalType.ALUMINUM.getName()));
		assert aluminumStoreEffect != null;
		MobEffectInstance storingIdentityEffect = entity.getEffect(aluminumStoreEffect);
		boolean noIdentityPlayer = storingIdentityEffect != null && storingIdentityEffect.getDuration() > 0;

		UUID itemAttunedPlayerUUID = getAttunedPlayer(itemStack);
		//null means not attuned at all, so can assume player is attuned with it
		UUID playerUUID = entity.getUUID();
		return noIdentityPlayer || itemAttunedPlayerUUID == null || itemAttunedPlayerUUID.equals(playerUUID);
	}
}
