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
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IHoldsPowers
{
	int getMaxCapacity();

	default void addPower(ItemStack itemStack, Holder<Attribute> attribute, int strength, int attributeSlot)
	{
		if (attribute == null)
		{
			return;
		}
		List<Holder<Attribute>> attributes = getAttributes(itemStack);
		Integer[] attributeStrengths = getAttributeStrengths(itemStack);

		if (attributes == null || attributes.size() == 0)
		{
			attributes = new ArrayList<>(getMaxCapacity());
			attributeStrengths = new Integer[getMaxCapacity()];
		}

		int newStrength = strength;
		if (attributes.get(attributeSlot) != null && attributeStrengths[attributeSlot] != null)
		{
			newStrength += attributeStrengths[attributeSlot];
		}

		attributes.set(attributeSlot, attribute);
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
		List<Holder<Attribute>> attributes = getAttributes(itemStack);
		Integer[] attributeStrengths = getAttributeStrengths(itemStack);

		for (int i = 0; i < attributes.size(); i++)
		{
			if (attributes.get(i) == attribute)
			{
				attributes.set(i, null);
				attributeStrengths[i] = null;
				setAttributes(itemStack, attributes);
				setAttributeStrengths(itemStack, attributeStrengths);
				break;
			}
		}

		for (Holder<Attribute> att : attributes)
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

	default List<Holder<Attribute>> getAttributes(ItemStack itemStack)
	{
		CompoundTag nbt = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		List<Holder<Attribute>> attributes;
		if (!nbt.contains("attributeIds"))
		{
			return new ArrayList<>();
		}


		ListTag attributeListTag = (ListTag) nbt.get("attributeIds");

		attributes = new ArrayList<>();
		for (int i = 0; i < attributeListTag.size(); i++)
		{
			CompoundTag tag = (CompoundTag) attributeListTag.get(i);
			if (tag.getString("attribute").equals("null"))
			{
				attributes.set(i, null);
			}
			else
			{
				attributes.set(i, CosmereAttributeUtils.getAttributeByDescriptionId(tag.getString("attribute")));
			}
		}

		return attributes;
	}

	default boolean setAttributes(ItemStack itemStack, List<Holder<Attribute>> attributes)
	{
		if (attributes.size() == 0)
		{
			return false;
		}

		CompoundTag nbt = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		ListTag attributesListTag = new ListTag();
		for (Holder<Attribute> attribute : attributes)
		{
			CompoundTag tag = new CompoundTag();
			if (attribute == null)
			{
				tag.putString("attribute", "null");
			}
			else
			{
				tag.putString("attribute", attribute.getRegisteredName());
			}
			attributesListTag.add(tag);
		}

		nbt.put("attributeIds", attributesListTag);
		return true;
	}

	default Integer[] getAttributeStrengths(ItemStack itemStack)
	{
		CompoundTag nbt = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

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

		CompoundTag nbt = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
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
				final Optional<ISpiritweb> spiritwebOpt = SpiritwebCapability.get(entity);

				if (spiritwebOpt.isPresent())
				{
					ISpiritweb spiritweb = spiritwebOpt.get();
					CosmereEffect aluminumEffect = CosmereEffectsRegistry.fromID(ResourceLocation.fromNamespaceAndPath("feruchemy", "storing_" + Metals.MetalType.ALUMINUM.getName())).value();
					if (spiritweb.hasEffect(aluminumEffect))
					{
						// Then set the metalmind to "unsealed". Any feruchemist with access to that power can use the metalmind
						StackNBTHelper.setUuid(itemStack, Constants.NBT.ATTUNED_PLAYER, Constants.NBT.UNKEYED_UUID);
						StackNBTHelper.setString(itemStack, Constants.NBT.ATTUNED_PLAYER_NAME, "Unkeyed"); // todo translation
						return true;
					}
				}
			}

			if (noAttunedPlayer || attunedPlayerID.equals(playerID) || attunedPlayerID.equals(Constants.NBT.UNKEYED_UUID))
			{
				if (noAttunedPlayer && getAttributes(itemStack).size() > 0)
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
		if (SpiritwebCapability.get(entity).isPresent())
		{
			ISpiritweb spiritweb = SpiritwebCapability.get(entity).get();
			CosmereEffect aluminumEffect = CosmereEffectsRegistry.fromID(ResourceLocation.fromNamespaceAndPath("feruchemy", "storing_" + Metals.MetalType.ALUMINUM.getName())).value();
			boolean noIdentityPlayer = spiritweb.hasEffect(aluminumEffect);

			UUID itemAttunedPlayerUUID = getAttunedPlayer(itemStack);
			UUID playerUUID = entity.getUUID();
			boolean noIdentityItem = itemAttunedPlayerUUID == null || itemAttunedPlayerUUID.equals(Constants.NBT.UNKEYED_UUID);

			return noIdentityPlayer || noIdentityItem || itemAttunedPlayerUUID.equals(playerUUID);
		}
		return false;
	}
}
