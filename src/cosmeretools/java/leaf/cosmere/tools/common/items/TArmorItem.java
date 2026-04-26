/*
 * File updated ~ 28 - 3 - 2026 ~ Leaf
 */

package leaf.cosmere.tools.common.items;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.tools.common.CosmereTools;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

// we use the DyeableLeatherItem interface to get free tinting,
// and then redirect requests for the overlay to a blank texture so it can't do anything with it
public class TArmorItem extends ArmorItem implements IHasMetalType, DyeableLeatherItem
{
	Metals.MetalType metalType;

	public TArmorItem(Metals.MetalType metalType, Type pSlot, Properties pProperties)
	{
		super(buildArmorMaterial(metalType), pSlot, pProperties.durability(metalType.getDurabilityForType(pSlot)));
		this.metalType = metalType;
	}

	private static Holder<ArmorMaterial> buildArmorMaterial(Metals.MetalType metalType)
	{
		return Holder.direct(new ArmorMaterial(
				Map.of(
						ArmorItem.Type.BOOTS, metalType.getDefenseForType(ArmorItem.Type.BOOTS),
						ArmorItem.Type.LEGGINGS, metalType.getDefenseForType(ArmorItem.Type.LEGGINGS),
						ArmorItem.Type.CHESTPLATE, metalType.getDefenseForType(ArmorItem.Type.CHESTPLATE),
						ArmorItem.Type.HELMET, metalType.getDefenseForType(ArmorItem.Type.HELMET),
						ArmorItem.Type.BODY, 0
				),
				metalType.getEnchantmentValue(),
				metalType.getEquipSound(),
				metalType::getRepairIngredient,
				List.of(
						new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(CosmereTools.MODID, metalType.getName())),
						new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(CosmereTools.MODID, metalType.getName()), "_overlay", true)
				),
				metalType.getToughness(),
				metalType.getKnockbackResistance()
		));
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

	@Override
	public int getEnchantmentValue(ItemStack stack)
	{
		return getEnchantmentValue();
	}

	@Override
	public int getEnchantmentValue()
	{
		return metalType.getEnchantmentValue();
	}

	@Override
	public boolean isEnchantable(ItemStack pStack)
	{
		return true;
	}

	@Override
	public boolean isValidRepairItem(ItemStack pStack, ItemStack pRepairCandidate)
	{
		return metalType.getRepairIngredient().test(pRepairCandidate);
	}

	@Nonnull
	@Override
	public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel)
	{
		return !layer.suffix().isEmpty()
		       ? ResourceLocation.fromNamespaceAndPath(CosmereTools.MODID, "textures/models/armor/armor_overlay.png")
		       : ResourceLocation.fromNamespaceAndPath(CosmereTools.MODID, "textures/models/armor/armor_layer_" + (innerModel ? 2 : 1) + ".png");
	}
}
