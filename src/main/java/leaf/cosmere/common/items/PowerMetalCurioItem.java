/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.items;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.charge.IHoldsPowers;
import leaf.cosmere.common.properties.PropTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;

public class PowerMetalCurioItem extends BaseItem implements IHasMetalType, ICurioItem, IHoldsPowers
{
	private final Metals.MetalType metalType;

	public PowerMetalCurioItem(Metals.MetalType metalType)
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
		List<Holder<Attribute>> attributes = getAttributes(itemStack);
		for (Holder<Attribute> attribute : attributes)
		{
			if (attribute != null)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn)
	{
		super.appendHoverText(stack, context, tooltip, flagIn);

		List<Holder<Attribute>> attributes = getAttributes(stack);
		Integer[] attributeStrengths = getAttributeStrengths(stack);

		String attunedPlayerName = getAttunedPlayerName(stack);
		UUID attunedPlayer = getAttunedPlayer(stack);

		if (attunedPlayer != null)
		{
			MutableComponent identityName = TextHelper.createText(attunedPlayerName).withStyle();

			Minecraft mc = Minecraft.getInstance();
			Player player = mc.player;

			if(attunedPlayer.equals(Constants.NBT.UNKEYED_UUID))
			{
				identityName = Component.literal("Unkeyed");
			}
			else if(player != null && !player.getUUID().equals(attunedPlayer))
			{
				identityName = Component.literal(EnchantmentNames.getInstance().getRandomName(mc.font, 16).getString());
			}
			tooltip.add(Component.literal("Identity: ").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC).append(identityName.withStyle(ChatFormatting.LIGHT_PURPLE)));
		}

		boolean isFirst = true;
		for (int i = 0; i < attributes.size(); i++)
		{

			if (attributes.get(i) != null && attributeStrengths[i] != null)
			{
				if (isFirst)
				{
					tooltip.add(Component.literal("When tapped:").withStyle(ChatFormatting.GOLD));
					isFirst = false;
				}

				tooltip.add(Component.literal("+" + attributeStrengths[i] + " ").append(
								Component.translatable(attributes.get(i).getRegisteredName()))
						.withStyle(ChatFormatting.BLUE));
			}
		}
	}
}
