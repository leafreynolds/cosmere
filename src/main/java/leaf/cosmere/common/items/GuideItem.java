/*
 * File updated ~ 11 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.common.items;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.compat.patchouli.PatchouliCompat;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nonnull;
import java.util.List;

public class GuideItem extends Item
{

	public GuideItem()
	{
		super(PropTypes.Items.ONE.get().rarity(Rarity.RARE));
	}

	public static boolean isOpen()
	{
		return ItemsRegistry.GUIDE.getRegistryName().equals(PatchouliAPI.get().getOpenBookGui());
	}


	public static Component getTitle(ItemStack stack)
	{
		return stack.getHoverName();
	}

	// Random item to expose this as public
	public static BlockHitResult doRayTrace(Level world, Player player, ClipContext.Fluid fluidMode)
	{
		return Item.getPlayerPOVHitResult(world, player, fluidMode);
	}

	/*
		@Override
		public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> list) {
			if (isInGroup(tab)) {
				list.add(new ItemStack(this));

			}
		}
	*/
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn)
	{
		tooltip.add(getEdition().copy().withStyle(ChatFormatting.GRAY));
	}

	public static Component getEdition()
	{
		if (PatchouliCompat.PatchouliIsPresent())
		{
			try
			{
				return PatchouliAPI.get().getSubtitle(ItemsRegistry.GUIDE.getRegistryName());
			}
			catch (IllegalArgumentException e)
			{
				return Component.empty();
			}
		}
		else
		{
			return TextHelper.createTranslatedText(Constants.Strings.PATCHOULI_NOT_INSTALLED);
		}
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
	{
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (!PatchouliCompat.PatchouliIsPresent())
		{
			playerIn.sendSystemMessage(TextHelper.createTranslatedText(Constants.Strings.PATCHOULI_NOT_INSTALLED));
		}
		else if (playerIn instanceof ServerPlayer player)
		{
			PatchouliAPI.get().openBookGUI(player, ItemsRegistry.GUIDE.getRegistryName());
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}
}
