/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;
import java.util.List;

public class SurgeTransportation extends SurgebindingManifestation
{
	//static Map<UUID, Integer> shiftDuration  = new HashMap<>();
	//static Map<UUID, List<Entity>> targets = new HashMap<>();

	public SurgeTransportation(Roshar.Surges surge)
	{
		super(surge);
	}

	//travel between realms or locations

	@Override
	public boolean tick(ISpiritweb data)
	{
		//shiftDuration.putIfAbsent(data.getLiving().getUUID(), 0);
		//targets.putIfAbsent(data.getLiving().getUUID(), new LinkedList<>());
		SpiritwebCapability.get(data.getLiving()).ifPresent(iSpiritweb ->
		{

			if (SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.TRANSPORTATION).getManifestation() instanceof SurgebindingManifestation sg &&
					sg.isActive(iSpiritweb))
			{
				SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
				LivingEntity living = data.getLiving();
				AABB areaEffect = new AABB(new Vec3(living.getX() - 3, living.getY() - 3, living.getZ() - 3), new Vec3(living.getX() + 3, living.getY() + 3, living.getZ() + 3));
				List<Entity> entitiesNear = living.level().getEntities(living, areaEffect);
				List<LivingEntity> entityList = new LinkedList<>();
				for (Entity entity : entitiesNear)
				{
					if (entity instanceof LivingEntity)
					{
						entityList.add((LivingEntity) entity);
					}
				}

				for (LivingEntity entity : entityList)
				{
					if (!entity.hasEffect(MobEffects.GLOWING) && submodule.adjustStormlight(-5, true))
					{
						entity.addEffect(EffectsHelper.getNewEffect(MobEffects.GLOWING, 9, 10));
					}
				}
			}

		});
		return super.tick(data);
	}


/*
	public static void onShift(MovementInputUpdateEvent event)
	{
		UUID uuid = event.getEntity().getUUID();
		SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
		{
			if (SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.TRANSPORTATION).getManifestation() instanceof SurgebindingManifestation sg &&
				sg.isActive(iSpiritweb) &&
				iSpiritweb.getMode(sg) >= 3)
			{
				SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
				if (event.getInput().shiftKeyDown)
				{
					chargeUp(event);
					if (!(submodule.getIdeal() > 3))
					{
						if (shiftDuration.get(uuid) >= 10)
						{
							shiftDuration.replace(uuid,10);
						}
					}
				}
				else
				{
					if (shiftDuration.get(uuid) > 0)
					{
						shiftDuration.replace(uuid,0);
						if (!(submodule.getIdeal() > 3))
						{
							targets.clear();
						}
						targets.get(uuid).add(0, iSpiritweb.getLiving());
						for (Entity target : targets.get(uuid))
						{
							//to do Teleport effect and cost
						}
					}
				}
			}
		});
	}

	public static void chargeUp(MovementInputUpdateEvent event)
	{
		shiftDuration.replace(event.getEntity().getUUID(),shiftDuration.get(event.getEntity().getUUID())+1);
		Level level = event.getEntity().level();
		AABB box = AABB.ofSize(event.getEntity().getEyePosition().add(0,-0.5,0),shiftDuration.get(event.getEntity().getUUID())*0.05,shiftDuration.get(event.getEntity().getUUID())*0.05,shiftDuration.get(event.getEntity().getUUID())*0.05);
		targets.replace(event.getEntity().getUUID(),level.getEntities(event.getEntity(), box));
		SimpleParticleType s = ParticleTypes.PORTAL;
		if(level instanceof ServerLevel serverLevel)
		{
			for (double i = box.minX; i < box.maxX; i += 0.1)
			{
				serverLevel.sendParticles(s, i, box.minY, box.minZ, 1, 0, 0,0,0.1);
				serverLevel.sendParticles(s, i, box.maxY, box.minZ, 1, 0, 0,0,0.1);
				serverLevel.sendParticles(s, i, box.maxY, box.maxZ, 1, 0, 0,0,0.1);
				serverLevel.sendParticles(s, i, box.minY, box.maxZ, 1, 0, 0,0,0.1);
			}
			for (double i = box.minY; i < box.maxY; i += 0.1)
			{
				serverLevel.sendParticles(s, box.minX, i, box.minZ, 1, 0, 0,0,0.1);
				serverLevel.sendParticles(s, box.maxX, i, box.minZ, 1, 0, 0,0,0.1);
				serverLevel.sendParticles(s, box.minX, i, box.maxZ, 1, 0, 0,0,0.1);
				serverLevel.sendParticles(s, box.maxX, i, box.maxZ, 1, 0, 0,0,0.1);
			}
			for (double i = box.minZ; i < box.maxZ; i += 0.1)
			{
				serverLevel.sendParticles(s, box.minX, box.minY, i, 1, 0, 0,0,0.1);
				serverLevel.sendParticles(s, box.maxX, box.minY, i, 1, 0, 0,0,0.1);
				serverLevel.sendParticles(s, box.minX, box.maxY, i, 1, 0, 0,0,0.1);
				serverLevel.sendParticles(s, box.maxX, box.maxY, i, 1, 0, 0,0,0.1);
			}
			for (Entity target : targets.get(event.getEntity().getUUID()))
			{
				for (double y = 0; y < 2.1; y += 0.2)
					serverLevel.sendParticles(ParticleTypes.END_ROD, target.getX(), target.getBbHeight() + target.getY() + y, target.getZ(), 2, 0, 0, 0, 0.1);
			}
		}
	}
 */
}
