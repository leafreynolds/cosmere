/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.compat.patchouli.PatchouliCompat;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.properties.PropTypes;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.utils.helpers.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
		return ItemsRegistry.GUIDE.getId().equals(PatchouliAPI.get().getOpenBookGui());
	}


	public static Component getTitle(ItemStack stack)
	{
		Component title = stack.getHoverName();

		String akashicTomeNBT = "akashictome:displayName";
		if (stack.hasTag() && stack.getTag().contains(akashicTomeNBT))
		{
			title = Component.Serializer.fromJson(stack.getTag().getString(akashicTomeNBT));
		}

		return title;
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
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		tooltip.add(getEdition().copy().withStyle(ChatFormatting.GRAY));
	}

	public static Component getEdition()
	{
		if (PatchouliCompat.PatchouliIsPresent())
		{
			try
			{
				return PatchouliAPI.get().getSubtitle(ItemsRegistry.GUIDE.getId());
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

			//UseItemSuccessTrigger.INSTANCE.trigger(player, stack, player.getServerWorld(), player.getPosX(), player.getPosY(), player.getPosZ());

			PatchouliAPI.get().openBookGUI((ServerPlayer) playerIn, ItemsRegistry.GUIDE.getId());
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}
}