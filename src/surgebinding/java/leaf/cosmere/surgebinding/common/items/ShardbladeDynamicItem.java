/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.surgebinding.client.render.renderer.ShardbladeItemRenderer;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardbladeData;
import leaf.cosmere.surgebinding.common.capabilities.RadiantShardData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ShardbladeDynamicItem extends ShardbladeItem
{
	public ShardbladeDynamicItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
	}

	@Override
	public DynamicShardbladeData getShardData(ItemStack stack)
	{
		return RadiantShardData.load(stack, createShardData(stack));
	}

	@Override
	public DynamicShardbladeData createShardData(ItemStack stack)
	{
		return new DynamicShardbladeData(stack);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
		//the class instance hasn't been resolved yet at this point, so we can't check for field values

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
	public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{
		final DynamicShardbladeData data = getShardData(pStack);
		String attunedPlayerName = data.getBondedName();
		UUID attunedPlayer = data.getBondedEntity();
		if (attunedPlayer != null)
		{
			pTooltipComponents.add(TextHelper.createText(attunedPlayerName));
		}

		if (data.getOrder() != null)
		{
			pTooltipComponents.add(TextHelper.createText(StringHelper.fixCapitalisation(data.getOrder().getName())));
		}

		if (!InventoryScreen.hasShiftDown())
		{
			pTooltipComponents.add(Constants.Translations.TOOLTIP_HOLD_SHIFT);
			return;
		}

		pTooltipComponents.add(TextHelper.createText(String.format("Blade: %s", data.getBladeID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Crossguard: %s", data.getCrossGuardID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Handle: %s", data.getHandleID())));
		pTooltipComponents.add(TextHelper.createText(String.format("Pommel: %s", data.getPommelID())));
	}
}
