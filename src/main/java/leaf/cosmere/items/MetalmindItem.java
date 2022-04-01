/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.properties.PropTypes;
import leaf.cosmere.utils.helpers.CompoundNBTHelper;
import leaf.cosmere.items.curio.IHemalurgicInfo;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class MetalmindItem extends ChargeableItemBase implements IHasMetalType, ICurioItem
{
    private final Metals.MetalType metalType;

    public MetalmindItem(Metals.MetalType metalType, ItemGroup group)
    {
        super(PropTypes.Items.ONE.get().rarity(metalType.getRarity()).tab(group));
        this.metalType = metalType;
    }

    @Override
    public Metals.MetalType getMetalType()
    {
        return this.metalType;
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
        if (metalType == Metals.MetalType.NICROSIL)
        {
            CompoundNBT nbt = stack.getOrCreateTagElement("StoredInvestiture");
            //for each power the user has access to

            for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
            {
                String manifestationName = manifestation.getRegistryName().getPath();
                if (!AttributesRegistry.COSMERE_ATTRIBUTES.containsKey(manifestationName))
                {
                    continue;
                }

                if (nbt.getBoolean(manifestationName))
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

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack)
    {
        //todo better logic.
        boolean isEquipping = prevStack == null || stack.getItem() != prevStack.getItem();
        onEquipStatusChanged(slotContext, stack, isEquipping);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack prevStack, ItemStack stack)
    {
        onEquipStatusChanged(slotContext, stack, false);
    }

    protected void onEquipStatusChanged(SlotContext slotContext, ItemStack stack, boolean isEquipping)
    {
    }
}
