/*
 * File updated ~ 3 - 4 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common.items;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.tools.common.CosmereTools;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

// we use the DyeableLeatherItem interface to get free tinting,
// and then redirect requests for the overlay to a blank texture so it can't do anything with it
public class TArmorItem extends ArmorItem implements IHasMetalType, DyeableLeatherItem
{
	Metals.MetalType metalType;

	public TArmorItem(Metals.MetalType metalType, Type pSlot, Properties pProperties)
	{
		super(metalType, pSlot, pProperties);
		this.metalType = metalType;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return metalType;
	}

	@Override
	public boolean hasCustomColor(ItemStack pStack)
	{
		return true;
	}

	@Override
	public int getColor(ItemStack pStack)
	{
		return metalType.getColorValue();
	}

	@Override
	public void clearColor(ItemStack pStack)
	{
	}

	@Override
	public void setColor(ItemStack pStack, int pColor)
	{
	}

	@Nonnull
	@Override
	public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
	{
		return type != null && type.contains("overlay")
		       ? CosmereTools.MODID + ":" + "textures/models/armor/armor_overlay.png"//blank texture
		       : (CosmereTools.MODID + ":" + "textures/models/armor/armor_layer_%s.png")//following minecraft style,
				       .formatted(
						       slot == EquipmentSlot.LEGS ? 2 : 1);//where leggings are separate from the other pieces
	}
}
