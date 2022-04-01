/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.charge.MetalmindChargeHelper;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.utils.helpers.EffectsHelper;
import leaf.cosmere.items.IHasMetalType;
import leaf.cosmere.manifestation.ManifestationBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.RegistryObject;

public class FeruchemyBase extends ManifestationBase implements IHasMetalType
{
    protected final Metals.MetalType metalType;

    public FeruchemyBase(Metals.MetalType metalType)
    {
        super(Manifestations.ManifestationTypes.FERUCHEMY, metalType.getColorValue());
        this.metalType = metalType;
    }

    @Override
    public int getPowerID()
    {
        return metalType.getID();
    }

    @Override
    public Metals.MetalType getMetalType()
    {
        return this.metalType;
    }

    @Override
    public boolean modeWraps(ISpiritweb data)
    {
        return false;
    }

    //storing is positive, eg adding to store
    @Override
    public int modeMax(ISpiritweb data)
    {
        return 3;
    }

    //tapping is negative, eg taking from store
    @Override
    public int modeMin(ISpiritweb data)
    {
        return -10;
    }

    @Override
    public void tick(ISpiritweb data)
    {
        //don't check every tick.
        LivingEntity livingEntity = data.getLiving();

        if (livingEntity.tickCount % 20 != 0)
        {
            return;
        }

        int mode = data.getMode(manifestationType, metalType.getID());

        int cost;

        Effect effect = getEffect(mode);

        // if we are tapping
        //check if there is charges to tap
        if (mode < 0)
        {
            //wanting to tap
            //get cost
            cost = mode <= -3 ? -(mode * mode) : mode;

        }
        //if we are storing
        //check if there is space to store
        else if (mode > 0)
        {
            cost = mode;
        }
        //can't store or tap any more
        else
        {
            //remove active effects.
            //let the current effect run out.
            return;
        }

        if (MetalmindChargeHelper.adjustMetalmindChargeExact(data, metalType, -cost, true, true))
        {
            EffectInstance currentEffect = EffectsHelper.getNewEffect(effect, Math.abs(mode) - 1);

            if (effect == null)
            {
                return;
            }

            livingEntity.addEffect(currentEffect);
        }

    }

    protected Effect getEffect(int mode)
    {
        if (mode == 0)
        {
            return null;
        }
        else if (mode < 0)
        {
            return metalType.getTappingEffect();
        }
        else
        {
            return metalType.getStoringEffect();
        }

    }

    public double getStrength(ISpiritweb cap)
    {
        RegistryObject<Attribute> mistingAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getFerringName());
        ModifiableAttributeInstance attribute = cap.getLiving().getAttribute(mistingAttribute.get());
        return attribute != null ? attribute.getValue() : 0;
    }
}
