package leaf.cosmere.api;

import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface IHasManifestations
{
	int getMaxCapacity();

	default void addManifestation(ItemStack itemStack, Manifestation manifestation, int strength, byte manifestationSlot)
	{
		if(manifestation == null) return;
		Manifestation[] manifestations = getManifestations(itemStack);
		Integer[] manifestationStrengths = getManifestationStrengths(itemStack);

		if(manifestations == null || manifestations.length == 0)
		{
			manifestations = new Manifestation[getMaxCapacity()];
			manifestationStrengths = new Integer[getMaxCapacity()];
		}

		manifestations[manifestationSlot] = manifestation;
		manifestationStrengths[manifestationSlot] = strength;
		setManifestations(itemStack, manifestations);
		setManifestationStrengths(itemStack, manifestationStrengths);
	}

	default void removeManifestation(ItemStack itemStack, Manifestation manifestation)
	{
		if(manifestation == null) return;
		Manifestation[] manifestations = getManifestations(itemStack);
		Integer[] manifestationStrengths = getManifestationStrengths(itemStack);

		for(int i = 0; i < manifestations.length; i++)
		{
			if(manifestations[i] == manifestation)
			{
				manifestations[i] = null;
				manifestationStrengths[i] = null;
				setManifestations(itemStack, manifestations);
				setManifestationStrengths(itemStack, manifestationStrengths);
				return;
			}
		}
	}

	default Manifestation[] getManifestations(ItemStack itemStack)
	{
		CompoundTag nbt = itemStack.getOrCreateTag();
		Manifestation[] manifestations;
		if(!nbt.contains("manifestationIds")) return new Manifestation[getMaxCapacity()];


		ListTag manifestationListTag = (ListTag) nbt.get("manifestationIds");

		manifestations = new Manifestation[manifestationListTag.size()];
		for(int i = 0; i < manifestationListTag.size(); i++)
		{
			CompoundTag tag  = (CompoundTag) manifestationListTag.get(i);
			if(tag.getString("manifestation").equals("null"))
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
		if(manifestations.length == 0) return false;

		CompoundTag nbt = itemStack.getOrCreateTag();
		ListTag manifestationListTag = new ListTag();
		for(Manifestation manifestation : manifestations)
		{

			CompoundTag tag = new CompoundTag();
			if(manifestation == null)
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

		if(!nbt.contains("manifestationStrengths")) return new Integer[getMaxCapacity()];
		int[] strengths = nbt.getIntArray("manifestationStrengths");
		Integer[] newStrengths = new Integer[strengths.length];
		for(int i = 0; i < strengths.length; i++){
			if (strengths[i] == 0)
			{
				newStrengths[i] = null;
			}
			else
			{
				newStrengths[i] = strengths[i];
			}
		}

		if(newStrengths.length == 0) return new Integer[getMaxCapacity()];
		return newStrengths;
	}

	default boolean setManifestationStrengths(ItemStack itemStack, Integer[] strengths)
	{
		if(strengths.length == 0) return false;

		CompoundTag nbt = itemStack.getOrCreateTag();
		int[] newStrengths = new int[getMaxCapacity()];
		for(int i = 0; i < strengths.length; i++)
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
}
