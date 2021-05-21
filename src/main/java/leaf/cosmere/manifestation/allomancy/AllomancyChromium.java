/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;

public class AllomancyChromium extends AllomancyBase
{
    public AllomancyChromium(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    protected void performEffect(ISpiritweb data)
    {
        //Wipes Allomantic Reserves of Target
        {
            int range = data.getMode(manifestationType, getMetalType().getID());

            LivingEntity living = data.getLiving();
            World world = living.world;
            boolean isActiveTick = living.world.getGameTime() % 20 == 0;
            if (!world.isRemote && isActiveTick)
            {
                //thank you to CyclopsMC and their repo EverlastingAbilities for their section on detecting enemies you are looking at
                //https://github.com/CyclopsMC/EverlastingAbilities/blob/master-1.16/src/main/java/org/cyclops/everlastingabilities/ability/AbilityTypePowerStare.java

                double eyeHeight = living.getEyeHeight();
                Vector3d lookVec = living.getLookVec();
                Vector3d origin = new Vector3d(living.getPosX(), living.getPosY() + eyeHeight, living.getPosZ());
                Vector3d direction = origin.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

                List<Entity> entitiesInRange = world.getEntitiesWithinAABBExcludingEntity(living,
                        living.getBoundingBox()
                                .expand(
                                        lookVec.x * range,
                                        lookVec.y * range,
                                        lookVec.z * range)
                                .grow(range));

                for (Entity e : entitiesInRange)
                {
                    SpiritwebCapability.get((LivingEntity) e).ifPresent(iSpiritweb ->
                    {
                        Entity entity = null;

                        float f10 = e.getCollisionBorderSize();
                        AxisAlignedBB axisalignedbb = e.getBoundingBox().expand((double) f10, (double) f10, (double) f10);
                        Vector3d hitVec = axisalignedbb.rayTrace(origin, direction).orElse(null);

                        if (axisalignedbb.contains(origin))
                        {
                            entity = e;
                        }
                        else if (hitVec != null)
                        {
                            double distance = origin.distanceTo(hitVec);
                            if (distance < range || range == 0.0D)
                            {
                                if (e == living.getRidingEntity() && !living.canRiderInteract())
                                {
                                    if (range == 0.0D)
                                    {
                                        entity = e;
                                    }
                                }
                                else
                                {
                                    entity = e;
                                }
                            }
                        }

                        if (entity != null)
                        {
                            for(Metals.MetalType metalType : Metals.MetalType.values())
                            {
                                iSpiritweb.adjustIngestedMetal(metalType, 9, true);
                            }
                        }
                    });

                }
            }
        }


    }


}
