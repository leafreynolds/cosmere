/*
 * File updated ~ 7 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.capabilities.AllomancySpiritwebSubmodule;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AllomancyChromium extends AllomancyManifestation
{
	public AllomancyChromium(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		//Wipes Allomantic Reserves of Target
		{
			int range = getRange(data);

			LivingEntity living = data.getLiving();
			Level world = living.level();
			boolean isActiveTick = isActiveTick(data);
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

				for (Entity targetEntity : entitiesInRange)
				{
					if (!(targetEntity instanceof LivingEntity))
					{
						continue;
					}
					SpiritwebCapability.get((LivingEntity) targetEntity).ifPresent(targetISpiritweb ->
					{
						Entity entity = null;
						final SpiritwebCapability targetSpiritweb = (SpiritwebCapability) targetISpiritweb;

						float f10 = targetEntity.getPickRadius();
						AABB axisalignedbb = targetEntity.getBoundingBox().expandTowards(f10, f10, f10);
						Vec3 hitVec = axisalignedbb.clip(origin, direction).orElse(null);

						if (axisalignedbb.contains(origin))
						{
							entity = targetEntity;
						}
						else if (hitVec != null)
						{
							double distance = origin.distanceTo(hitVec);
							if (distance < range || range == 0.0D)
							{
								if (targetEntity == living.getVehicle() && !living.canRiderInteract())
								{
									if (range == 0.0D)
									{
										entity = targetEntity;
									}
								}
								else
								{
									entity = targetEntity;
								}
							}
						}

						if (entity != null)
						{
							AllomancySpiritwebSubmodule allo = (AllomancySpiritwebSubmodule) targetSpiritweb.getSubmodule(Manifestations.ManifestationTypes.ALLOMANCY);
							for (Metals.MetalType metalType : Metals.MetalType.values())
							{
								float drainAmount = allo.getIngestedMetal(metalType) * 0.1f;
								allo.adjustIngestedMetal(metalType, (int) -drainAmount, true);
							}
						}
					});

				}
			}
		}


	}


}
