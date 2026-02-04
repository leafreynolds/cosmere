package leaf.cosmere.common.items;

import leaf.cosmere.api.*;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.util.CosmereAttributeUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

import static leaf.cosmere.common.util.CosmereAttributeUtils.grantBaseAttribute;

public class GodMetalNuggetItem extends MetalNuggetItem implements IHasSize, IGrantsBaseAttributes
{
	public static int MIN_SIZE = 1;
	public static int MAX_SIZE = 16;

	public GodMetalNuggetItem(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public int getMaxSize()
	{
		return MAX_SIZE;
	}

	@Override
	public int getMinSize()
	{
		return MIN_SIZE;
	}

	@Override
	public void onCraftedBy(ItemStack itemStack, Level level, Player player)
	{
		CompoundTag nbt = itemStack.getOrCreateTag();
		if (!nbt.contains("nuggetSize"))
		{
			writeMetalAlloySizeNbtData(itemStack, getMaxSize());
		}
	}

	// God Metals shouldn't hurt
	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level pLevel, LivingEntity pLivingEntity)
	{
		if (pLevel.isClientSide)
		{
			return itemstack;
		}

		if (pLivingEntity instanceof Player player && !player.isCreative())
		{
			itemstack.shrink(1);
		}

		return itemstack;
	}

	@Override
	public boolean hasCraftingRemainingItem(ItemStack stack)
	{
		return true;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isFoil(ItemStack itemStack)
	{
		// God Metals should have foil
		return super.isFoil(itemStack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		Integer size = readMetalAlloySizeNbtData(stack);

		tooltip.add(Component.literal("Size: ").withStyle(ChatFormatting.WHITE).append(
				Component.literal(size + "/" + MAX_SIZE).withStyle(ChatFormatting.GRAY)));

		ArrayList<Attribute> attributes = determineBaseAttributes(stack);

		if (!attributes.isEmpty())
		{
			tooltip.add(Component.empty());
			tooltip.add(Component.literal("When consumed:").withStyle(ChatFormatting.GOLD));
			for (Attribute attribute : attributes)
			{
				tooltip.add(Component.literal("+" + size + " ").append(
								Component.translatable(attribute.getDescriptionId()))
						.withStyle(ChatFormatting.BLUE));
			}
		}
	}

	public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess)
	{
		return false;
	}

	@Override
	public ArrayList<Attribute> determineBaseAttributes(ItemStack itemStack)
	{
		ArrayList<Attribute> attributes = new ArrayList<>();

		if (this.getMetalType() == Metals.MetalType.LERASIUM)
		{
			for (Metals.MetalType metal : EnumUtils.METAL_TYPES)
			{
				Attribute attribute = CosmereAttributeUtils.getAttribute(Manifestations.ManifestationTypes.ALLOMANCY, metal.getID());
				if (attribute != null)
				{
					attributes.add(attribute);
				}
			}
		}
		else if (this.getMetalType() == Metals.MetalType.LERASATIUM)
		{
			for (Metals.MetalType metal : EnumUtils.METAL_TYPES)
			{
				Attribute attribute = CosmereAttributeUtils.getAttribute(Manifestations.ManifestationTypes.FERUCHEMY, metal.getID());
				if (attribute != null)
				{
					attributes.add(attribute);
				}
			}
		}
		return attributes;
	}

	@Override
	public void grantBaseAttributes(LivingEntity livingEntity, ArrayList<Attribute> attributes, int strength)
	{
		SpiritwebCapability.get(livingEntity).ifPresent(iSpiritweb ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) iSpiritweb;

			for (Attribute attribute : attributes)
			{
				if (!(attribute instanceof RangedAttribute rangedAttribute))
				{
					return;
				}
				grantBaseAttribute(livingEntity, rangedAttribute, strength);
			}

			if (livingEntity instanceof ServerPlayer serverPlayer)
			{
				spiritweb.syncToClients(serverPlayer);
			}
		});
	}

}
