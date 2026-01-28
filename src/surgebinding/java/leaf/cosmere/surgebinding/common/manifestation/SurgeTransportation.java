/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Roshar;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
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
	static List<Entity> targets = new LinkedList<>();
	static int shiftDuration=0;

	public static void onShift(MovementInputUpdateEvent event){
		if(event.getInput().shiftKeyDown){
			chargeUp(event);
		}
		else{
			if(shiftDuration>0){
				shiftDuration=0;
				System.out.println(targets);
				for(Entity target : targets){
					target.setPos(new Vec3(50,50,50));
				}
			}
		}
	}

	public static void chargeUp(MovementInputUpdateEvent event){
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
