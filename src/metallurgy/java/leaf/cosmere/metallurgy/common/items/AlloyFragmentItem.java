package leaf.cosmere.metallurgy.common.items;

import leaf.cosmere.common.items.BaseItem;
import leaf.cosmere.metallurgy.common.util.AlloyComposition;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class AlloyFragmentItem extends BaseItem {
    public AlloyFragmentItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        // Display fragment percentage
        double percentage = AlloyComposition.getFragmentPercentage(stack);
        if (percentage < 1.0) {
            tooltip.add(Component.literal(String.format("%.1f%% of an ingot", percentage * 100.0))
                    .withStyle(ChatFormatting.AQUA));
        }

        AlloyComposition.addContaminationTooltip(stack, tooltip);
    }
}
