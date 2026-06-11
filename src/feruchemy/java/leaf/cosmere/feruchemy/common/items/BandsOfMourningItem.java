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
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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

		CompoundTag nbt = StackNBTHelper.getOrCreateCompoundTag(fullPower, "StoredInvestiture");

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
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack)
	{
		Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers = super.getAttributeModifiers(slotContext, uuid, stack);
		CompoundTag nbt = StackNBTHelper.getOrCreateCompoundTag(stack, "StoredInvestiture");

		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			Holder<Attribute> attribute = manifestation.getAttribute();
			final String attributeRegistryName = manifestation.getRegistryName().toString();
			if (!CompoundNBTHelper.verifyExistance(nbt, attributeRegistryName) || attribute == null)
			{
				continue;
			}

			attributeModifiers.put(
					attribute,
					new AttributeModifier(
							ResourceLocation.parse(attributeRegistryName),
							CompoundNBTHelper.getDouble(
									nbt,
									attributeRegistryName,
									0),
							AttributeModifier.Operation.ADD_VALUE));

		}

		return attributeModifiers;
	}
}
