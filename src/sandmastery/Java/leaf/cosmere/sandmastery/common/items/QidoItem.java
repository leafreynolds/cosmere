package leaf.cosmere.sandmastery.common.items;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.itemgroups.CosmereItemGroups;
import leaf.cosmere.common.items.ChargeableItemBase;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.registries.SandmasteryManifestations;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class QidoItem extends ChargeableItemBase {
    public QidoItem() {
        super(PropTypes.Items.ONE.get().tab(CosmereItemGroups.ITEMS));
    }

    @Override
    public int getMaxCharge(ItemStack itemStack)
    {
        return Mth.floor(1000 * getMaxChargeModifier());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
    {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

        if (pLevel.isClientSide)
        {
            return InteractionResultHolder.pass(itemStack);
        }

        SpiritwebCapability.get(pPlayer).ifPresent(spiritweb ->
        {
            SpiritwebCapability data = (SpiritwebCapability) spiritweb;

            boolean isMaster = SandmasteryManifestations.SANDMASTERY_POWERS.values().stream().anyMatch((manifestation -> spiritweb.hasManifestation(manifestation.getManifestation())));

            if (!isMaster) return;

            final int liquid = getCharge(itemStack);

            SandmasterySpiritwebSubmodule sb = (SandmasterySpiritwebSubmodule) data.spiritwebSubmodules.get(Manifestations.ManifestationTypes.SANDMASTERY);

            int playerHydration = sb.getHydrationLevel();
            final int maxPlayerHydration = sb.maxHydration;

            if (liquid + playerHydration > maxPlayerHydration)
            {
                sb.adjustHydration((maxPlayerHydration - playerHydration), true);
                setCharge(itemStack, ((liquid + playerHydration) - maxPlayerHydration));
            }
            else
            {
                sb.adjustHydration(liquid, true);
                setCharge(itemStack, 0);
            }
        });


        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack)
    {
        return stack.isEnchanted();
    }



}
