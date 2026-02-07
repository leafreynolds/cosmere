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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.MovementInputUpdateEvent;

import java.util.LinkedList;
import java.util.List;

public class SurgeTransportation extends SurgebindingManifestation
{
	public SurgeTransportation(Roshar.Surges surge)
	{
		super(surge);
	}

	//travel between realms or locations

	@Override
	public boolean tick(ISpiritweb data)
	{
		SpiritwebCapability.get(data.getLiving()).ifPresent(iSpiritweb ->
		{
			if (iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.TRANSPORTATION).get()) &&
				SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.TRANSPORTATION).getManifestation().isActive(iSpiritweb))
			{
				SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
				LivingEntity living = data.getLiving();
				AABB areaEffect = new AABB(new Vec3(living.getX()-3, living.getY()-3, living.getZ()-3), new Vec3(living.getX()+3, living.getY()+3, living.getZ()+3));
				List<Entity> entitiesNear = living.level().getEntities(living,areaEffect);
				List<LivingEntity> entityList= new LinkedList<>();
				for(Entity entity : entitiesNear){
					if(entity instanceof LivingEntity){
						entityList.add((LivingEntity)entity);
					}
				}
				for(LivingEntity entity : entityList){
					entity.addEffect(EffectsHelper.getNewEffect(MobEffects.GLOWING,9,4));
				}
			}

		});

		return super.tick(data);
	}

	static List<Entity> targets = new LinkedList<>();
	static int shiftDuration=0;

	public static void onShift(MovementInputUpdateEvent event)
	{
		SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
		{
			if (iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.TRANSPORTATION).get()) &&
				SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.TRANSPORTATION).getManifestation().isActive(iSpiritweb) &&
				iSpiritweb.getMode(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.TRANSPORTATION).getManifestation())>=3)
			{
				SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
				if(event.getInput().shiftKeyDown){
					if(!(submodule.getIdeal()>3)){
						if(shiftDuration==10){
							shiftDuration=10;
						}
					}
					chargeUp(event);
				}
				else{
					if(shiftDuration>0){
						shiftDuration=0;
						System.out.println(targets);
						targets.add(0, iSpiritweb.getLiving());
						for(Entity target : targets){
							if(submodule.adjustStormlight(60,true))
							{
								//Teleport Effect
							}
						}
					}
				}
			}
		});
	}

	public static void chargeUp(MovementInputUpdateEvent event)
	{
		shiftDuration++;
		Level level = event.getEntity().level();
		AABB box = AABB.ofSize(event.getEntity().getEyePosition().add(0,-0.5,0),shiftDuration*0.05,shiftDuration*0.05,shiftDuration*0.05);
		targets = level.getEntities(event.getEntity(), box);
		SimpleParticleType s = ParticleTypes.PORTAL;
		for(double i = box.minX; i<box.maxX;i+=0.1){
			level.addParticle(s,i,box.minY,box.minZ,0,0,0);
			level.addParticle(s,i,box.maxY,box.minZ,0,0,0);
			level.addParticle(s,i,box.maxY,box.maxZ,0,0,0);
			level.addParticle(s,i,box.minY,box.maxZ,0,0,0);
		}
		for(double i = box.minY; i<box.maxY;i+=0.1){
			level.addParticle(s,box.minX,i,box.minZ,0,0,0);
			level.addParticle(s,box.maxX,i,box.minZ,0,0,0);
			level.addParticle(s,box.minX,i,box.maxZ,0,0,0);
			level.addParticle(s,box.maxX,i,box.maxZ,0,0,0);
		}
		for(double i = box.minZ; i<box.maxZ;i+=0.1){
			level.addParticle(s,box.minX,box.minY,i,0,0,0);
			level.addParticle(s,box.maxX,box.minY,i,0,0,0);
			level.addParticle(s,box.minX,box.maxY,i,0,0,0);
			level.addParticle(s,box.maxX,box.maxY,i,0,0,0);
		}
		for (Entity target : targets)
		{
			for(double y = 0; y<2.1; y+=0.2)
				level.addParticle(ParticleTypes.END_ROD,target.getX(),target.getBbHeight()+target.getY()+y,target.getZ(),0,0,0);
		}
	}
}
