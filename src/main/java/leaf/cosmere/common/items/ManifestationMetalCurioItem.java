/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.items;

import leaf.cosmere.api.IHasManifestations;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.properties.PropTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Arrays;
import java.util.List;

public class ManifestationMetalCurioItem extends BaseItem implements IHasMetalType, ICurioItem, IHasManifestations
{
	private final Metals.MetalType metalType;

	public ManifestationMetalCurioItem(Metals.MetalType metalType)
	{
		super(PropTypes.Items.ONE.get().rarity(metalType.getRarity()));
		this.metalType = metalType;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}


	@Override
	public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack)
	{
		return ICurioItem.super.getAttributesTooltip(tooltips, stack);
	}

	@Override
	public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack)
	{
		return makesPiglinsNeutral(stack, slotContext.entity());
	}

	@Override
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer)
	{
		return this.metalType == Metals.MetalType.GOLD;
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack)
	{
		ICurioItem.super.onUnequip(slotContext, newStack, stack);
	}

	@Override
	public int getMaxCapacity()
	{
		return 0;
	}

	@Override
	public boolean isFoil(ItemStack itemStack)
	{
		Manifestation[] manifestations = getManifestations(itemStack);
		for(Manifestation manifestation : manifestations)
		{
			if(manifestation != null) return true;
		}
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		super.appendHoverText(stack, worldIn, tooltip, flagIn);

		Manifestation[] manifestations = getManifestations(stack);
		Integer[] manifestationStrengths = getManifestationStrengths(stack);

		boolean isFirst = true;
		for (int i = 0; i < manifestations.length; i++)
		{

			if(manifestations[i] != null && manifestationStrengths[i] != null)
			{
				if(isFirst)
				{
					tooltip.add(Component.empty());
					tooltip.add(Component.literal("When tapped:").withStyle(ChatFormatting.GOLD));
					isFirst = false;
				}

				tooltip.add(Component.literal("+" + manifestationStrengths[i] + " ").append(
						Component.translatable(manifestations[i].getTranslationKey()))
						.withStyle(ChatFormatting.BLUE));
			}
		}
	}
}
