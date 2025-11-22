/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.utils;

import leaf.cosmere.allomancy.common.capabilities.AllomancySpiritwebSubmodule;
import leaf.cosmere.api.*;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.items.GodMetalNuggetItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;

public class MiscHelper
{

	public static void consumeNugget(LivingEntity livingEntity, ItemStack itemStack, int amount)
	{
		if (livingEntity.level().isClientSide) return;

		if(itemStack.getItem() instanceof IGrantsManifestations manifestingItem && itemStack.getItem() instanceof IHasSize sizeItem)
		{
			Integer size = sizeItem.readMetalAlloySizeNbtData(itemStack);
			if(size != null)
			{
				ArrayList<Manifestation> manifestations = manifestingItem.determineManifestations(itemStack);
				manifestingItem.grantManifestations(livingEntity, manifestations, size);
			}

			//https://www.theoryland.com/intvmain.php?i=977#43
			if (itemStack.getItem() instanceof GodMetalNuggetItem godItem)
			{
				if (godItem.getMetalType() == Metals.MetalType.LERASIUM)
				{
					if (livingEntity instanceof Llama && !livingEntity.hasCustomName())
					{
						//todo translations
						livingEntity.setCustomName(TextHelper.createTranslatedText("Mistborn Llama"));
					}
				}
				else if(godItem.getMetalType() == Metals.MetalType.ATIUM)
				{
					eatMetal(godItem.getMetalType(), livingEntity, amount);
				}
			}
		}
		else if(itemStack.getItem() instanceof IHasMetalType metalItem)
		{
			eatMetal(metalItem.getMetalType(), livingEntity, amount);
		}
		else if(itemStack.getItem() == Items.IRON_NUGGET)
		{
			eatMetal(Metals.MetalType.IRON, livingEntity, amount);
		}
		else if(itemStack.getItem() == Items.GOLD_NUGGET)
		{
			eatMetal(Metals.MetalType.GOLD, livingEntity, amount);
		}
	}

	private static void eatMetal(Metals.MetalType metalType, LivingEntity livingEntity, int amount)
	{
		SpiritwebCapability.get(livingEntity).ifPresent(iSpiritweb ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) iSpiritweb;
			if (metalType.hasAssociatedManifestation()) //ignore metals without manifestations, that's handled in feruchemy
			{
				//add to metal stored
				final int addAmount = metalType.getAllomancyBurnTimeSeconds() * amount;
				AllomancySpiritwebSubmodule allo = (AllomancySpiritwebSubmodule) spiritweb.getSubmodule(Manifestations.ManifestationTypes.ALLOMANCY);
				allo.adjustIngestedMetal(metalType, addAmount, true);
			}
		});
	}


}
