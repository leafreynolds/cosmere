/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.surgebinding.client.render.renderer.ShardbladeItemRenderer;
import leaf.cosmere.surgebinding.common.capabilities.BondableRadiantShardData;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardbladeData;
import leaf.cosmere.surgebinding.common.capabilities.IShardbladeDynamicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ShardbladeDynamicItem extends ShardbladeItem
{
	public ShardbladeDynamicItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
	}

	public static IShardbladeDynamicData getData(ItemStack stack)
	{
		return DynamicShardbladeData.fromStack(stack);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
		consumer.accept(new IClientItemExtensions()
		{
			ShardbladeItemRenderer renderer = null;

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer()
			{
				Minecraft minecraft = Minecraft.getInstance();
				if (renderer == null)
				{
					renderer = new ShardbladeItemRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
				}
				return renderer;
			}
		});
	}

	@Override
	public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{
		final BondableRadiantShardData bondData = getShardData(pStack);
		UUID attunedPlayer = bondData.getBondedEntity();
		if (attunedPlayer != null)
		{
			pTooltipComponents.add(TextHelper.createText(bondData.getBondedName()));
		}

		Roshar.RadiantOrder order = bondData.getOrder();
		if (order != null)
		{
			pTooltipComponents.add(TextHelper.createText(StringHelper.fixCapitalisation(order.getName())));
		}

		if (!InventoryScreen.hasShiftDown())
		{
			pTooltipComponents.add(Constants.Translations.TOOLTIP_HOLD_SHIFT);
			return;
		}

		final IShardbladeDynamicData data = getData(pStack);
		pTooltipComponents.add(TextHelper.createText(String.format("Blade: %s", data.getBladeID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Crossguard: %s", data.getCrossGuardID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Handle: %s", data.getHandleID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Pommel: %s", data.getPommelID())));
	}
}
