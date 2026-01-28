package leaf.cosmere.common.items;

import leaf.cosmere.api.IGrantsBaseAttributes;
import leaf.cosmere.api.IHasSize;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.util.CosmereAttributeUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

import static leaf.cosmere.common.util.CosmereAttributeUtils.grantBaseAttribute;

public class GodMetalAlloyNuggetItem extends AlloyNuggetItem implements IHasSize, IGrantsBaseAttributes
{
	public static int MIN_SIZE = 1;
	public static int MAX_SIZE = 16;

	public GodMetalAlloyNuggetItem(Metals.MetalType metalType, Metals.MetalType alloyedMetalType)
	{

		super(metalType, alloyedMetalType);
	}

	public int getMinSize()
	{
		return MIN_SIZE;
	}

	public int getMaxSize()
	{
		return MAX_SIZE;
	}

	public void addFilled(CreativeModeTab.Output output, int size)
	{
		ItemStack itemStack = new ItemStack(this);
		writeMetalAlloySizeNbtData(itemStack, size);
		output.accept(itemStack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		Integer size = readMetalAlloySizeNbtData(stack);

		MutableComponent metalListComponent = Component.empty();
		metalListComponent.append(Component.translatable(this.getMetalType().getTranslationKey())
				.withStyle(Style.EMPTY.withColor(this.getMetalType().getColorValue())));

		metalListComponent.append(Component.literal(" / ").withStyle(ChatFormatting.WHITE));
		metalListComponent.append(Component.translatable(alloyedMetalType.getTranslationKey())
				.withStyle(Style.EMPTY.withColor(alloyedMetalType.getColorValue())));

		tooltip.add(metalListComponent);
		tooltip.add(Component.literal("Size: ").withStyle(ChatFormatting.WHITE).append(
				Component.literal(size + "/" + MAX_SIZE).withStyle(ChatFormatting.GRAY)));

		ArrayList<Attribute> attributes = determineBaseAttributes(stack);

		if (!attributes.isEmpty() & size != null)
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

	@Override
	public Rarity getRarity(ItemStack itemStack)
	{
		Integer size = readMetalAlloySizeNbtData(itemStack);
		if (size == null)
		{
			return Rarity.COMMON;
		}

		if (size <= 8)
		{
			return Rarity.UNCOMMON;
		}
		else if (size == MAX_SIZE)
		{
			return Rarity.EPIC;
		}
		return Rarity.RARE;
	}

	public ArrayList<Attribute> determineBaseAttributes(ItemStack itemStack)
	{
		ArrayList<Attribute> attributes = new ArrayList<>();

		if (this.getMetalType() == Metals.MetalType.LERASIUM)
		{
			Attribute attribute = CosmereAttributeUtils.getAttribute(Manifestations.ManifestationTypes.ALLOMANCY, alloyedMetalType.getID());
			if (attribute != null)
			{
				attributes.add(attribute);
			}
		}
		else if (this.getMetalType() == Metals.MetalType.LERASATIUM)
		{
			Attribute attribute = CosmereAttributeUtils.getAttribute(Manifestations.ManifestationTypes.FERUCHEMY, alloyedMetalType.getID());
			if (attribute != null)
			{
				attributes.add(attribute);
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