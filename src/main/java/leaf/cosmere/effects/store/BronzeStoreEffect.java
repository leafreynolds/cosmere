/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;

//wakefulness
public class BronzeStoreEffect extends FeruchemyEffectBase
{
    public BronzeStoreEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        //sleep
        if (!(entityLivingBaseIn instanceof PlayerEntity))
        {
            return;
        }

        PlayerEntity player = (PlayerEntity) entityLivingBaseIn;

        player.trySleep(player.getPosition()).ifLeft((result) ->
        {
            if (result != null)
            {
                player.sendStatusMessage(result.getMessage(), true);
            }

        });
    }
}
