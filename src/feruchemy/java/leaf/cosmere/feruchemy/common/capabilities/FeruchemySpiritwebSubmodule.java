/*
 * File updated ~ 12 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.capabilities;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.PlayerHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.feruchemy.common.items.RingMetalmindItem;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyManifestation;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FeruchemySpiritwebSubmodule implements ISpiritwebSubmodule
{
	@Override
	public void GiveStartingItem(Player player)
	{
		//todo config how many metalminds a full feruchemist can start with
		final int startingMetalmindCount = 3;

		for (int i = 0; i < startingMetalmindCount; i++)
		{
			//todo config feruchemist starting metalmind charge level
			final float fillAmount = (float) (0.6667f * Math.random());
			int id = MathHelper.randomInt(0, 15);
			Metals.MetalType.valueOf(id).ifPresent(metalType -> GiveStartingItem(player, metalType, fillAmount));
		}
	}

	@Override
	public void GiveStartingItem(Player player, Manifestation manifestation)
	{
		if (manifestation instanceof FeruchemyManifestation feruchemyManifestation)
		{
			//todo config ferring starting metalmind charge level
			final float fillAmount = (float) (0.6667f * Math.random());
			GiveStartingItem(player, feruchemyManifestation.getMetalType(), fillAmount);
		}
	}

	private static void GiveStartingItem(Player player, Metals.MetalType metalType, float fillAmount)
	{
		final RingMetalmindItem metalmindItem = FeruchemyItems.METAL_RINGS.get(metalType).get();
		ItemStack itemStack = new ItemStack(metalmindItem);


		metalmindItem.setCharge(itemStack, (int) (metalmindItem.getMaxCharge(itemStack) * fillAmount));
		PlayerHelper.addItem(player, itemStack);
	}

}
