package leaf.cosmere.common.items;

import leaf.cosmere.api.*;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
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

public class GodMetalNuggetItem extends MetalNuggetItem implements IHasSize, IGrantsManifestations
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
	public void onCraftedBy(ItemStack itemStack, Level level, Player player) {
		CompoundTag nbt = itemStack.getOrCreateTag();
		if(!nbt.contains("nuggetSize")) writeMetalAlloySizeNbtData(itemStack, getMaxSize());
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
	public boolean hasCraftingRemainingItem(ItemStack stack) {
		return true;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {
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

		ArrayList<Manifestation> manifestations = determineManifestations(stack);

		if (!manifestations.isEmpty())
		{
			tooltip.add(Component.empty());
			tooltip.add(Component.literal("When consumed:").withStyle(ChatFormatting.GOLD));
			for (Manifestation manifestation : manifestations)
			{
				tooltip.add(Component.literal("+" + size + " ").append(
								Component.translatable(manifestation.getTranslationKey()))
						.withStyle(ChatFormatting.BLUE));
			}
		}
	}

	public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
		return false;
	}

	@Override
	public ArrayList<Manifestation> determineManifestations(ItemStack itemStack)
	{
		ArrayList<Manifestation> manifestations = new ArrayList<>();

		if (this.getMetalType() == Metals.MetalType.LERASIUM)
		{
			for (Metals.MetalType metal : EnumUtils.METAL_TYPES)
			{
				Manifestation manifestation = Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(metal.getID());
				if (manifestation.getManifestationType() != Manifestations.ManifestationTypes.NONE)
				{
					manifestations.add(manifestation);
				}
			}
		}
		else if (this.getMetalType() == Metals.MetalType.LERASATIUM)
		{
			for (Metals.MetalType metal : EnumUtils.METAL_TYPES)
			{
				Manifestation manifestation = Manifestations.ManifestationTypes.FERUCHEMY.getManifestation(metal.getID());
				if (manifestation.getManifestationType() != Manifestations.ManifestationTypes.NONE)
				{
					manifestations.add(manifestation);
				}
			}
		}
		return manifestations;
	}

	@Override
	public void grantManifestations(LivingEntity livingEntity, ArrayList<Manifestation> manifestations, int strength)
	{
		SpiritwebCapability.get(livingEntity).ifPresent(iSpiritweb ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) iSpiritweb;

			for(Manifestation manifestation: manifestations)
			{
				int currentStrength = 0;
				if(!(manifestation.getAttribute() instanceof RangedAttribute attribute)) return;
				AttributeInstance attributeInstance = livingEntity.getAttribute(attribute);
				if(attributeInstance != null) {
					currentStrength = (int) attributeInstance.getValue();
				}

				// Let's ensure not to exceed the base value if it's out of range,
				// even if it will get sanitized
				int newStrength = strength + currentStrength;
				if(newStrength < attribute.getMinValue())
				{
					newStrength = (int) attribute.getMinValue();
				}
				else if (newStrength > attribute.getMaxValue())
				{
					newStrength = (int) attribute.getMaxValue();
				}

				spiritweb.giveManifestation(manifestation, newStrength);

			}

			if (livingEntity instanceof ServerPlayer serverPlayer)
			{
				spiritweb.syncToClients(serverPlayer);
			}
		});
	}

}
