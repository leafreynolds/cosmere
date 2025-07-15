/*
 * File updated ~ 14 - 7 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.api.Constants;
import leaf.cosmere.api.IHasColour;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardplateData;
import leaf.cosmere.surgebinding.common.items.tiers.ShardplateArmorMaterial;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.awt.*;
import java.util.List;

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
		builder.put(Attributes.ARMOR, new AttributeModifier("Armor modifier", (double) this.defense, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("Armor toughness", (double) this.toughness, AttributeModifier.Operation.ADDITION));
		if (this.knockbackResistance > 0.0F)
		{
			builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier("Armor knockback resistance", (double) this.knockbackResistance, AttributeModifier.Operation.ADDITION));
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


	public int getEnchantmentValue()
	{
		return this.material.getEnchantmentValue();
	}

	public ShardplateArmorMaterial getMaterial()
	{
		return this.material;
	}

	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers()
	{
		return this.defaultModifiers;
	}

	public int getDefense()
	{
		return this.defense;
	}

	public float getToughness()
	{
		return this.toughness;
	}

	public SoundEvent getEquipSound()
	{
		return this.getMaterial().getEquipSound();
	}


	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{
		if (!InventoryScreen.hasShiftDown())
		{
			pTooltipComponents.add(Constants.Translations.TOOLTIP_HOLD_SHIFT);
			return;
		}

		final DynamicShardplateData data = pStack.getCapability(ShardplateCurioItem.CAPABILITY).resolve().get();
		pTooltipComponents.add(TextHelper.createText(String.format("Faceplate: %s", data.getFaceplateID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Head: %s", data.getHeadID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Body: %s", data.getBodyID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Kama: %s", data.getKamaID())));

		pTooltipComponents.add(TextHelper.createText(String.format("Left Arm: %s", data.getLeftArmID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Left Paldron: %s", data.getLeftPaldronsID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Left Leg: %s", data.getLeftLegID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Left Boot Outside: %s", data.getLeftBootOutsideID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Left Boot Tip: %s", data.getLeftBootTipID())));

		pTooltipComponents.add(TextHelper.createText(String.format("Right Arm: %s", data.getRightArmID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Right Paldron: %s", data.getRightPaldronsID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Right Leg: %s", data.getRightLegID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Right Boot Outside: %s", data.getRightBootOutsideID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Right Boot Tip: %s", data.getRightBootTipID())));
	}

	@Override
	public @Nullable CompoundTag getShareTag(@NotNull ItemStack stack)
	{
		final DynamicShardplateData data = stack.getCapability(ShardplateCurioItem.CAPABILITY).resolve().get();
		CompoundTag tag = stack.getOrCreateTag();

		tag.put("shard_data", data.serializeNBT());

		return tag;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt)
	{
		super.readShareTag(stack, nbt);

		if (nbt != null)
		{
			final DynamicShardplateData data = stack.getCapability(ShardplateCurioItem.CAPABILITY).resolve().get();

			if (nbt.contains("shard_data"))
			{
				data.deserializeNBT(nbt.getCompound("shard_data"));
			}
		}
	}
}