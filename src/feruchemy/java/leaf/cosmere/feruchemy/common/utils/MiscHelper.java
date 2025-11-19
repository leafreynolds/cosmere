/*
 * File updated ~ 9 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.utils;

import leaf.cosmere.api.IGrantsManifestations;
import leaf.cosmere.api.IHasSize;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.items.GodMetalNuggetItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class MiscHelper
{

	public static void consumeNugget(LivingEntity livingEntity, ItemStack itemStack)
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

			if (itemStack.getItem() instanceof GodMetalNuggetItem godItem && godItem.getMetalType() == Metals.MetalType.LERASATIUM)
			{
				if (livingEntity instanceof Llama && !livingEntity.hasCustomName())
				{
					//todo translations
					livingEntity.setCustomName(TextHelper.createTranslatedText("Full Feruchemist Llama"));
				}
			}
		}
	}


}
