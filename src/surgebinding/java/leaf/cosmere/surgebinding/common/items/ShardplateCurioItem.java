package leaf.cosmere.surgebinding.common.items;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.api.IHasColour;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardplateData;
import leaf.cosmere.surgebinding.common.items.tiers.ShardplateArmorMaterial;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.awt.*;
import java.util.UUID;

public class ShardplateCurioItem extends Item implements ICurioItem, IHasColour
{
	public static final Capability<DynamicShardplateData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>()
	{
	});

	ShardplateArmorMaterial material;
	private final int defense;
	private final float toughness;
	protected final float knockbackResistance;
	private final Multimap<Attribute, AttributeModifier> defaultModifiers;





	public ShardplateCurioItem(Properties properties)
	{
		this(ShardplateArmorMaterial.DEADPLATE, properties);
	}


	public ShardplateCurioItem(ShardplateArmorMaterial material, Properties properties)
	{
		super(properties);
		this.material = material;
		defense = material.getDefense();
		toughness = material.getToughness();
		knockbackResistance = material.getKnockbackResistance();

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ARMOR, new AttributeModifier("Armor modifier", (double)this.defense, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("Armor toughness", (double)this.toughness, AttributeModifier.Operation.ADDITION));
		if (this.knockbackResistance > 0.0F) {
			builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier("Armor knockback resistance", (double)this.knockbackResistance, AttributeModifier.Operation.ADDITION));
		}

		this.defaultModifiers = builder.build();
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack)
	{
		ICurioItem.super.curioTick(slotContext, stack);

	}

	@Override
	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
	{
		final DynamicShardplateData dynamicShardplateData = new DynamicShardplateData();

		if (nbt != null)
		{
			dynamicShardplateData.deserializeNBT(nbt); // todo check if this breaks things?
		}

		return dynamicShardplateData;
	}

	public boolean isLiving()
	{
		return false;
	}


	@Override
	public Color getColour()
	{
		return Roshar.getDeadplate();
	}


	public int getEnchantmentValue() {
		return this.material.getEnchantmentValue();
	}

	public ShardplateArmorMaterial getMaterial() {
		return this.material;
	}

	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers() {
		return this.defaultModifiers;
	}

	public int getDefense() {
		return this.defense;
	}

	public float getToughness() {
		return this.toughness;
	}

	public SoundEvent getEquipSound() {
		return this.getMaterial().getEquipSound();
	}
}