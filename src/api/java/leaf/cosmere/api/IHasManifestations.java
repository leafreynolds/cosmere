package leaf.cosmere.api;

import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import leaf.cosmere.api.Manifestations.ManifestationTypes;


import java.util.ArrayList;

public interface IHasManifestations
{
	int getMaxCapacity();

	default ManifestationTypes[] getManifestations(ItemStack itemStack)
	{
		CompoundTag nbt = itemStack.getOrCreateTag();
		ManifestationTypes[] manifestationTypes;

		if(!nbt.contains("manifestationIds")) return new ManifestationTypes[0];

		int[] manifestationIds = nbt.getIntArray("manifestationIds");
		manifestationTypes = new ManifestationTypes[manifestationIds.length];
		for(int i = 0; i < manifestationIds.length; i++)
		{
			if(ManifestationTypes.valueOf(i).isPresent()) manifestationTypes[i] = ManifestationTypes.valueOf(i).get();
		}

		return manifestationTypes;
	}

	default boolean setManifestations(ItemStack itemStack, ArrayList<Manifestation> manifestations)
	{
		if(manifestations.isEmpty()) return false;

		CompoundTag nbt = itemStack.getOrCreateTag();
		ArrayList<Integer> manifestationIds = new ArrayList<>();
		manifestations.forEach(manifestation -> manifestationIds.add(manifestation.getManifestationType().getID()));

		nbt.putIntArray("manifestationIds", manifestationIds);
		return true;
	}

	default Integer[] getManifestationStrengths(ItemStack itemStack)
	{
		CompoundTag nbt = itemStack.getOrCreateTag();

		if(!nbt.contains("manifestationStrengths")) return new Integer[0];
		int[] strengths = nbt.getIntArray("manifestationStrengths");
		Integer[] newStrengths = new Integer[strengths.length];
		for(int i = 0; i < strengths.length; i++) newStrengths[i] = strengths[i];

		return newStrengths;
	}

	default boolean setManifestationStrengths(ItemStack itemStack, ArrayList<Integer> strengths)
	{
		if(strengths.isEmpty()) return false;

		CompoundTag nbt = itemStack.getOrCreateTag();
		ArrayList<Integer> manifestationStrengths = new ArrayList<>();

		nbt.putIntArray("manifestationStrengths", manifestationStrengths);
		return true;
	}
}
