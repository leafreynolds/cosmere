/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

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
			int range = getRange(data);

			LivingEntity living = data.getLiving();
			Level world = living.level;
			boolean isActiveTick = living.level.getGameTime() % 20 == 0;
			if (!world.isClientSide && isActiveTick)
			{
				//thank you to CyclopsMC and their repo EverlastingAbilities for their section on detecting enemies you are looking at
				//https://github.com/CyclopsMC/EverlastingAbilities/blob/master-1.16/src/main/java/org/cyclops/everlastingabilities/ability/AbilityTypePowerStare.java

				double eyeHeight = living.getEyeHeight();
				Vec3 lookVec = living.getLookAngle();
				Vec3 origin = new Vec3(living.getX(), living.getY() + eyeHeight, living.getZ());
				Vec3 direction = origin.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

				List<Entity> entitiesInRange = world.getEntities(living,
						living.getBoundingBox()
								.inflate(
										lookVec.x * range,
										lookVec.y * range,
										lookVec.z * range)
								.inflate(range));

				for (Entity e : entitiesInRange)
				{
					if (!(e instanceof LivingEntity))
					{
						continue;
					}
					SpiritwebCapability.get((LivingEntity) e).ifPresent(iSpiritweb ->
					{
						Entity entity = null;

						float f10 = e.getPickRadius();
						AABB axisalignedbb = e.getBoundingBox().expandTowards(f10, f10, f10);
						Vec3 hitVec = axisalignedbb.clip(origin, direction).orElse(null);

						if (axisalignedbb.contains(origin))
						{
							entity = e;
						}
						else if (hitVec != null)
						{
							double distance = origin.distanceTo(hitVec);
							if (distance < range || range == 0.0D)
							{
								if (e == living.getVehicle() && !living.canRiderInteract())
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
							for (Metals.MetalType metalType : Metals.MetalType.values())
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
