/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items.curio;

import com.google.common.collect.Multimap;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.CompoundNBTHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
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
		if (allowdedIn(tab))
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
					nbt.putDouble(metalType.get().getAllomancyRegistryName(), 20);
					nbt.putDouble(metalType.get().getFeruchemyRegistryName(), 20);
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

		for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			String manifestationName = manifestation.getName();
			RegistryObject<Attribute> attributeRegistryObject = manifestation.getAttribute();
			if (!CompoundNBTHelper.verifyExistance(nbt, manifestationName) || attributeRegistryObject == null)
			{
				continue;
			}

			attributeModifiers.put(
					attributeRegistryObject.get(),
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
