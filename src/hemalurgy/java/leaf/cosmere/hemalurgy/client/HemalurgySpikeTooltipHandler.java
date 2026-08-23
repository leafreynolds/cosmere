/*
 * File created ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.hemalurgy.client;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.capabilities.HemalurgyItemCapabilities;
import leaf.cosmere.hemalurgy.common.items.HemalurgicSpikeItem;
import leaf.cosmere.hemalurgy.common.items.IHemalurgicInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

//an event, not appendHoverText, so data-map spikes get the tooltip too
@EventBusSubscriber(modid = Hemalurgy.MODID, value = Dist.CLIENT)
public class HemalurgySpikeTooltipHandler
{
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event)
	{
		final ItemStack stack = event.getItemStack();
		final IHemalurgicInfo spike = HemalurgyItemCapabilities.getSpike(stack);
		if (spike == null)
		{
			return;
		}

		//name the spike type on items that aren't spike items
		if (!(stack.getItem() instanceof HemalurgicSpikeItem))
		{
			final Metals.MetalType metalType = spike.getSpikeMetalType(stack);
			if (metalType != null)
			{
				event.getToolTip().add(TextHelper.createTranslatedText("item." + Hemalurgy.MODID + "." + metalType.getName() + "_spike")
						.withStyle(ChatFormatting.DARK_RED));
			}
		}

		spike.addInvestitureInformation(stack, event.getToolTip());

		//vessel usage
		final double stolen = spike.getTotalStolenStrength(stack);
		final double chargeUnits = spike.getFeruchemicalChargeUnits(stack);
		if (stolen > 0.01 || chargeUnits > 0.01)
		{
			final double used = (double) Math.round((stolen + chargeUnits) * 10) / 10;
			event.getToolTip().add(TextHelper.createText(
							String.format("Investiture: %s/%s", used, spike.getSpikeInvestitureCapacity()))
					.withStyle(ChatFormatting.GRAY));
		}
	}
}
