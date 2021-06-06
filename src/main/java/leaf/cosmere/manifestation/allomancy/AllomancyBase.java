/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.charge.MetalmindChargeHelper;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.IHasMetalType;
import leaf.cosmere.manifestation.ManifestationBase;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.KeybindingRegistry;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.RegistryObject;

public class AllomancyBase extends ManifestationBase implements IHasMetalType
{
    private final Metals.MetalType metalType;

    public AllomancyBase(Metals.MetalType metalType)
    {
        super(Manifestations.ManifestationTypes.ALLOMANCY, metalType.getColorValue());
        this.metalType = metalType;
    }


    @Override
    public int getPowerID()
    {
        return metalType.getID();
    }

    @Override
    public boolean modeWraps(ISpiritweb data)
    {
        return true;
    }

    @Override
    public Metals.MetalType getMetalType()
    {
        return this.metalType;
    }

    //active or not active
    @Override
    public int modeMax(ISpiritweb data)
    {
        return 3;
    }

    @Override
    public int modeMin(ISpiritweb data)
    {
        return 0;
    }

    @Override
    public boolean isActive(ISpiritweb data)
    {
        return super.isActive(data) && isMetalBurning(data);
    }

    public boolean isMetalBurning(ISpiritweb data)
    {
        int mode = data.getMode(manifestationType, metalType.getID());

        //make sure the user can afford the cost of burning this metal
        while (mode > 0)
        {
            //if not then try reduce the amount that they are burning

            if (data.adjustIngestedMetal(metalType, -mode, false))
            {
                return true;
            }
            else
            {
                mode--;
                //set that mode back to the capability.
                data.setMode(manifestationType, metalType.getID(), mode);
                //if it hits zero then return out
                if (mode == 0)
                {
                    break;
                }

                //try again at a lower burn rate.
            }
        }
        return false;
    }

    @Override
    public void tick(ISpiritweb data)
    {
        int mode = data.getMode(manifestationType, metalType.getID());

        if (!isActive(data))
        {
            return;
        }

        //don't check every tick.
        LivingEntity livingEntity = data.getLiving();
        boolean isActiveTick = livingEntity.ticksExisted % 20 == 0;
        data.adjustIngestedMetal(metalType, -mode, isActiveTick);

        //if we get to this point, we are in an active burn state.
        //check for compound.
        int feruchemyMode = data.hasManifestation(Manifestations.ManifestationTypes.FERUCHEMY, getPowerID())
                            ? data.getMode(Manifestations.ManifestationTypes.FERUCHEMY, metalType.getID())
                            : 0;

        //feruchemy power exists and is active
        if (feruchemyMode != 0 && isActiveTick)
        {
            if (MetalmindChargeHelper.adjustMetalmindChargeExact(data, metalType, (-5) * (mode), true, true))
            {
                //compound successful
            }
        }

        performEffect(data);
    }

    protected void performEffect(ISpiritweb data) {  }

    protected KeyBinding getKeyBinding()
    {
        if (getMetalType().isPullMetal())
        {
            return KeybindingRegistry.ALLOMANCY_PULL;
        }
        else if (getMetalType().isPushMetal())
        {
            return KeybindingRegistry.ALLOMANCY_PUSH;
        }

        return null;
    }

    protected double getAllomanticStrength(ISpiritweb cap)
    {
        RegistryObject<Attribute> mistingAttribute = AttributesRegistry.MANIFESTATION_STRENGTH_ATTRIBUTES.get(metalType.getMistingName());
        ModifiableAttributeInstance attribute = cap.getLiving().getAttribute(mistingAttribute.get());
        return attribute != null ? attribute.getValue() : 0;
    }


    public int getRange(ISpiritweb cap)
    {
        if (!isActive(cap))
            return 0;

        //get allomantic strength
        double allomanticStrength = getAllomanticStrength(cap);
        return MathHelper.floor(allomanticStrength * cap.getMode(Manifestations.ManifestationTypes.ALLOMANCY, getPowerID()));

    }
}
