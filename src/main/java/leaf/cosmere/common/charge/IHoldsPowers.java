package leaf.cosmere.common.charge;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.registry.CosmereEffectsRegistry;
import leaf.cosmere.common.util.CosmereAttributeUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public interface IHoldsPowers
{
	int getMaxCapacity();

	default void addPower(ItemStack itemStack, Attribute attribute, int strength, byte attributeSlot)
	{
		if (attribute == null)
		{
			return;
		}
		Attribute[] attributes = getAttributes(itemStack);
		Integer[] attributeStrengths = getAttributeStrengths(itemStack);

		if (attributes == null || attributes.length == 0)
		{
			attributes = new Attribute[getMaxCapacity()];
			attributeStrengths = new Integer[getMaxCapacity()];
		}

		int newStrength = strength;
		if (attributes[attributeSlot] != null && attributeStrengths[attributeSlot] != null)
		{
			newStrength += attributeStrengths[attributeSlot];
		}

		attributes[attributeSlot] = attribute;
		attributeStrengths[attributeSlot] = newStrength;
		setAttributes(itemStack, attributes);
		setAttributeStrengths(itemStack, attributeStrengths);
	}

	default void removePower(ItemStack itemStack, Attribute attribute)
	{
		if (attribute == null)
		{
			return;
		}
		Attribute[] attributes = getAttributes(itemStack);
		Integer[] attributeStrengths = getAttributeStrengths(itemStack);

		for (int i = 0; i < attributes.length; i++)
		{
			if (attributes[i] == attribute)
			{
				attributes[i] = null;
				attributeStrengths[i] = null;
				setAttributes(itemStack, attributes);
				setAttributeStrengths(itemStack, attributeStrengths);
				break;
			}
		}

		for (Attribute att : attributes)
		{
			if (att != null)
			{
				return;
			}
		}
		//if no powers left, clear attuned player
		StackNBTHelper.removeEntry(itemStack, Constants.NBT.ATTUNED_PLAYER);
		StackNBTHelper.removeEntry(itemStack, Constants.NBT.ATTUNED_PLAYER_NAME);
	}

	default Attribute[] getAttributes(ItemStack itemStack)
	{
		CompoundTag nbt = itemStack.getOrCreateTag();
		Attribute[] attributes;
		if (!nbt.contains("attributeIds"))
		{
			return new Attribute[getMaxCapacity()];
		}


		ListTag attributeListTag = (ListTag) nbt.get("attributeIds");

		attributes = new Attribute[attributeListTag.size()];
		for (int i = 0; i < attributeListTag.size(); i++)
		{
			CompoundTag tag = (CompoundTag) attributeListTag.get(i);
			if (tag.getString("attribute").equals("null"))
			{
				attributes[i] = null;
			}
			else
			{
				attributes[i] = CosmereAttributeUtils.getAttributeByDescriptionId(tag.getString("attribute"));
			}
		}

		return attributes;
	}

	default boolean setAttributes(ItemStack itemStack, Attribute[] attributes)
	{
		if (attributes.length == 0)
		{
			return false;
		}

		CompoundTag nbt = itemStack.getOrCreateTag();
		ListTag attributesListTag = new ListTag();
		for (Attribute attribute : attributes)
		{

			CompoundTag tag = new CompoundTag();
			if (attribute == null)
			{
				tag.putString("attribute", "null");
			}
			else
			{
				tag.putString("attribute", attribute.getDescriptionId());
			}
			attributesListTag.add(tag);
		}

		nbt.put("attributeIds", attributesListTag);
		return true;
	}

	default Integer[] getAttributeStrengths(ItemStack itemStack)
	{
		CompoundTag nbt = itemStack.getOrCreateTag();

		if (!nbt.contains("attributeStrengths"))
		{
			return new Integer[getMaxCapacity()];
		}
		int[] strengths = nbt.getIntArray("attributeStrengths");
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

	default boolean setAttributeStrengths(ItemStack itemStack, Integer[] strengths)
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

		nbt.putIntArray("attributeStrengths", newStrengths);
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
				ISpiritweb spiritweb = SpiritwebCapability.get(entity).resolve().get();
				CosmereEffect aluminumEffect = CosmereEffectsRegistry.fromID(new ResourceLocation("feruchemy", "storing_" + Metals.MetalType.ALUMINUM.getName()));
				if (spiritweb.hasEffect(aluminumEffect))
				{
					// Then set the metalmind to "unsealed". Any feruchemist with access to that power can use the metalmind
					StackNBTHelper.setUuid(itemStack, Constants.NBT.ATTUNED_PLAYER, Constants.NBT.UNKEYED_UUID);
					StackNBTHelper.setString(itemStack, Constants.NBT.ATTUNED_PLAYER_NAME, "Unkeyed"); // todo translation
					return true;
				}
			}

			if (noAttunedPlayer || attunedPlayerID.equals(playerID) || attunedPlayerID.equals(Constants.NBT.UNKEYED_UUID))
			{
				if (noAttunedPlayer && getAttributes(itemStack).length > 0)
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
		if (SpiritwebCapability.get(entity).resolve().isPresent())
		{
			ISpiritweb spiritweb = SpiritwebCapability.get(entity).resolve().get();
			CosmereEffect aluminumEffect = CosmereEffectsRegistry.fromID(new ResourceLocation("feruchemy", "storing_" + Metals.MetalType.ALUMINUM.getName()));
			boolean noIdentityPlayer = spiritweb.hasEffect(aluminumEffect);

			UUID itemAttunedPlayerUUID = getAttunedPlayer(itemStack);
			UUID playerUUID = entity.getUUID();
			boolean noIdentityItem = itemAttunedPlayerUUID == null || itemAttunedPlayerUUID.equals(Constants.NBT.UNKEYED_UUID);

			return noIdentityPlayer || noIdentityItem || itemAttunedPlayerUUID.equals(playerUUID);
		}
		return false;
	}
}
