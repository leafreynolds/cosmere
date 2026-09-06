/*
 * File created ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.items;

import com.google.common.collect.Multimap;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

//ICurio for data-map spikes. Curios wraps ICurioItem items itself
public class SpikeCurio implements ICurio
{
	private final ItemStack stack;
	private final IHemalurgicInfo spike;

	public SpikeCurio(ItemStack stack, IHemalurgicInfo spike)
	{
		this.stack = stack;
		this.spike = spike;
	}

	@Override
	public ItemStack getStack()
	{
		return stack;
	}

	@Override
	public boolean canEquip(SlotContext slotContext)
	{
		return SpikeCurioLogic.canEquip(spike, slotContext, stack);
	}

	@Override
	public boolean canUnequip(SlotContext slotContext)
	{
		return SpikeCurioLogic.canUnequip(slotContext, stack);
	}

	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack)
	{
		SpikeCurioLogic.onEquip(slotContext, prevStack, stack);
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack)
	{
		//nicrosil strips granted powers on unequip
		if (spike.getSpikeMetalType(stack) == Metals.MetalType.NICROSIL)
		{
			SpiritwebCapability.get(slotContext.entity()).ifPresent(data ->
			{
				for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
				{
					Holder<Attribute> attribute = manifestation.getAttribute();
					if (attribute == null)
					{
						continue;
					}

					final AttributeInstance attributeInstance = data.getLiving().getAttribute(attribute);
					if (attributeInstance != null)
					{
						attributeInstance.removeModifier(ResourceLocation.fromNamespaceAndPath("cosmere", "feru_nicrosil_" + Constants.NBT.FERU_NICROSIL_UUID));
					}
				}
			});
		}
		SpikeCurioLogic.onUnequip(slotContext, newStack, stack);
	}

	@Override
	public boolean makesPiglinsNeutral(SlotContext slotContext)
	{
		return spike.getSpikeMetalType(stack) == Metals.MetalType.GOLD;
	}

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id)
	{
		return SpikeCurioLogic.getAttributeModifiers(spike, slotContext, stack);
	}
}
