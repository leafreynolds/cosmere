/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.registries.SurgebindingAttributes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class NightbloodItem extends ShardbladeItem
{
	public NightbloodItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
	}

	// canSummonDismiss intentionally inherits ShardbladeItem's bonded-user gate.

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers()
	{
		var builder = ItemAttributeModifiers.builder();

		builder.add(Attributes.ATTACK_DAMAGE,
				new AttributeModifier(ResourceLocation.withDefaultNamespace("base_attack_damage"),
						attackDamage, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND);
		builder.add(Attributes.ATTACK_SPEED,
				new AttributeModifier(ResourceLocation.withDefaultNamespace("base_attack_speed"),
						attackSpeedIn, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND);

		if (SurgebindingConfigs.SERVER.NIGHTBLOOD_SPOILERS.get())
		{
			for (Roshar.Surges surge : EnumUtils.SURGES)
			{
				Attribute surgeAttr = SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(surge).getAttribute();
				builder.add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(surgeAttr),
						new AttributeModifier(Surgebinding.rl("nightblood_" + surge.getName()),
								5, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.MAINHAND);
			}
		}

		return builder.build();
	}
}
