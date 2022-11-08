package leaf.cosmere.sandmastery.common.utils;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MiscHelper {
    public static boolean checkIfNearbyInvestiture(ServerLevel pLevel, BlockPos pPos) {

        int range = 10;
        AABB areaOfEffect = new AABB(pPos).inflate(range, range, range);
        List<LivingEntity> entitiesToCheckForInvesiture = pLevel.getEntitiesOfClass(LivingEntity.class, areaOfEffect, e-> true);

        AtomicBoolean foundSomething = new AtomicBoolean(false);
        for (LivingEntity targetEntity : entitiesToCheckForInvesiture)
        {
			MobEffectInstance copperEffect = targetEntity.getEffect(ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("allomancy", "copper_cloud")));
			if (copperEffect != null && copperEffect.getDuration() > 0)
			{
				//skip clouded entities.
				continue;
			}

            SpiritwebCapability.get(targetEntity).ifPresent(targetSpiritweb ->
            {
                // Check for Allomancy nearby
                for (Metals.MetalType metalType : Metals.MetalType.values())
                {
                    if (metalType == Metals.MetalType.COPPER) continue;
                    if (!metalType.hasAssociatedManifestation()) continue;

                    int metalTypeID = metalType.getID();
                    if (targetSpiritweb.canTickManifestation(Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(metalTypeID)))
                    {
                        foundSomething.set(true);
                    }
                }

                // Check for Surgebinding nearby
                for (Roshar.Surges surge : Roshar.Surges.values())
                {
                    int surgeID = surge.getID();
                    if(targetSpiritweb.canTickManifestation(Manifestations.ManifestationTypes.SURGEBINDING.getManifestation(surgeID))) {
                        foundSomething.set(true);
                    }
                }
            });
        }
        return foundSomething.get();
    }

    public static int distanceFromGround(LivingEntity e) {
        BlockPos pos = e.blockPosition();
        double y = pos.getY();
        int dist = 0;
        for (double i = y; i >= e.level.getMinBuildHeight(); i--) {
            BlockState block = e.level.getBlockState(pos.offset(0, -dist, 0));
            if (!block.isAir()) return dist;
            dist++;
        }

        return -1;
    }

    public static boolean isActivatedAndActive(ISpiritweb data, Manifestation manifestation) {
        return (Keybindings.MANIFESTATION_USE_ACTIVE.isDown() && data.getSelectedManifestation() == manifestation);
    }
}
