package leaf.cosmere.metallurgy.common.items;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class MetallurgistChiselItem extends DiggerItem {
    public MetallurgistChiselItem(Tier tier, float attackDamageModifier, float attackSpeedModifier,
            Properties properties) {
        super(attackDamageModifier, attackSpeedModifier, tier, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, net.minecraft.world.level.block.state.BlockState state) {
        // Chisel doesn't mine blocks effectively, it's for workbench use
        return false;
    }
}
