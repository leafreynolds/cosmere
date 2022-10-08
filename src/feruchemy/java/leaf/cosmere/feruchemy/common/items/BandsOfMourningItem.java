/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.items;

import com.google.common.collect.Multimap;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class BandsOfMourningItem extends BraceletMetalmindItem
{
	public BandsOfMourningItem()
	{
		super(Metals.MetalType.HARMONIUM);
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> stacks)
	{
		if (allowedIn(tab))
		{
			ItemStack fullPower = new ItemStack(this);
			setCharge(fullPower, getMaxCharge(fullPower));

			CompoundTag nbt = fullPower.getOrCreateTagElement("StoredInvestiture");
			//for each power the user has access to
			for (int i = 0; i < 16; i++)
			{
				//even if it's granted from hemalurgy/temporary
				//update the nbt.
				//this will add/remove powers based on what the user currently has.
				//todo, come back to this later when more sleep. bugs me about losing potential stored powers
				final Optional<Metals.MetalType> metalType = Metals.MetalType.valueOf(i);
				if (metalType.isPresent())
				{
					nbt.putDouble("allomancy:" + metalType.get().getName(), 20);
					nbt.putDouble("feruchemy:" + metalType.get().getName(), 20);
				}
			}

			stacks.add(fullPower);
		}
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
			String manifestationName = manifestation.getName();
			Attribute attributeRegistryObject = manifestation.getAttribute();
			if (!CompoundNBTHelper.verifyExistance(nbt, manifestationName) || attributeRegistryObject == null)
			{
				continue;
			}

			attributeModifiers.put(
					attributeRegistryObject,
					new AttributeModifier(
							Constants.NBT.UNKEYED_UUID,
							manifestationName,
							CompoundNBTHelper.getDouble(
									nbt,
									manifestationName,
									0),
							AttributeModifier.Operation.ADDITION));

		}

		return attributeModifiers;
	}
}
