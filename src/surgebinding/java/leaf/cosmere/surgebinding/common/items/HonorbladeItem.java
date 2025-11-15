/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.helpers.TimeHelper;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.surgebinding.common.capabilities.IShard;
import leaf.cosmere.surgebinding.common.capabilities.ShardData;
import leaf.cosmere.surgebinding.common.registries.SurgebindingAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class HonorbladeItem extends ShardbladeItem
{
	public final Roshar.RadiantOrder radiantOrder;

	/**
	 * Modifiers applied when the item is in the mainhand of a user. copied from sword item
	 */
	private Multimap<Attribute, AttributeModifier> attributeModifiers = null;

	// having primary and secondary means that theoretically a player could
	// hold two shards that share a surge and be twice as strong in that surge // todo more strength shouldn't really matter much in surgebinding
	protected static final UUID PRIMARY_HONORBLADE_SURGE_UUID = UUID.fromString("CB3F55D3-4865-4180-A497-9C13A33DB5CF");
	protected static final UUID SECONDARY_HONORBLADE_SURGE_UUID = UUID.fromString("FA233E1C-4180-4865-A497-BCCE9785ACA3");

	public HonorbladeItem(Roshar.RadiantOrder gemstone, Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
		this.radiantOrder = gemstone;
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
			builder.put(SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(radiantOrder.getFirstSurge()).getAttribute(), new AttributeModifier(PRIMARY_HONORBLADE_SURGE_UUID, "SurgeAttribute", 5, AttributeModifier.Operation.ADDITION));
			builder.put(SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(radiantOrder.getSecondSurge()).getAttribute(), new AttributeModifier(SECONDARY_HONORBLADE_SURGE_UUID, "SurgeAttribute", 5, AttributeModifier.Operation.ADDITION));
			this.attributeModifiers = builder.build();
		}

		return switch (equipmentSlot)
		{
			case MAINHAND, OFFHAND -> this.attributeModifiers;
			default -> super.getAttributeModifiers(equipmentSlot, stack);
		};

	}

	@Override
	public ItemStack getDefaultInstance()
	{
		ItemStack stack = super.getDefaultInstance();
		IShard data = stack.getCapability(ShardData.SHARD_DATA).orElseGet(() -> {return new ShardData(stack);});
		data.setOrder(radiantOrder);
		return stack;
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected)
	{
		super.inventoryTick(pStack, pLevel, pEntity, pItemSlot, pIsSelected);
		getShardData(pStack).setOrder(getOrder(pStack));
	}

	@Override
	public int bondTime()
	{
		return 5;
	}

	@Override
	public boolean canSummonDismiss(LivingEntity entity, ItemStack stack)
	{
		return true;
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{
		final ShardData data = getShardData(pStack);
		String attunedPlayerName = data.getBondedName();
		UUID attunedPlayer = data.getBondedEntity();
		if (attunedPlayer != null)
		{
			pTooltipComponents.add(TextHelper.createText(attunedPlayerName));
		}

		if(data.getOrder() != null)
		{
			pTooltipComponents.add(TextHelper.createText(StringHelper.fixCapitalisation(data.getOrder().getName())));
		}

	}

	@Override
	public ItemStack randomizedLootData(ItemStack stack)
	{
		ShardData data = getShardData(stack);
		data.setLiving(false);
		data.setOrder(radiantOrder);
		return stack;
	}
}
