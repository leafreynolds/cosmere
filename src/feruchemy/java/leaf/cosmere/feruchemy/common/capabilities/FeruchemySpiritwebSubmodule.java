/*
 * File updated ~ 5 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.capabilities;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.PlayerHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.gui.SpiritwebRegistry;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.feruchemy.client.gui.FeruchemySpiritwebMenu;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import leaf.cosmere.feruchemy.common.config.FeruchemyConfigs;
import leaf.cosmere.feruchemy.common.items.NicrosilRingMetalmindItem;
import leaf.cosmere.feruchemy.common.items.RingMetalmindItem;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyManifestation;
import leaf.cosmere.feruchemy.common.registries.FeruchemyAttributes;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.stream.Collectors;

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
	public List<Attribute> getPowers()
	{
		return FeruchemyAttributes.FERUCHEMY_ATTRIBUTES.values().stream()
				.map((AttributeRegistryObject::getAttribute)).collect(Collectors.toList());
	}

	@Override
	public void drainInvestiture(ISpiritweb data, double strength)
	{
		//todo - how should we handle draining feruchemy?
		// remove the effects only? can we even detect that properly?
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerMenu()
	{
		SpiritwebCapability.get(Minecraft.getInstance().player).ifPresent( (spiritweb) -> {
			if (spiritweb.hasManifestationOfType(Manifestations.ManifestationTypes.FERUCHEMY))
				SpiritwebRegistry.getInstance().register(Manifestations.ManifestationTypes.FERUCHEMY, FeruchemySpiritwebMenu::new);
		});
	}

	private static void GiveStartingItem(Player player, Metals.MetalType metalType, float fillAmount)
	{
		ItemStack itemStack;
		if (metalType == Metals.MetalType.NICROSIL)
		{
			final Item metalmindItem = FeruchemyItems.METAL_RINGS.get(metalType).get();
			itemStack = new ItemStack(metalmindItem);
		}
		else
		{
			final RingMetalmindItem metalmindItem = (RingMetalmindItem) FeruchemyItems.METAL_RINGS.get(metalType).get();
			itemStack = new ItemStack(metalmindItem);
			metalmindItem.setCharge(itemStack, (int) (metalmindItem.getMaxCharge(itemStack) * fillAmount));
		}
		PlayerHelper.addItem(player, itemStack);
	}

}
