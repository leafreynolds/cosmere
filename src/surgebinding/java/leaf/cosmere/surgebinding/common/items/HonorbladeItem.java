package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.registries.SurgebindingAttributes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class HonorbladeItem extends ShardbladeItem
{
	public final Roshar.RadiantOrder radiantOrder;

	public HonorbladeItem(Roshar.RadiantOrder radiantOrder, Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
		this.radiantOrder = radiantOrder;
	}

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

		Attribute firstSurge = SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(radiantOrder.getFirstSurge()).getAttribute();
		Attribute secondSurge = SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(radiantOrder.getSecondSurge()).getAttribute();

		builder.add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(firstSurge),
				new AttributeModifier(Surgebinding.rl(radiantOrder.getName() + "_primary_honorblade_surge"),
						5, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND);
		builder.add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(secondSurge),
				new AttributeModifier(Surgebinding.rl(radiantOrder.getName() + "_secondary_honorblade_surge"),
						5, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND);

		return builder.build();
	}

	@Override
	public boolean canSummonDismiss(Player player)
	{
		return true;
	}
}
