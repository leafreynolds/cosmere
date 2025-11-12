package leaf.cosmere.metallurgy.common.items;

import leaf.cosmere.common.items.BaseItem;
import leaf.cosmere.metallurgy.common.util.AlloyComposition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class AlloyPowderItem extends BaseItem {
    public AlloyPowderItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        AlloyComposition.addContaminationTooltip(stack, tooltip);
    }
}
