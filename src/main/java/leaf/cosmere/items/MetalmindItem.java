/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.curio.IHemalurgicInfo;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.properties.PropTypes;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.CompoundNBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class MetalmindItem extends ChargeableItemBase implements IHasMetalType, ICurioItem
{
	private final Metals.MetalType metalType;

	public MetalmindItem(Metals.MetalType metalType, CreativeModeTab group)
	{
		super(PropTypes.Items.ONE.get().rarity(metalType.getRarity()).tab(group));
		this.metalType = metalType;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}


	@Override
	public boolean showAttributesTooltip(String identifier, ItemStack stack)
	{
		return false;
	}

	/**
	 * generate new map of attributes for when used as a curio item.
	 */
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack)
	{
		Multimap<Attribute, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();

		Metals.MetalType metalType = getMetalType();
		if (stack.getItem() instanceof IHemalurgicInfo)
		{
			//add hemalurgic attributes, if any.
			((IHemalurgicInfo) (stack.getItem())).getHemalurgicAttributes(attributeModifiers, stack, metalType);
		}

		//todo better nicrosil tracking.
		if (metalType == Metals.MetalType.NICROSIL || metalType == Metals.MetalType.HARMONIUM)
		{
			CompoundTag nbt = stack.getOrCreateTagElement("StoredInvestiture");
			//for each power the user has access to

			for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
			{
				String manifestationName = manifestation.getName();
				if (!AttributesRegistry.COSMERE_ATTRIBUTES.containsKey(manifestationName))
				{
					continue;
				}

				if (CompoundNBTHelper.getDouble(nbt, manifestationName, 0) > 0)
				{
					UUID someUUID = UUID.nameUUIDFromBytes((manifestationName + uuid.toString()).getBytes());
					attributeModifiers.put(
							AttributesRegistry.COSMERE_ATTRIBUTES.get(manifestationName).get(),
							new AttributeModifier(
									someUUID,
									manifestationName,
									CompoundNBTHelper.getDouble(
											nbt,
											manifestationName,
											0),
									AttributeModifier.Operation.ADDITION));

				}
			}
		}

		return attributeModifiers;
	}
}
