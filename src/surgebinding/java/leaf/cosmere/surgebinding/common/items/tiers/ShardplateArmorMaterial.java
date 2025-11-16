/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items.tiers;

import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ShardplateArmorMaterial implements ArmorMaterial
{
	DEADPLATE("deadplate", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, () ->
	{
		return Ingredient.of(Items.DIAMOND);
	}),
	LIVINGPLATE("livingplate", 35, new int[]{3, 6, 8, 3}, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, () ->
	{
		return Ingredient.of(Items.NETHERITE_INGOT);
	});

	private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
	private final String name;
	private final int durabilityMultiplier;
	private final int[] slotProtections;
	private final int enchantmentValue;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	ShardplateArmorMaterial(String pName, int pDurabilityMultiplier, int[] pSlotProtections, int pEnchantmentValue, SoundEvent pSound, float pToughness, float pKnockbackResistance, Supplier<Ingredient> pRepairIngredient)
	{
		this.name = pName;
		this.durabilityMultiplier = pDurabilityMultiplier;
		this.slotProtections = pSlotProtections;
		this.enchantmentValue = pEnchantmentValue;
		this.sound = pSound;
		this.toughness = pToughness;
		this.knockbackResistance = pKnockbackResistance;
		this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
	}

	public int getDurabilityForType(ArmorItem.Type pType)
	{
		return HEALTH_PER_SLOT[pType.getSlot().getIndex()] * this.durabilityMultiplier;
	}

	public int getDefenseForType(ArmorItem.Type pType)
	{
		return this.slotProtections[pType.getSlot().getIndex()];
	}

	public int getEnchantmentValue()
	{
		return this.enchantmentValue;
	}

	public SoundEvent getEquipSound()
	{
		return this.sound;
	}

	public Ingredient getRepairIngredient()
	{
		return this.repairIngredient.get();
	}

	public String getName()
	{
		return Surgebinding.MODID + ":" + this.name;
	}

	public float getToughness()
	{
		return this.toughness;
	}

	/**
	 * Gets the percentage of knockback resistance provided by armor of the material.
	 */
	public float getKnockbackResistance()
	{
		return this.knockbackResistance;
	}

}