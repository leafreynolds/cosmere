/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.properties.PropTypes;
import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
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

    public static ITextComponent getEdition()
    {
        return PatchouliAPI.get().getSubtitle(ItemsRegistry.GUIDE.getId());
    }

    public static ITextComponent getTitle(ItemStack stack)
    {
        ITextComponent title = stack.getDisplayName();

        String akashicTomeNBT = "akashictome:displayName";
        if (stack.hasTag() && stack.getTag().contains(akashicTomeNBT))
        {
            title = ITextComponent.Serializer.getComponentFromJson(stack.getTag().getString(akashicTomeNBT));
        }

        return title;
    }

    // Random item to expose this as public
    public static BlockRayTraceResult doRayTrace(World world, PlayerEntity player, RayTraceContext.FluidMode fluidMode)
    {
        return Item.rayTrace(world, player, fluidMode);
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
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(getEdition().deepCopy().mergeStyle(TextFormatting.GRAY));
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (playerIn instanceof ServerPlayerEntity)
        {
            ServerPlayerEntity player = (ServerPlayerEntity) playerIn;

            //UseItemSuccessTrigger.INSTANCE.trigger(player, stack, player.getServerWorld(), player.getPosX(), player.getPosY(), player.getPosZ());

            PatchouliAPI.get().openBookGUI((ServerPlayerEntity) playerIn, ItemsRegistry.GUIDE.getId());
        }

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }
}