/*
 * File updated ~ 11 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.items;

import com.google.common.collect.Multimap;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class BandsOfMourningItem extends BraceletMetalmindItem
{
	public BandsOfMourningItem()
	{
		super(Metals.MetalType.HARMONIUM);
	}

	@Override
	public void addFilled(CreativeModeTab.Output output)
	{
		ItemStack fullPower = new ItemStack(this);
		setCharge(fullPower, getMaxCharge(fullPower));

		CompoundTag nbt = fullPower.getOrCreateTagElement("StoredInvestiture");

		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			final String attributeRegistryName = manifestation.getRegistryName().toString();

			if (manifestation instanceof IHasMetalType)
			{
				nbt.putDouble(attributeRegistryName, 20);
			}
		}

		output.accept(fullPower);
	}

	@Override
	public float getMaxChargeModifier()
	{
		return 1;
	}

	@Override
	public int getMaxCharge(ItemStack stack)
	{
		return Integer.MAX_VALUE - 100;
	}


	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack)
	{
		Multimap<Attribute, AttributeModifier> attributeModifiers = super.getAttributeModifiers(slotContext, uuid, stack);
		CompoundTag nbt = stack.getOrCreateTagElement("StoredInvestiture");

		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			Attribute attribute = manifestation.getAttribute();
			final String attributeRegistryName = manifestation.getRegistryName().toString();
			if (!CompoundNBTHelper.verifyExistance(nbt, attributeRegistryName) || attribute == null)
			{
				continue;
			}

			attributeModifiers.put(
					attribute,
					new AttributeModifier(
							Constants.NBT.UNKEYED_UUID,
							attributeRegistryName,
							CompoundNBTHelper.getDouble(
									nbt,
									attributeRegistryName,
									0),
							AttributeModifier.Operation.ADDITION));

		}

		return attributeModifiers;
	}
}
