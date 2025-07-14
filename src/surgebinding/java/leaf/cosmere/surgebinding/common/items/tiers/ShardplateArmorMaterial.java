
/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 * File updated ~ 12 - 7 - 2025 ~ Soar
 */

package leaf.cosmere.surgebinding.common.items.tiers;

import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ShardplateArmorMaterial
{
	//enchantmentValue changed to 0 because investiture resists other investiture.
	DEADPLATE("deadplate", 40, 22, 0, SoundEvents.ARMOR_EQUIP_DIAMOND, 4.0F, 0.4F, () ->
	{
		return null;
	}),
	LIVINGPLATE("livingplate", 45, 22, 0, SoundEvents.ARMOR_EQUIP_NETHERITE, 4.0F, 0.4F, () ->
	{
		return null;
	});

	private static final int HEALTH_PER_SLOT = 55;
	private final String name;
	private final int durabilityMultiplier;
	private final int slotProtection;
	private final int enchantmentValue;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	ShardplateArmorMaterial(String pName, int pDurabilityMultiplier, int pSlotProtection , int pEnchantmentValue, SoundEvent pSound, float pToughness, float pKnockbackResistance, Supplier<Ingredient> pRepairIngredient)
	{
		this.name = pName;
		this.durabilityMultiplier = pDurabilityMultiplier;
		this.slotProtection = pSlotProtection;
		this.enchantmentValue = pEnchantmentValue;
		this.sound = pSound;
		this.toughness = pToughness;
		this.knockbackResistance = pKnockbackResistance;
		this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
	}

	public int getDurability()
	{
		return HEALTH_PER_SLOT * this.durabilityMultiplier;
	}

	public int getDefense()
	{
		return this.slotProtection;
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
