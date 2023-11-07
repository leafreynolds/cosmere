/*
 * File updated ~ 28 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.capabilities;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.PlayerHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.feruchemy.common.config.FeruchemyConfigs;
import leaf.cosmere.feruchemy.common.items.RingMetalmindItem;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyManifestation;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import leaf.cosmere.feruchemy.client.utils.FeruchemyChargeThread;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;

public class FeruchemySpiritwebSubmodule implements ISpiritwebSubmodule
{
	@Override
	public void GiveStartingItem(Player player)
	{
		final int startingMetalmindCount = FeruchemyConfigs.SERVER.FULL_FERUCHEMIST_STARTING_METALMIND_COUNT.get();
		final double maxAmount = FeruchemyConfigs.SERVER.STARTING_METALMIND_RANDOMISED_MAX_FILL_AMOUNT.get();

		for (int i = 0; i < startingMetalmindCount; i++)
		{
			final float fillAmount = (float) (maxAmount * Math.random());
			int id = MathHelper.randomInt(0, 15);
			Metals.MetalType.valueOf(id).ifPresent(metalType -> GiveStartingItem(player, metalType, fillAmount));
		}
	}

	@Override
	public void GiveStartingItem(Player player, Manifestation manifestation)
	{
		if (manifestation instanceof FeruchemyManifestation feruchemyManifestation)
		{
			final double maxAmount = FeruchemyConfigs.SERVER.STARTING_METALMIND_RANDOMISED_MAX_FILL_AMOUNT.get();

			final float fillAmount = (float) (maxAmount * Math.random());
			GiveStartingItem(player, feruchemyManifestation.getMetalType(), fillAmount);
		}
	}

	@Override
	public void collectMenuInfo(List<String> m_infoText) {
		HashMap<Metals.MetalType, Double> metalmindChargesMap = FeruchemyChargeThread.getInstance().getCharges();

		if (!metalmindChargesMap.isEmpty())
		{
			for (Metals.MetalType metalType : metalmindChargesMap.keySet())
			{
				// todo localisation check
				final String text = "F. " + metalType.getName() + ": " + metalmindChargesMap.getOrDefault(metalType, 0D).intValue();
				m_infoText.add(text);
			}
		}

		ISpiritwebSubmodule.super.collectMenuInfo(m_infoText);
	}

	private static void GiveStartingItem(Player player, Metals.MetalType metalType, float fillAmount)
	{
		final RingMetalmindItem metalmindItem = FeruchemyItems.METAL_RINGS.get(metalType).get();
		ItemStack itemStack = new ItemStack(metalmindItem);


		metalmindItem.setCharge(itemStack, (int) (metalmindItem.getMaxCharge(itemStack) * fillAmount));
		PlayerHelper.addItem(player, itemStack);
	}

}
