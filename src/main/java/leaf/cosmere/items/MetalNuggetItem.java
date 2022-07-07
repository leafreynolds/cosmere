/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.TextHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public class MetalNuggetItem extends MetalItem
{
	public static final DamageSource EAT_METAL = (new DamageSource("eat_metal")).bypassArmor().bypassMagic();


	public MetalNuggetItem(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public boolean isEdible()
	{
		return true;
	}

	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
	{
		return ItemUtils.startUsingInstantly(worldIn, playerIn, handIn);
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		//be annoying enough that people prefer metal vials
		return 16;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity)
	{
		consumeNugget(pLivingEntity, this.getMetalType(), pStack, 1);
		pLivingEntity.hurt(EAT_METAL,1);
		return pStack;
	}

	public static void consumeNugget(LivingEntity livingEntity, Metals.MetalType metalType, ItemStack itemstack, int amount)
	{
		if (metalType == null || livingEntity.level.isClientSide)
		{
			return;
		}

		SpiritwebCapability.get(livingEntity).ifPresent(iSpiritweb ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) iSpiritweb;

			if (metalType == Metals.MetalType.LERASIUM || metalType == Metals.MetalType.LERASATIUM)
			{
				//https://www.theoryland.com/intvmain.php?i=977#43
				if (metalType == Metals.MetalType.LERASIUM && livingEntity instanceof Llama && !livingEntity.hasCustomName())
				{
					//todo translations
					livingEntity.setCustomName(TextHelper.createTranslatedText("Mistborn Llama"));
				}


				for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
				{
					switch (metalType)
					{
						case LERASIUM:
							//give allomancy
							if (manifestation.getManifestationType() == Manifestations.ManifestationTypes.ALLOMANCY)
							{
								//todo config allomancy strength
								spiritweb.giveManifestation(manifestation, 13);
							}
							break;
						case LERASATIUM:
							//give feruchemy
							if (manifestation.getManifestationType() == Manifestations.ManifestationTypes.FERUCHEMY)
							{
								//todo config feruchemy strength
								spiritweb.giveManifestation(manifestation, 13);
							}
							break;
					}
				}
			}
			else
			{
				//add to metal stored
				Integer metalIngested = spiritweb.METALS_INGESTED.get(metalType);
				spiritweb.METALS_INGESTED.put(metalType, metalIngested + (metalType.getAllomancyBurnTimeSeconds() * amount));
			}

			spiritweb.syncToClients(null);

			if (itemstack != null && livingEntity instanceof Player && !((Player) livingEntity).isCreative())
			{
				itemstack.shrink(1);
			}
		});
	}
}
