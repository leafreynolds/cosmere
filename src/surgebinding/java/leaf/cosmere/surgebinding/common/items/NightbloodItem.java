/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.registries.SurgebindingAttributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

import java.util.UUID;

public class NightbloodItem extends ShardbladeItem
{
	private Multimap<Attribute, AttributeModifier> attributeModifiers = null;
	protected static final UUID NIGHTBLOOD_SURGE_UUID = UUID.fromString("CB3F55D3-4865-4180-A497-9C13A33DB5CC");

	public NightbloodItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
	}


	public boolean canSummonDismiss(Player player)
	{
		return false;
	}

	/**
	 * Gets a map of item attribute modifiers, used by damage when used as melee weapon.
	 */
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack)
	{
		if (attributeModifiers == null)
		{
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeedIn, AttributeModifier.Operation.ADDITION));

			if (SurgebindingConfigs.SERVER.NIGHTBLOOD_SPOILERS.get())
			{
				for (Roshar.Surges surge : EnumUtils.SURGES)
				{
					builder.put(SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(surge).getAttribute(), new AttributeModifier(NIGHTBLOOD_SURGE_UUID, "Nightblood", 5, AttributeModifier.Operation.ADDITION));
				}
			}

			this.attributeModifiers = builder.build();
		}

		return switch (equipmentSlot)
		{
			case MAINHAND, OFFHAND -> this.attributeModifiers;
			default -> super.getAttributeModifiers(equipmentSlot, stack);
		};
	}
}
