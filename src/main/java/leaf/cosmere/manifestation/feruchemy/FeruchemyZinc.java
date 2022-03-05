/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.EffectsRegistry;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerXpEvent;

public class FeruchemyZinc extends FeruchemyBase
{
    public FeruchemyZinc(Metals.MetalType metalType)
    {
        super(metalType);
        MinecraftForge.EVENT_BUS.addListener(this::onXPChange);
    }

    public void onXPChange(PlayerXpEvent.XpChange event)
    {
        boolean isRemote = event.getEntityLiving().level.isClientSide;
        if (isRemote)
        {
            return;
        }
        EffectInstance tappingZincEffect = event.getPlayer().getEffect(EffectsRegistry.TAPPING_EFFECTS.get(Metals.MetalType.ZINC).get());
        EffectInstance storingZincEffect = event.getPlayer().getEffect(EffectsRegistry.STORING_EFFECTS.get(Metals.MetalType.ZINC).get());

        if (tappingZincEffect != null)
        {
            event.setAmount(event.getAmount() * (tappingZincEffect.getAmplifier() + 2));

        }
        else if (storingZincEffect != null)
        {
            event.setAmount(event.getAmount() / (storingZincEffect.getAmplifier() + 2));
        }

    }

}
