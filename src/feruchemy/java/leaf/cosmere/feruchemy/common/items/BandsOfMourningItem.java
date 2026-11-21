/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.items;

import com.google.common.collect.Multimap;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.feruchemy.common.Feruchemy;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class BandsOfMourningItem extends BraceletMetalmindItem
{
	private static final String STORED_INVESTITURE = "StoredInvestiture";

	public BandsOfMourningItem()
	{
		super(Metals.MetalType.HARMONIUM);
	}

	@Override
	public void addFilled(CreativeModeTab.Output output)
	{
		ItemStack fullPower = new ItemStack(this);
		setCharge(fullPower, getMaxCharge(fullPower));

		CompoundTag nbt = StackNBTHelper.getCompound(fullPower, STORED_INVESTITURE, false);

		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			final String attributeRegistryName = manifestation.getRegistryName().toString();

			if (manifestation instanceof IHasMetalType)
			{
				nbt.putDouble(attributeRegistryName, 20);
			}
		}

		StackNBTHelper.setCompound(fullPower, STORED_INVESTITURE, nbt);

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
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation location, ItemStack stack)
	{
		Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers = super.getAttributeModifiers(slotContext, location, stack);
		CompoundTag nbt = StackNBTHelper.getCompound(stack, STORED_INVESTITURE, false);

		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			Holder<Attribute> attribute = manifestation.getAttribute();
			final ResourceLocation manifestationName = manifestation.getRegistryName();
			final String attributeRegistryName = manifestationName.toString();
			if (!CompoundNBTHelper.verifyExistance(nbt, attributeRegistryName) || attribute == null)
			{
				continue;
			}

			attributeModifiers.put(
					attribute,
					new AttributeModifier(
							//modifier ids are resource locations now
							// so a manifestation name isn't a legal path on its own
							Feruchemy.rl("bands_of_mourning/" + manifestationName.getNamespace() + "/" + manifestationName.getPath()),
							CompoundNBTHelper.getDouble(
									nbt,
									attributeRegistryName,
									0),
							AttributeModifier.Operation.ADD_VALUE));

		}

		return attributeModifiers;
	}
}
